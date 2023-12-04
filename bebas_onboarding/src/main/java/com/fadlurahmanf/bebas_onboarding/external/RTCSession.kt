package com.fadlurahmanf.bebas_onboarding.external

import android.app.Activity
import org.webrtc.IceCandidate
import org.webrtc.PeerConnection
import org.webrtc.PeerConnection.IceServer
import org.webrtc.PeerConnectionFactory
import org.webrtc.SoftwareVideoDecoderFactory
import org.webrtc.SoftwareVideoEncoderFactory
import org.webrtc.VideoDecoderFactory
import org.webrtc.VideoEncoderFactory


class RTCSession(val sessionId: String, val sessionToken: String, activity: Activity) {

    var peerConnectionFactory: PeerConnectionFactory
    lateinit var customWebSocket: CustomWebSocket
    lateinit var localParticipant: LocalParticipant
    private val iceServersDefault =
        listOf<IceServer>(IceServer.builder("stun:stun.l.google.com:19302").createIceServer())
    private val iceServers: ArrayList<IceServer> = ArrayList<IceServer>()

    init {
        val optionBuilder =
            PeerConnectionFactory.InitializationOptions.builder(activity.applicationContext)
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


    fun setWebSocket(websocket: CustomWebSocket) {
        this.customWebSocket = websocket
    }

    fun createLocalPeerConnection(): PeerConnection {
        val config =
            PeerConnection.RTCConfiguration(if (iceServers.isEmpty()) iceServersDefault else iceServers)
        config.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.ENABLED
        config.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE
        config.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.NEGOTIATE
        config.continualGatheringPolicy =
            PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY
        config.keyType = PeerConnection.KeyType.ECDSA
        config.sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN

        val peerConnection = peerConnectionFactory.createPeerConnection(config, object :
            CustomPeerConnectionObserver() {
            override fun onIceCandidate(p0: IceCandidate?) {
                super.onIceCandidate(p0)
            }

            override fun onSignalingChange(p0: PeerConnection.SignalingState?) {
                super.onSignalingChange(p0)
                if (PeerConnection.SignalingState.STABLE == p0) {
                    // SDP Offer/Answer finished. Add stored remote candidates.
                    val it: List<IceCandidate> =
                        localParticipant.getIceCandidateList()
                    // TODO: BEDA DARI ASLI
                    for (element in it){
                        localParticipant.getPeerConnection()?.addIceCandidate(element)
                    }
                }
            }
        })
        return peerConnection!!
    }
}