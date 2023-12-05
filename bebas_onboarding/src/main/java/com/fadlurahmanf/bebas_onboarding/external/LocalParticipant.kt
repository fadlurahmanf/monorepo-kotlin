package com.fadlurahmanf.bebas_onboarding.external

import android.content.Context
import android.os.Build
import android.util.Log
import org.webrtc.Camera1Enumerator
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraEnumerator
import org.webrtc.EglBase
import org.webrtc.MediaConstraints
import org.webrtc.SurfaceTextureHelper
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoCapturer
import org.webrtc.VideoSource


class LocalParticipant(
    override val participantName: String,
    override val session: RTCSession,
    val context: Context,
    val localVideoView: SurfaceViewRenderer
) : Participant(participantName, session) {

    private lateinit var surfaceTextureHelper: SurfaceTextureHelper
    private lateinit var videoCapturer: VideoCapturer
    private lateinit var eglBaseContext: EglBase.Context

    fun startCamera(eglBaseContext: EglBase.Context) {
        this.eglBaseContext = eglBaseContext
        val peerConnectionFactory = session.peerConnectionFactory
        val audioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
        audioTrack = peerConnectionFactory.createAudioTrack("101", audioSource)

        surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBaseContext)

        // Create VideoCapturer
        val videoCapturer = createCameraCapturer()
        if (videoCapturer != null) {
            val videoSource: VideoSource =
                peerConnectionFactory.createVideoSource(videoCapturer.isScreencast)
            videoCapturer.initialize(surfaceTextureHelper, context, videoSource.capturerObserver)
            videoCapturer.startCapture(480, 640, 30)
            videoTrack = peerConnectionFactory.createVideoTrack("100", videoSource)
            videoTrack.addSink(localVideoView)
            Log.d("BebasLogger", "SUCCESS START CAMERA")
        } else {
            Log.d("BebasLogger", "CAMERA NULL")
        }
    }

    private fun createCameraCapturer(): VideoCapturer? {
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
                videoCapturer = enumerator.createCapturer(deviceName, null)
                Log.d("BebasLogger", "FRONT CAMERA FOUND")
                return videoCapturer
            }
        }

        // Front facing camera not found, try something else
        for (deviceName in deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                videoCapturer = enumerator.createCapturer(deviceName, null)
                Log.d("BebasLogger", "BACK CAMERA FOUND")
                return videoCapturer
            }
        }
        return null
    }
}