package com.fadlurahmanf.bebas_onboarding.external

import org.webrtc.IceCandidate
import org.webrtc.PeerConnection

class BebasWebRTCUtility {
    companion object {
        fun createLocalPeerConnectionConfiguration(iceServers: List<PeerConnection.IceServer>): PeerConnection.RTCConfiguration {
            val config =
                PeerConnection.RTCConfiguration(iceServers)
            config.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.ENABLED
            config.bundlePolicy = PeerConnection.BundlePolicy.BALANCED
            config.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.NEGOTIATE
            config.continualGatheringPolicy =
                PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY
            config.keyType = PeerConnection.KeyType.ECDSA
            config.sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
            return config
        }

        fun createRemotePeerConnectionConfiguration(iceServers: List<PeerConnection.IceServer>): PeerConnection.RTCConfiguration {
            val config =
                PeerConnection.RTCConfiguration(iceServers)
            config.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.ENABLED
            config.bundlePolicy = PeerConnection.BundlePolicy.BALANCED
            config.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.NEGOTIATE
            config.continualGatheringPolicy =
                PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY
            config.keyType = PeerConnection.KeyType.ECDSA
            config.sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
            return config
        }
    }
}