package com.fadlurahmanf.bebas_onboarding.external

import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.fadlurahmanf.bebas_onboarding.presentation.vc.DebugVideoCallActivity
import com.fadlurahmanf.bebas_shared.BebasShared
import com.neovisionaries.ws.client.ThreadType
import com.neovisionaries.ws.client.WebSocket
import com.neovisionaries.ws.client.WebSocketException
import com.neovisionaries.ws.client.WebSocketFactory
import com.neovisionaries.ws.client.WebSocketFrame
import com.neovisionaries.ws.client.WebSocketListener
import com.neovisionaries.ws.client.WebSocketState
import org.json.JSONException
import org.json.JSONObject
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection.IceServer
import org.webrtc.SessionDescription
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class CustomWebSocket(
    private val session: RTCSession,
    private val activity: DebugVideoCallActivity
) :
    AsyncTask<DebugVideoCallActivity, Void, Void>(),
    WebSocketListener {

    private val TAG = "BebasLoggerRTC"
    private val ID_JOINROOM = AtomicInteger(-1)
    private val ID_PUBLISHVIDEO = AtomicInteger(-1)
    private val IDS_ONICECANDIDATE = Collections.newSetFromMap(ConcurrentHashMap<Int, Boolean>())
    private val IDS_PREPARERECEIVEVIDEO: HashMap<Int, Pair<String, String>> = hashMapOf()
    private val IDS_RECEIVEVIDEO: HashMap<Int, String> = hashMapOf()
    private val trustManagers = arrayOf<TrustManager>(object : X509TrustManager {
        override fun getAcceptedIssuers(): Array<X509Certificate?> {
            return arrayOfNulls<X509Certificate>(0)
        }

        @Throws(CertificateException::class)
        override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String) {
            Log.i(TAG, ": authType: $authType")
        }

        @Throws(CertificateException::class)
        override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String) {
            Log.i(TAG, ": authType: $authType")
        }
    })

    private lateinit var webSocket: WebSocket
    private val ID_PING = AtomicInteger(-1)
    private val RPC_ID = AtomicInteger(0)

    private lateinit var mediaServer: String

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: DebugVideoCallActivity?): Void? {
        try {
            val factory = WebSocketFactory()

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagers, SecureRandom())
            factory.sslContext = sslContext

            factory.verifyHostname = false

            webSocket = factory.createSocket(getWebSocketAddress())
            webSocket.addListener(this)
            webSocket.connect()
        } catch (e: Exception) {
            Log.e(TAG, "ERROR DO IN BACKGROUND: ${e.message}", e)
        }
        return null
    }

    private fun getWebSocketAddress(): String {
        val wsuri =
            "wss://${BebasShared.getOpenviduHost()}:443/openvidu?sessionId=${session.sessionId}&token=${session.sessionToken}"
        Log.d(TAG, "WSURI WEBSOCKET: ${wsuri}")
        return wsuri
    }

    override fun onTextMessage(websocket: WebSocket?, text: String?) {
        Log.d(TAG, "<--- BEBAS ON TEXT MESSAGE: $text --->")
        if (text != null) {
            val json = JSONObject(text)

            if (json.has(JsonConstants.RESULT)) {
                handleServerResponse(json)
            } else if (json.has(JsonConstants.ERROR)) {
                handleServerError(json)
            } else if (json.has(JsonConstants.METHOD)) {
                handleServerMethod(json)
            }
        }
    }

    override fun onTextMessage(websocket: WebSocket?, data: ByteArray?) {

    }

    private fun handleServerResponse(json: JSONObject) {
        Log.d(TAG, "<--- HANDLE RESULT SERVER: $json --->")
        val rpcId = json.getInt(JsonConstants.ID)
        val result = JSONObject(json.getString(JsonConstants.RESULT))

        if (result.has("value") && result.getString("value").equals("pong")) {
            // Response to ping
            Log.i(TAG, "pong")
        } else if (rpcId == ID_JOINROOM.get()) {
            val localParticipant = session.localParticipant
            val localConnectionId = result.getString(JsonConstants.MEDIA_SERVER)
            localParticipant.connectionId = localConnectionId
            mediaServer = result.getString(JsonConstants.MEDIA_SERVER)

            if (result.has(JsonConstants.ICE_SERVERS)) {
                val jsonIceServers = result.getJSONArray(JsonConstants.ICE_SERVERS)
                val iceServers: ArrayList<IceServer> = arrayListOf()
                for (i in 0 until jsonIceServers.length()) {
                    val jsonIceServer = jsonIceServers.getJSONObject(i)
                    val urls: MutableList<String> = arrayListOf()
                    if (jsonIceServer.has("urls")) {
                        val jsonUrls = jsonIceServer.getJSONArray("urls")
                        for (j in 0 until jsonUrls.length()) {
                            urls.add(jsonUrls.getString(j))
                        }
                    }
                    if (jsonIceServer.has("url")) {
                        urls.add(jsonIceServer.getString("url"))
                    }
                    var iceServerBuilder: IceServer.Builder
                    try {
                        iceServerBuilder = IceServer.builder(urls)
                    } catch (e: IllegalArgumentException) {
                        Log.e(TAG, "JOIN ROOM METHOD ERROR: ${e.message}")
                        return
                    }
                    if (jsonIceServer.has("username")) {
                        iceServerBuilder.setUsername(jsonIceServer.getString("username"))
                    }
                    if (jsonIceServer.has("credential")) {
                        iceServerBuilder.setPassword(jsonIceServer.getString("credential"))
                    }
                    iceServers.add(iceServerBuilder.createIceServer())
                }
                session.setIceServers(iceServers)
            }

            val localPeerConnection = session.createLocalPeerConnection()

            localParticipant.setPeerConnection(localPeerConnection)

            val sdpConstraints = MediaConstraints()
            sdpConstraints.mandatory.add(
                MediaConstraints.KeyValuePair(
                    "offerToReceiveAudio",
                    "true"
                )
            )
            sdpConstraints.mandatory.add(
                MediaConstraints.KeyValuePair(
                    "offerToReceiveVideo",
                    "true"
                )
            )
            session.createOfferForPublishing(sdpConstraints)

            if (result.getJSONArray(JsonConstants.VALUE).length() > 0) {
                // There were users already connected to the session
                addRemoteParticipantsAlreadyInRoom(result)
            }
        }
    }

    private fun addRemoteParticipantsAlreadyInRoom(result: JSONObject) {
        val participantsAlreadyInRoom = result.getJSONArray(JsonConstants.VALUE)
        Log.d(TAG, "TOTAL PARTICIPANT ALREADY IN ROOM ${participantsAlreadyInRoom.length()}")
        for (i in 0 until participantsAlreadyInRoom.length()) {
            val participantJson = participantsAlreadyInRoom.getJSONObject(i)
            Log.d(TAG, "PARTICIPANT ALREADY IN ROOM: $participantJson")
            val remoteParticipant: RemoteParticipant = newRemoteParticipantAux(participantJson)
            try {
                val streams = participantJson.getJSONArray("streams")
                for (j in 0 until streams.length()) {
                    val stream = streams.getJSONObject(0)
                    val streamId = stream.getString("id")
                    this.subscribe(remoteParticipant, streamId)
                }
            } catch (e: java.lang.Exception) {
                //Sometimes when we enter in room the other participants have no stream
                //We catch that in this way the iteration of participants doesn't stop
                Log.e(TAG, "Error in addRemoteParticipantsAlreadyInRoom: " + e.localizedMessage)
            }
        }
    }

    private fun newRemoteParticipantAux(participantJson: JSONObject): RemoteParticipant {
        val connectionId = participantJson.getString(JsonConstants.ID)
        var participantName: String? = ""
        val jsonStringified = participantJson.getString(JsonConstants.METADATA)
        try {
            val json = JSONObject(jsonStringified)
            val clientData = json.getString("clientData")
            participantName = clientData
        } catch (e: JSONException) {
            participantName = jsonStringified
        }
        val remoteParticipant = RemoteParticipant(participantName ?: "-", session, connectionId)
        this.activity.createRemoteParticipantVideo(remoteParticipant)
        session.createRemotePeerConnection(remoteParticipant.connectionId)
        return remoteParticipant
    }

    fun prepareReceiveVideoFrom(remoteParticipant: RemoteParticipant, streamId: String?) {
        val prepareReceiveVideoFromParams: MutableMap<String, String?> = HashMap()
        prepareReceiveVideoFromParams["sender"] = streamId
        prepareReceiveVideoFromParams["reconnect"] = "false"
        val ids = sendJson(
            JsonConstants.PREPARERECEIVEVIDEO_METHOD,
            prepareReceiveVideoFromParams
        )
        IDS_PREPARERECEIVEVIDEO[ids] = Pair(remoteParticipant.connectionId, streamId ?: "-")
    }

    private fun subscribe(remoteParticipant: RemoteParticipant, streamId: String) {
        if ("kurento" == mediaServer) {
            subscriptionInitiatedFromClient(remoteParticipant, streamId)
        } else {
            prepareReceiveVideoFrom(remoteParticipant, streamId)
        }
    }

    private fun subscriptionInitiatedFromClient(
        remoteParticipant: RemoteParticipant,
        streamId: String
    ) {
        val sdpConstraints = MediaConstraints()
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"))
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"))
        remoteParticipant.getPeerConnection()!!
            .createOffer(object : CustomSdpObserver("remote create offer") {
                override fun onCreateSuccess(p0: SessionDescription?) {
                    super.onCreateSuccess(p0)
                    remoteParticipant.getPeerConnection()!!
                        .setLocalDescription(object : CustomSdpObserver("remote set description") {
                            override fun onSetSuccess() {
                                super.onSetSuccess()
                                if (p0 != null) {
                                    receiveVideoFrom(p0, remoteParticipant, streamId)
                                }
                            }
                        }, p0)
                }
            }, sdpConstraints)
    }

    fun receiveVideoFrom(
        sessionDescription: SessionDescription,
        remoteParticipant: RemoteParticipant,
        streamId: String?
    ) {
        val receiveVideoFromParams: MutableMap<String, String?> = HashMap()
        receiveVideoFromParams["sender"] = streamId
        if ("kurento" == mediaServer) {
            receiveVideoFromParams["sdpOffer"] = sessionDescription.description
        } else {
            receiveVideoFromParams["sdpAnswer"] = sessionDescription.description
        }
        val ids = sendJson(
            JsonConstants.RECEIVEVIDEO_METHOD,
            receiveVideoFromParams
        )
        this.IDS_RECEIVEVIDEO[ids] = remoteParticipant.connectionId
    }

    private fun handleServerError(json: JSONObject) {

    }

    private fun handleServerMethod(json: JSONObject) {

    }

    override fun onStateChanged(websocket: WebSocket?, newState: WebSocketState?) {

    }

    private val handler = Handler(Looper.getMainLooper())
    private val pingRunnable = object : Runnable {
        override fun run() {
            val pingParams: HashMap<String, String> = HashMap()
            if (ID_PING.get() == -1) {
                pingParams["interval"] = "5000"
            }
            sendJson(JsonConstants.PING_METHOD, pingParams)
            handler.postDelayed(this, 3000)
        }
    }

    @Synchronized
    fun sendJson(method: String, params: Map<String, String?>): Int {
        val id: Int = RPC_ID.get()
        val jsonObject = JSONObject()
        try {
            val paramsJson = JSONObject()
            for ((key, value) in params) {
                paramsJson.put(key, value)
            }
            jsonObject.put("jsonrpc", JsonConstants.JSON_RPCVERSION)
            jsonObject.put("method", method)
            jsonObject.put("id", id)
            jsonObject.put("params", paramsJson)
        } catch (e: JSONException) {
            Log.e(TAG, "JSONException raised on sendJson", e)
            return -1
        }
        webSocket.sendText(jsonObject.toString())
        RPC_ID.incrementAndGet()
        return id
    }

    fun onIceCandidate(iceCandidate: IceCandidate, endpointName: String?) {
        val onIceCandidateParams: MutableMap<String, String?> = HashMap()
        if (endpointName != null) {
            onIceCandidateParams["endpointName"] = endpointName
        }
        onIceCandidateParams["candidate"] = iceCandidate.sdp
        onIceCandidateParams["sdpMid"] = iceCandidate.sdpMid
        onIceCandidateParams["sdpMLineIndex"] = Integer.toString(iceCandidate.sdpMLineIndex)
        this.IDS_ONICECANDIDATE.add(
            sendJson(
                JsonConstants.ONICECANDIDATE_METHOD,
                onIceCandidateParams
            )
        )
    }

    fun joinRoom() {
        try {
            val joinRoomParams: MutableMap<String, String?> = HashMap()
            joinRoomParams[JsonConstants.METADATA] =
                "{\"clientData\": \"" + session.localParticipant.participantName + "\"}"
            joinRoomParams["secret"] = ""
            joinRoomParams["session"] = session.sessionId
            joinRoomParams["platform"] = "Android " + Build.VERSION.SDK_INT
            joinRoomParams["token"] = session.sessionToken
            joinRoomParams["sdkVersion"] = "2.29.0"
            val joinRoomId = sendJson(JsonConstants.JOINROOM_METHOD, joinRoomParams)
            Log.d("BebasLoggerRTC", "ID JOIN ROOM: $joinRoomId")
            ID_JOINROOM.set(joinRoomId)
        } catch (e: java.lang.Exception) {
            Log.d("BebasLoggerRTC", "JOIN ROOM ERROR: ${e.message}")
        }
    }

    fun publishVideo(sessionDescription: SessionDescription) {
        val publishVideoParams: MutableMap<String, String?> = HashMap()
        publishVideoParams["audioActive"] = "true"
        publishVideoParams["videoActive"] = "true"
        publishVideoParams["doLoopback"] = "false"
        publishVideoParams["frameRate"] = "30"
        publishVideoParams["hasAudio"] = "true"
        publishVideoParams["hasVideo"] = "true"
        publishVideoParams["typeOfVideo"] = "CAMERA"
        publishVideoParams["videoDimensions"] = "{\"width\":320, \"height\":240}"
        publishVideoParams["sdpOffer"] = sessionDescription.description
        this.ID_PUBLISHVIDEO.set(sendJson(JsonConstants.PUBLISHVIDEO_METHOD, publishVideoParams))
    }


    override fun onConnected(
        websocket: WebSocket?,
        headers: MutableMap<String, MutableList<String>>?
    ) {
        Log.d(TAG, "WEBRTC Connected")
        handler.postDelayed(pingRunnable, 5000)
        joinRoom()
    }

    override fun onConnectError(websocket: WebSocket?, cause: WebSocketException?) {
        Log.d(TAG, "WEBRTC ERROR: ${cause?.error?.name}")
        Log.d(TAG, "WEBRTC ERROR: ${cause?.message}")
    }

    override fun onDisconnected(
        websocket: WebSocket?,
        serverCloseFrame: WebSocketFrame?,
        clientCloseFrame: WebSocketFrame?,
        closedByServer: Boolean
    ) {

    }

    override fun onFrame(websocket: WebSocket?, frame: WebSocketFrame?) {

    }

    override fun onContinuationFrame(websocket: WebSocket?, frame: WebSocketFrame?) {

    }

    override fun onTextFrame(websocket: WebSocket?, frame: WebSocketFrame?) {

    }

    override fun onBinaryFrame(websocket: WebSocket?, frame: WebSocketFrame?) {

    }

    override fun onCloseFrame(websocket: WebSocket?, frame: WebSocketFrame?) {

    }

    override fun onPingFrame(websocket: WebSocket?, frame: WebSocketFrame?) {

    }

    override fun onPongFrame(websocket: WebSocket?, frame: WebSocketFrame?) {

    }

    override fun onBinaryMessage(websocket: WebSocket?, binary: ByteArray?) {

    }

    override fun onSendingFrame(websocket: WebSocket?, frame: WebSocketFrame?) {

    }

    override fun onFrameSent(websocket: WebSocket?, frame: WebSocketFrame?) {

    }

    override fun onFrameUnsent(websocket: WebSocket?, frame: WebSocketFrame?) {

    }

    override fun onThreadCreated(websocket: WebSocket?, threadType: ThreadType?, thread: Thread?) {

    }

    override fun onThreadStarted(websocket: WebSocket?, threadType: ThreadType?, thread: Thread?) {

    }

    override fun onThreadStopping(websocket: WebSocket?, threadType: ThreadType?, thread: Thread?) {

    }

    override fun onError(websocket: WebSocket?, cause: WebSocketException?) {

    }

    override fun onFrameError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        frame: WebSocketFrame?
    ) {

    }

    override fun onMessageError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        frames: MutableList<WebSocketFrame>?
    ) {

    }

    override fun onMessageDecompressionError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        compressed: ByteArray?
    ) {

    }

    override fun onTextMessageError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        data: ByteArray?
    ) {

    }

    override fun onSendError(
        websocket: WebSocket?,
        cause: WebSocketException?,
        frame: WebSocketFrame?
    ) {

    }

    override fun onUnexpectedError(websocket: WebSocket?, cause: WebSocketException?) {

    }

    override fun handleCallbackError(websocket: WebSocket?, cause: Throwable?) {

    }

    override fun onSendingHandshake(
        websocket: WebSocket?,
        requestLine: String?,
        headers: MutableList<Array<String>>?
    ) {

    }

}