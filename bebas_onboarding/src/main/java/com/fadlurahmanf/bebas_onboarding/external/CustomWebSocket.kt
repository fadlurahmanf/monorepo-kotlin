package com.fadlurahmanf.bebas_onboarding.external

import android.os.AsyncTask
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.fadlurahmanf.bebas_onboarding.presentation.vc.VideoCallActivity
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
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.atomic.AtomicInteger
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager


class CustomWebSocket(private val session: RTCSession) : AsyncTask<VideoCallActivity, Void, Void>(),
    WebSocketListener {

    private val TAG = "BebasLoggerRTC"
    private val ID_JOINROOM = AtomicInteger(-1)
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

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: VideoCallActivity?): Void? {
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
        val wsuri = "wss://${BebasShared.getOpenviduHost()}:443/openvidu?sessionId=${session.sessionId}&token=${session.sessionToken}"
        Log.d(TAG, "WSURI WEBSOCKET: ${wsuri}")
        return wsuri
    }

    override fun onTextMessage(websocket: WebSocket?, text: String?) {
        Log.d(TAG, "BEBAS ON TEXT MESSAGE: $text")
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

    fun joinRoom() {
        val joinRoomParams: MutableMap<String, String?> = HashMap()
        joinRoomParams[JsonConstants.METADATA] =
            "{\"clientData\": \"" + session.localParticipant.participantName + "\"}"
        joinRoomParams["secret"] = ""
        joinRoomParams["session"] = session.sessionId
        joinRoomParams["platform"] = "Android " + Build.VERSION.SDK_INT
        joinRoomParams["token"] = session.sessionToken
        joinRoomParams["sdkVersion"] = "2.29.0"
        this.ID_JOINROOM.set(sendJson(JsonConstants.JOINROOM_METHOD, joinRoomParams))
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

    override fun onTextMessage(websocket: WebSocket?, data: ByteArray?) {

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