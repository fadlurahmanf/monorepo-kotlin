package com.fadlurahmanf.bebas_onboarding.external

import android.util.Log
import com.fadlurahmanf.bebas_onboarding.presentation.vc.DebugVideoCallActivity
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
import org.webrtc.VideoDecoderFactory
import org.webrtc.VideoEncoderFactory


class RTCSession(
    val sessionId: String,
    val sessionToken: String,
    val activity: DebugVideoCallActivity
) {

    var peerConnectionFactory: PeerConnectionFactory
    lateinit var bebasWebSocket: BebasWebSocket
    private val remoteParticipants: Map<String, RemoteParticipant> = HashMap()
    lateinit var localParticipant: LocalParticipant
    private val iceServersDefault =
        listOf<IceServer>(IceServer.builder("stun:stun.l.google.com:19302").createIceServer())
    private var iceServers: List<IceServer> = listOf()

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


    fun setWebSocket(websocket: BebasWebSocket) {
        this.bebasWebSocket = websocket
    }

    fun createLocalPeerConnection(): PeerConnection {
        val config =
            PeerConnection.RTCConfiguration(iceServers.ifEmpty { iceServersDefault })
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
                    for (element in it) {
                        localParticipant.getPeerConnection()?.addIceCandidate(element)
                    }
                }
            }
        })
        return peerConnection!!
    }

    fun setIceServers(iceServers: List<IceServer>) {
        this.iceServers = iceServers
    }

    fun createOfferForPublishing(constraints: MediaConstraints) {
        Log.d("BebasLoggerRTC", "CREATE OFFER: $constraints")
        localParticipant.getPeerConnection()!!
            .createOffer(object : CustomSdpObserver("local create offer") {
                override fun onCreateSuccess(p0: SessionDescription?) {
                    super.onCreateSuccess(p0)
                    Log.d("BebasLoggerRTC", "SDP TYPE: ${p0?.type}")
                    Log.d("BebasLoggerRTC", "SDP OFFER: ${p0?.description}")
                    localParticipant.getPeerConnection()!!.setLocalDescription(object :
                                                                                   CustomSdpObserver(
                                                                                       "local set description"
                                                                                   ) {
                        override fun onSetSuccess() {
                            super.onSetSuccess()
                            if (p0 != null) {
                                bebasWebSocket.publishVideo(p0)
                            }
                        }
                    }, p0)
                }
            }, constraints)
    }

    fun createRemotePeerConnection(connectionId: String?) {
        val config =
            PeerConnection.RTCConfiguration(if (iceServers.isEmpty()) iceServersDefault else iceServers)
        config.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.ENABLED
        config.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE
        config.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.NEGOTIATE
        config.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY
        config.keyType = PeerConnection.KeyType.ECDSA
        config.sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
        val peerConnection = peerConnectionFactory.createPeerConnection(
            config,
            object : CustomPeerConnectionObserver() {
                override fun onIceCandidate(iceCandidate: IceCandidate?) {
                    super.onIceCandidate(iceCandidate)
                    if (iceCandidate != null) {
                        bebasWebSocket.onIceCandidate(iceCandidate, connectionId)
                    }
                }

                override fun onAddTrack(
                    rtpReceiver: RtpReceiver,
                    mediaStreams: Array<MediaStream>
                ) {
                    super.onAddTrack(rtpReceiver, mediaStreams)
                    remoteParticipants[connectionId]?.let {
                        activity.setRemoteMediaStream(
                            mediaStreams[0],
                            it
                        )
                    }
                }

                override fun onSignalingChange(signalingState: PeerConnection.SignalingState?) {
                    if (PeerConnection.SignalingState.STABLE == signalingState) {
                        // SDP Offer/Answer finished. Add stored remote candidates.
                        val remoteParticipant: RemoteParticipant? =
                            remoteParticipants[connectionId]
                        if (remoteParticipant != null) {
                            val it = remoteParticipant.getIceCandidateList().toList()
                            for (element in it) {
                                remoteParticipant.getPeerConnection()!!.addIceCandidate(element)
                            }
                        }
                    }
                }
            })
        peerConnection!!.addTransceiver(
            MediaStreamTrack.MediaType.MEDIA_TYPE_AUDIO,
            RtpTransceiver.RtpTransceiverInit(RtpTransceiver.RtpTransceiverDirection.RECV_ONLY)
        )
        peerConnection.addTransceiver(
            MediaStreamTrack.MediaType.MEDIA_TYPE_VIDEO,
            RtpTransceiver.RtpTransceiverInit(RtpTransceiver.RtpTransceiverDirection.RECV_ONLY)
        )
        remoteParticipants[connectionId]?.setPeerConnection(peerConnection)
    }
}