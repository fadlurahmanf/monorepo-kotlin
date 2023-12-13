package com.fadlurahmanf.bebas_onboarding.external

import android.content.Context
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
import org.webrtc.AudioTrack
import org.webrtc.EglBase
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.MediaStreamTrack
import org.webrtc.PeerConnection
import org.webrtc.PeerConnection.IceServer
import org.webrtc.PeerConnectionFactory
import org.webrtc.RtpReceiver
import org.webrtc.RtpTransceiver
import org.webrtc.SessionDescription
import org.webrtc.SoftwareVideoDecoderFactory
import org.webrtc.SoftwareVideoEncoderFactory
import org.webrtc.SurfaceTextureHelper
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoDecoderFactory
import org.webrtc.VideoEncoderFactory
import org.webrtc.VideoSource
import org.webrtc.VideoTrack
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.atomic.AtomicInteger
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class BebasWebSocket(
    context: Context,
    private val sessionId: String,
    private val sessionToken: String
) : AsyncTask<DebugVideoCallActivity, Void, Void>(), WebSocketListener {

    private val TAG = "BebasLoggerRTC"
    private var callback: Callback? = null
    lateinit var localPeerConnection: PeerConnection
    lateinit var remotePeerConnection: PeerConnection
    lateinit var eglBaseContext: EglBase.Context
    lateinit var audioTrack: AudioTrack
    lateinit var videoTrack: VideoTrack

    private val ID_JOINROOM = AtomicInteger(-1)
    private val ID_PUBLISHVIDEO = AtomicInteger(-1)

    private val IDS_ONICECANDIDATE = arrayListOf<Int>()

    private val ICE_CANDIDATES_LOCAL = arrayListOf<IceCandidate>()
    private val ICE_CANDIDATES_REMOTE = arrayListOf<IceCandidate>()

    //    private val IDS_PREPARERECEIVEVIDEO: HashMap<Int, Pair<String, String>> = hashMapOf()
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
    private lateinit var localConnectionId: String
    private lateinit var remoteConnectionId: String

    private lateinit var mediaServer: String

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    var peerConnectionFactory: PeerConnectionFactory

    init {
        val optionBuilder =
            PeerConnectionFactory.InitializationOptions.builder(context)
        optionBuilder.setEnableInternalTracer(true)
        val opt = optionBuilder.createInitializationOptions()
        PeerConnectionFactory.initialize(opt)
        val options = PeerConnectionFactory.Options()

        val encoderFactory: VideoEncoderFactory
        val decoderFactory: VideoDecoderFactory
        encoderFactory = SoftwareVideoEncoderFactory()
        decoderFactory = SoftwareVideoDecoderFactory()

        peerConnectionFactory = PeerConnectionFactory.builder()
            .setVideoEncoderFactory(encoderFactory)
            .setVideoDecoderFactory(decoderFactory)
            .setOptions(options)
            .createPeerConnectionFactory()
    }

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
            "wss://${BebasShared.getOpenviduHost()}:443/openvidu?sessionId=${sessionId}&token=${sessionToken}"
        Log.d(TAG, "WSURI WEBSOCKET: $wsuri")
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


    private fun handleServerResponse(json: JSONObject) {
        Log.d(TAG, "<--- HANDLE RESULT SERVER: $json --->")
        val rpcId = json.getInt(JsonConstants.ID)
        val result = JSONObject(json.getString(JsonConstants.RESULT))

        if (result.has("value") && result.getString("value").equals("pong")) {
            // Response to ping
            Log.i(TAG, "pong")
        } else if (rpcId == ID_JOINROOM.get()) {
            localConnectionId = result.getString(JsonConstants.ID)
            mediaServer = result.getString(JsonConstants.MEDIA_SERVER)

            if (result.has(JsonConstants.ICE_SERVERS)) {
                val jsonIceServers = result.getJSONArray(JsonConstants.ICE_SERVERS)
                val iceServers: ArrayList<IceServer> = arrayListOf()
                for (i in 0 until jsonIceServers.length()) {
                    val jsonIceServer = jsonIceServers.getJSONObject(i)
                    val urls: ArrayList<String> = arrayListOf()
                    if (jsonIceServer.has("urls")) {
                        val jsonUrls = jsonIceServer.getJSONArray("urls")
                        for (j in 0 until jsonUrls.length()) {
                            urls.add(jsonUrls.getString(j))
                        }
                    }
                    if (jsonIceServer.has("url")) {
                        urls.add(jsonIceServer.getString("url"))
                    }
                    val iceServerBuilder: IceServer.Builder = try {
                        IceServer.builder(urls)
                    } catch (e: IllegalArgumentException) {
                        continue
                    }

                    if (jsonIceServer.has("username")) {
                        iceServerBuilder.setUsername(jsonIceServer.getString("username"))
                    }
                    if (jsonIceServer.has("credential")) {
                        iceServerBuilder.setPassword(jsonIceServer.getString("credential"))
                    }
                    iceServers.add(iceServerBuilder.createIceServer())
                }
                this.iceServers = iceServers
            }

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

            initLocalPeerConnection(localConnectionId)
            localPeerConnection.createOffer(
                object : CustomSdpObserver("local peer connection offer") {
                    override fun onCreateSuccess(p0: SessionDescription?) {
                        super.onCreateSuccess(p0)
                        Log.d(TAG, "SUCCESS CREATE OFFER")
                        Log.d(TAG, "type: ${p0?.type}")
                        Log.d(TAG, "sdp: ${p0?.description}")
                        localPeerConnection.setLocalDescription(object :
                                                                    CustomSdpObserver("local peer connection set local description") {
                            override fun onSetSuccess() {
                                super.onSetSuccess()
                                Log.d(TAG, "SUCCESS SET LOCAL DESCRIPTION OFFER")
                                if (p0 != null) {
                                    publishVideo(p0)
                                }
                            }
                        }, p0)
                    }
                }, sdpConstraints
            )

            val anotherParticipants = result.getJSONArray(JsonConstants.VALUE)
            if (anotherParticipants.length() > 0) {
                addRemoteParticipantsAlreadyInRoom(result)
            }

        } else if (rpcId == ID_PUBLISHVIDEO.get()) {
            Log.d(TAG, "ID_PUBLISHVIDEO -> set remote description")
            val remoteAnswer = SessionDescription(
                SessionDescription.Type.ANSWER,
                result.getString(JsonConstants.SDP_ANSWER)
            )
            localPeerConnection.setRemoteDescription(
                object : CustomSdpObserver("local set remote description") {
                    override fun onSetSuccess() {
                        super.onSetSuccess()
                        Log.d(TAG, "LOCAL SET REMOTE DESCRIPTION SUCCESS")
                    }
                },
                remoteAnswer
            )
        } else if (IDS_RECEIVEVIDEO.containsKey(rpcId)) {
            Log.d(TAG, "IDS_RECEIVEVIDEO -> set remote description")
//            val id = IDS_RECEIVEVIDEO.remove(rpcId)
            if ("kurento" == mediaServer) {
                val sessionDescription = SessionDescription(
                    SessionDescription.Type.ANSWER,
                    result.getString(JsonConstants.SDP_ANSWER)
                )
                remotePeerConnection.setRemoteDescription(
                    CustomSdpObserver("remote set remote description"),
                    sessionDescription
                )
            }
        }

        Log.d(
            TAG,
            "IDS_RECEIVEVIDEO KEY: ${
                IDS_RECEIVEVIDEO.keys.toList().joinToString(", ")
            }, rpcId: ${rpcId}"
        )
        Log.d(
            TAG,
            "IDS_RECEIVEVIDEO VALUE: ${
                IDS_RECEIVEVIDEO.values.toList().joinToString(", ")
            }, rpcId: ${rpcId}"
        )

    }

    private fun addRemoteParticipantsAlreadyInRoom(result: JSONObject) {
        val participantsAlreadyInRoom = result.getJSONArray(JsonConstants.VALUE)
        for (i in 0 until participantsAlreadyInRoom.length()) {
            val participantJson = participantsAlreadyInRoom.getJSONObject(i)
            Log.d(TAG, "PARTICIPANT ALREADY IN ROOM ${i + 1}: $participantJson")
            createRemoteParticipant(participantJson)
            try {
                val streams = participantJson.getJSONArray("streams")
                for (j in 0 until streams.length()) {
                    val stream = streams.getJSONObject(0)
                    val streamId = stream.getString("id")
                    subscribe(streamId, stream)
                }
            } catch (e: java.lang.Exception) {
                //Sometimes when we enter in room the other participants have no stream
                //We catch that in this way the iteration of participants doesn't stop
                Log.e(TAG, "Error in addRemoteParticipantsAlreadyInRoom: " + e.localizedMessage)
            }
        }
    }

    private fun subscribe(streamId: String, stream: JSONObject) {
        Log.d(TAG, "REMOTE SUBSCRIBE VIDEO $mediaServer $stream")
        if ("kurento" == mediaServer) {
            subscriptionInitiatedFromClient(streamId)
        } else {
//            this.prepareReceiveVideoFrom(remoteParticipant, streamId)
        }
    }

    private fun subscriptionInitiatedFromClient(streamId: String) {
        val sdpConstraints = MediaConstraints()
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"))
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"))
        remotePeerConnection.createOffer(object : CustomSdpObserver("remote offer sdp") {
            override fun onCreateSuccess(sdp: SessionDescription?) {
                super.onCreateSuccess(sdp)
                Log.d(TAG, "SUCCESS CREATE OFFER REMOTE PEER CONNECTION")
                Log.d(TAG, "type: ${sdp?.type}")
                Log.d(TAG, "description: ${sdp?.description}")
                remotePeerConnection
                    .setLocalDescription(object :
                                             CustomSdpObserver("remote set local description") {
                        override fun onSetSuccess() {
                            super.onSetSuccess()
                            Log.d(TAG, "SUCCESS SET OFFER REMOTE PEER CONNECTION")
                            sdp?.let {
                                receiveVideoFrom(it, streamId)
                            }
                        }
                    }, sdp)
            }
        }, sdpConstraints)
    }

    private fun subscriptionInitiatedFromServer(
        remoteParticipant: RemoteParticipant,
        streamId: String
    ) {
        val sdpConstraints = MediaConstraints()
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"))
        sdpConstraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"))
//        this.session.createAnswerForSubscribing(remoteParticipant, streamId, sdpConstraints)
    }

    fun receiveVideoFrom(
        sessionDescription: SessionDescription,
        streamId: String
    ) {
        val receiveVideoFromParams: MutableMap<String, String?> = HashMap()
        receiveVideoFromParams["sender"] = streamId
        if ("kurento" == mediaServer) {
            receiveVideoFromParams["sdpOffer"] = sessionDescription.description
        } else {
            receiveVideoFromParams["sdpAnswer"] = sessionDescription.description
        }
        val receiveVideoId = sendJson(
            JsonConstants.RECEIVEVIDEO_METHOD,
            receiveVideoFromParams
        )
        Log.d(TAG, "ID RECEIVE VIDEO: $receiveVideoId")
        IDS_RECEIVEVIDEO[receiveVideoId] = remoteConnectionId
    }

    private fun createRemoteParticipant(participant: JSONObject) {
        try {
            remoteConnectionId = participant.getString(JsonConstants.ID)
            var participantName = "-"
            val metaData = participant.optString(JsonConstants.METADATA, "")
            if (metaData.isNotEmpty()) {
                try {
                    val json = JSONObject(metaData)
                    val clientData = json.optString("clientData", "")
                    if (clientData.isNotEmpty()) {
                        participantName = clientData
                    }
                } catch (e: JSONException) {
                    Log.e(TAG, "createRemoteParticipant: ${e.message}")
                    participantName = "-"
                }
            }
            initRemmotePeerConnection(remoteConnectionId)
            callback?.onRemoteParticipantAlreadyInRoom(remoteConnectionId, participantName)
        } catch (e: java.lang.Exception) {
            Log.e(TAG, "FAILED CREATE REMOTE PEER CONNECTION: ${e.message}")
            callback?.onErrorMessage("FAILED CREATE REMOTE PEER CONNECTION: ${e.message ?: "-"}")
        }
    }

    private fun handleServerError(json: JSONObject) {

    }

    private fun handleServerMethod(json: JSONObject) {
        if (!json.has(JsonConstants.METHOD)) {
            Log.e(
                TAG, "Server event lacks a field '" + JsonConstants.METHOD + "'; JSON: "
                + json.toString()
            )
            return
        }
        val method = json.getString(JsonConstants.METHOD)

        if (!json.has(JsonConstants.PARAMS)) {
            Log.e(
                TAG, "Server event '" + method + "' lacks a field '" + JsonConstants.PARAMS
                + "'; JSON: " + json.toString()
            )
            return
        }
        val params = JSONObject(json.getString(JsonConstants.PARAMS))
        when (method) {
            JsonConstants.ICE_CANDIDATE -> {
                iceCandidateEvent(params)
            }
        }
    }

    private fun iceCandidateEvent(params: JSONObject) {
        val iceCandidate = IceCandidate(
            params.getString("sdpMid"),
            params.getInt("sdpMLineIndex"),
            params.getString("candidate")
        )
        val connectionId = params.getString("senderConnectionId")
        var isUnknown = true
        var isLocal: Boolean? = null
        var isRemote: Boolean? = null

        if (::localConnectionId.isInitialized) {
            isUnknown = false
            isLocal = connectionId == localConnectionId
            isRemote = !isLocal
        } else if (::remoteConnectionId.isInitialized) {
            isUnknown = false
            isRemote = connectionId == remoteConnectionId
            isLocal = !isRemote
        }


        val pc: PeerConnection =
            if (isRemote == true) remotePeerConnection else localPeerConnection
        when (pc.signalingState()) {
            PeerConnection.SignalingState.CLOSED -> Log.e(
                TAG,
                "iceCandidateEvent PeerConnection object is closed"
            )

            PeerConnection.SignalingState.STABLE -> {
                if (pc.remoteDescription != null) {
                    pc.addIceCandidate(iceCandidate)
                } else {
                    if (!isUnknown && isLocal == true) {
                        ICE_CANDIDATES_LOCAL.add(iceCandidate)
                    } else if (!isUnknown && isRemote == true) {
                        ICE_CANDIDATES_REMOTE.add(iceCandidate)
                    }
                }
            }

            else -> {
                ICE_CANDIDATES_LOCAL.add(iceCandidate)
            }
        }
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
        Log.d(
            TAG,
            "ON ICE CANDIDATE IS LOCAL: ${endpointName == localConnectionId} & END POINT NAME $endpointName"
        )
        val onIceCandidateParams: MutableMap<String, String?> = HashMap()
        if (endpointName != null) {
            onIceCandidateParams["endpointName"] = endpointName
        }
        onIceCandidateParams["candidate"] = iceCandidate.sdp
        onIceCandidateParams["sdpMid"] = iceCandidate.sdpMid
        onIceCandidateParams["sdpMLineIndex"] = iceCandidate.sdpMLineIndex.toString()
        Log.d(TAG, "ON ICE CANDIDATE PARAM: $onIceCandidateParams")
        val onIceCandidateId = sendJson(
            JsonConstants.ONICECANDIDATE_METHOD,
            onIceCandidateParams
        )
        this.IDS_ONICECANDIDATE.add(onIceCandidateId)
    }

    private fun joinRoom() {
        val joinRoomParams: MutableMap<String, String?> = HashMap()
        joinRoomParams[JsonConstants.METADATA] =
            "{\"clientData\": \"" + "MOBILE-CLIENT" + "\"}"
        joinRoomParams["secret"] = ""
        joinRoomParams["session"] = sessionId
        joinRoomParams["platform"] = "Android " + Build.VERSION.SDK_INT
        joinRoomParams["token"] = sessionToken
        joinRoomParams["sdkVersion"] = "2.29.0"
        val joinRoomId = sendJson(JsonConstants.JOINROOM_METHOD, joinRoomParams)
        Log.d(TAG, "ID JOIN ROOM: $joinRoomId")
        ID_JOINROOM.set(joinRoomId)
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
        val publishVideoId = sendJson(JsonConstants.PUBLISHVIDEO_METHOD, publishVideoParams)
        Log.d(TAG, "ID PUBLISH VIDEO: $publishVideoId")
        ID_PUBLISHVIDEO.set(publishVideoId)
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

    override fun onTextMessage(websocket: WebSocket?, data: ByteArray?) {

    }

    // ----- > BATAS < -----

    private val iceServersDefault =
        listOf<PeerConnection.IceServer>(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
        )
    private var iceServers: List<PeerConnection.IceServer> = listOf()

    fun initLocalPeerConnection(localConnectionId: String) {
        val config =
            BebasWebRTCUtility.createLocalPeerConnectionConfiguration(iceServersDefault)
        localPeerConnection = peerConnectionFactory.createPeerConnection(
            config,
            object : CustomPeerConnectionObserver() {
                override fun onIceCandidate(p0: IceCandidate?) {
                    super.onIceCandidate(p0)
                    Log.d(TAG, "LOCAL ON ICE CANDIDATE: ${p0?.sdp}")
                    p0?.let {
                        onIceCandidate(p0, localConnectionId)
                    }
                }

                override fun onSignalingChange(p0: PeerConnection.SignalingState?) {
                    super.onSignalingChange(p0)
                    if (p0 == PeerConnection.SignalingState.STABLE) {
                        Log.d(TAG, "LOCAL PEER CONNECTION STABLE")
                        Log.d(TAG, "LOCAL ICE CANDIDATE LENGTH: ${ICE_CANDIDATES_LOCAL.size}")
                    }
                }

            })!!

        try {
            localPeerConnection.addTransceiver(
                audioTrack,
                RtpTransceiver.RtpTransceiverInit(RtpTransceiver.RtpTransceiverDirection.SEND_ONLY)
            )

            localPeerConnection.addTransceiver(
                videoTrack,
                RtpTransceiver.RtpTransceiverInit(RtpTransceiver.RtpTransceiverDirection.SEND_ONLY)
            )
        }catch (t:Throwable){
            Log.e(TAG, "LOCAL ADD TRANSCEIVER ERROR: ${t.message}")
        }
    }

    fun initRemmotePeerConnection(remoteConnectionId: String) {
        val config =
            BebasWebRTCUtility.createRemotePeerConnectionConfiguration(iceServersDefault)
        remotePeerConnection = peerConnectionFactory.createPeerConnection(
            config,
            object : CustomPeerConnectionObserver() {
                override fun onIceCandidate(p0: IceCandidate?) {
                    super.onIceCandidate(p0)
                    p0?.let {
                        onIceCandidate(it, remoteConnectionId)
                    }
                }

                override fun onSignalingChange(p0: PeerConnection.SignalingState?) {
                    super.onSignalingChange(p0)
                    if (p0 == PeerConnection.SignalingState.STABLE) {
                        Log.d(TAG, "REMOTE PEER CONNECTION STABLE")
                        Log.d(TAG, "REMOTE ICE CANDIDATE LENGTH: ${ICE_CANDIDATES_REMOTE.size}")
                    }
                }

                override fun onAddTrack(
                    receiver: RtpReceiver?,
                    mediaStreams: Array<out MediaStream>?
                ) {
                    super.onAddTrack(receiver, mediaStreams)
                    Log.d(TAG, "REMOTE ON ADD TRACK MEDIA STREAM")
                    if (mediaStreams != null) {
                        callback?.onRemoteMediaStream(mediaStreams[0])
                    }
                }
            })!!

        try {
            remotePeerConnection.addTransceiver(
                MediaStreamTrack.MediaType.MEDIA_TYPE_AUDIO,
                RtpTransceiver.RtpTransceiverInit(RtpTransceiver.RtpTransceiverDirection.RECV_ONLY)
            )
            remotePeerConnection.addTransceiver(
                MediaStreamTrack.MediaType.MEDIA_TYPE_VIDEO,
                RtpTransceiver.RtpTransceiverInit(RtpTransceiver.RtpTransceiverDirection.RECV_ONLY)
            )
        }catch (e:Throwable){
            Log.e(TAG, "REMOTE ADD TRANSCEIVER: ${e.message}")
        }
    }

    fun startCamera(
        eglBaseContext: EglBase.Context,
        context: Context,
        localVideoView: SurfaceViewRenderer
    ) {
        this.eglBaseContext = eglBaseContext
        val audioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
        audioTrack = peerConnectionFactory.createAudioTrack("101", audioSource)

        val surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBaseContext)

        // Create VideoCapturer
        val videoCapturer = BebasWebRTCUtility.createCameraCapturer(context)
        if (videoCapturer != null) {
            val videoSource: VideoSource =
                peerConnectionFactory.createVideoSource(videoCapturer.isScreencast)
            videoCapturer.initialize(surfaceTextureHelper, context, videoSource.capturerObserver)
            videoCapturer.startCapture(480, 640, 30)
            videoTrack = peerConnectionFactory.createVideoTrack("100", videoSource)
            videoTrack.addSink(localVideoView)
            callback?.onLocalCameraStarted()
            Log.d("BebasLogger", "SUCCESS START CAMERA")
        } else {
            Log.d("BebasLogger", "CAMERA NULL")
        }
    }

    // ----- > BATAS < -----

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

    interface Callback {
        fun onErrorMessage(errorMessage: String)
        fun onRemoteParticipantAlreadyInRoom(connectionId: String, participantName: String)
        fun onRemoteMediaStream(mediaStream: MediaStream)
        fun onLocalCameraStarted()
    }

}