package com.fadlurahmanf.bebas_onboarding.external

import android.util.Log
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.PeerConnection

open class CustomPeerConnectionObserver : PeerConnection.Observer {
    private val TAG = "BebasLoggerCPC"
    override fun onSignalingChange(p0: PeerConnection.SignalingState?) {
        Log.d(TAG, "onSignalingChange: ${p0?.name}")
    }

    override fun onIceConnectionChange(p0: PeerConnection.IceConnectionState?) {
        Log.d(TAG, "onIceConnectionChange: ${p0?.name}")
    }

    override fun onIceConnectionReceivingChange(p0: Boolean) {
        Log.d(TAG, "onIceConnectionReceivingChange: $p0")
    }

    override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {
        Log.d(TAG, "onIceGatheringChange: ${p0?.name}")
    }

    override fun onIceCandidate(p0: IceCandidate?) {
        Log.d(TAG, "onIceGatheringChange: ${p0?.sdp}")
    }

    override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {
        Log.d(TAG, "onIceCandidatesRemoved")
    }

    override fun onAddStream(p0: MediaStream?) {
        Log.d(TAG, "onAddStream ${p0?.id}")
    }

    override fun onRemoveStream(p0: MediaStream?) {
        Log.d(TAG, "onRemoveStream ${p0?.id}")
    }

    override fun onDataChannel(p0: DataChannel?) {
        Log.d(TAG, "onDataChannel")
    }

    override fun onRenegotiationNeeded() {
        Log.d(TAG, "onRenegotiationNeeded")
    }
}