package com.fadlurahmanf.bebas_onboarding.external

import android.util.Log
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

open class CustomSdpObserver(val additionalTag: String) : SdpObserver {
    override fun onCreateSuccess(p0: SessionDescription?) {
        Log.d("BebasLoggerRTC", "$additionalTag onCreateSuccess")
    }

    override fun onSetSuccess() {
        Log.d("BebasLoggerRTC", "$additionalTag onSetSuccess")
    }

    override fun onCreateFailure(p0: String?) {
        Log.d("BebasLoggerRTC", "$additionalTag onCreateFailure: $p0")
    }

    override fun onSetFailure(p0: String?) {
        Log.d("BebasLoggerRTC", "$additionalTag onSetFailure: $p0")
    }
}