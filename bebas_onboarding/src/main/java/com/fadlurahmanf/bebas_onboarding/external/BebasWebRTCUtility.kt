package com.fadlurahmanf.bebas_onboarding.external

import android.content.Context
import android.os.Build
import android.util.Log
import org.webrtc.Camera1Enumerator
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraEnumerator
import org.webrtc.IceCandidate
import org.webrtc.PeerConnection
import org.webrtc.VideoCapturer

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

        fun createCameraCapturer(context: Context): VideoCapturer? {
            val enumerator: CameraEnumerator =
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    Camera2Enumerator(context)
                } else {
                    Camera1Enumerator(false)
                }
            val deviceNames = enumerator.deviceNames

            // Try to find front facing camera
            for (deviceName in deviceNames) {
                if (enumerator.isFrontFacing(deviceName)) {
                    Log.d("BebasLogger", "FRONT CAMERA FOUND")
                    return enumerator.createCapturer(deviceName, null)
                }
            }

            // Front facing camera not found, try something else
            for (deviceName in deviceNames) {
                if (!enumerator.isFrontFacing(deviceName)) {
                    Log.d("BebasLogger", "BACK CAMERA FOUND")
                    return enumerator.createCapturer(deviceName, null)
                }
            }
            return null
        }
    }
}