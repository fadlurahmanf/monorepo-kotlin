package com.fadlurahmanf.bebas_onboarding.external

import android.app.Activity
import org.webrtc.PeerConnectionFactory
import org.webrtc.SoftwareVideoDecoderFactory
import org.webrtc.SoftwareVideoEncoderFactory
import org.webrtc.VideoDecoderFactory
import org.webrtc.VideoEncoderFactory

class RTCSession(val sessionId: String, val sessionToken: String, activity: Activity) {

    private var peerConnectionFactory:PeerConnectionFactory

    init {
        val optionBuilder = PeerConnectionFactory.InitializationOptions.builder(activity.applicationContext)
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
}