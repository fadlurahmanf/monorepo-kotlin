package com.fadlurahmanf.bebas_onboarding.external

import android.util.Log
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription

open class CustomSdpObserver : SdpObserver {
    override fun onCreateSuccess(p0: SessionDescription?) {
        Log.d("BebasLoggerRtc", "onCreateSuccess")
    }

    override fun onSetSuccess() {
        Log.d("BebasLoggerRtc", "onSetSuccess")
    }

    override fun onCreateFailure(p0: String?) {
        Log.d("BebasLoggerRtc", "onCreateFailure: $p0")
    }

    override fun onSetFailure(p0: String?) {
        Log.d("BebasLoggerRtc", "onSetFailure: $p0")
    }
}