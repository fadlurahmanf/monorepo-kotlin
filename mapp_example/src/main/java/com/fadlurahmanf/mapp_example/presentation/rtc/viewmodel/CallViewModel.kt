package com.fadlurahmanf.mapp_example.presentation.rtc.viewmodel

import android.content.Context
import org.webrtc.AudioSource
import org.webrtc.AudioTrack
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraVideoCapturer
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.PeerConnection.Observer
import org.webrtc.PeerConnectionFactory
import org.webrtc.SurfaceTextureHelper
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoCapturer
import org.webrtc.VideoSource
import org.webrtc.VideoTrack
import javax.inject.Inject

class CallViewModel {

    companion object {
        private const val LOCAL_TRACK_ID = "local_track"
        private const val LOCAL_STREAM_ID = "local_track"
    }

    private val rootEglBase: EglBase = EglBase.create()

    private var localAudioTrack: AudioTrack? = null
    private var localVideoTrack: VideoTrack? = null

    fun initSurfaceView(view: SurfaceViewRenderer) {
        view.run {
            setMirror(true)
            setEnableHardwareScaler(true)
            init(rootEglBase.eglBaseContext, null)
        }
    }

    fun startLocalVideoCapture(localVideoOutput: SurfaceViewRenderer) {
        val surfaceTextureHelper =
            SurfaceTextureHelper.create(Thread.currentThread().name, rootEglBase.eglBaseContext)
        videoCapturer.initialize(
            surfaceTextureHelper,
            localVideoOutput.context,
            getLocalVideoSources().capturerObserver
        )
        videoCapturer.startCapture(320, 240, 60)
        localAudioTrack =
            peerConnectionFactory2.createAudioTrack(LOCAL_TRACK_ID + "_audio", audioSource)
        localVideoTrack = peerConnectionFactory2.createVideoTrack(LOCAL_TRACK_ID, videoSource)
        localVideoTrack?.addSink(localVideoOutput)
        val localStream = peerConnectionFactory2.createLocalMediaStream(LOCAL_STREAM_ID)
        localStream.addTrack(localVideoTrack)
        localStream.addTrack(localAudioTrack)
        peerConnection?.addStream(localStream)
    }

    private lateinit var context: Context
    private lateinit var observer: PeerConnection.Observer

    fun setContext(context: Context, observer: Observer) {
        this.context = context
    }

    private val audioSource: AudioSource by lazy {
        peerConnectionFactory2.createAudioSource(MediaConstraints())
    }

    private val videoSource: VideoSource by lazy {
        peerConnectionFactory2.createVideoSource(false)
    }

    private val peerConnection by lazy { buildPeerConnection(observer) }

    private val iceServer = listOf(
        PeerConnection.IceServer.builder("stun:stun.l.google.com:19302")
            .createIceServer()
    )

    private fun buildPeerConnection(observer: PeerConnection.Observer) =
        peerConnectionFactory2.createPeerConnection(
            iceServer,
            observer
        )

    private val videoCapturer: CameraVideoCapturer by lazy {
        getVideoCapturer(context)
    }

    private fun getVideoCapturer(context: Context): CameraVideoCapturer {
        return Camera2Enumerator(context).run {
            deviceNames.find {
                isFrontFacing(it)
            }?.let {
                createCapturer(it, null)
            } ?: throw IllegalStateException()
        }
    }


    private val localVideoSource: VideoSource by lazy {
        getLocalVideoSources()
    }

    private fun getLocalVideoSources(): VideoSource {
        return peerConnectionFactory2.createVideoSource(false)
    }

//    private lateinit var peerConnectionFactory:PeerConnectionFactory
    private val peerConnectionFactory2 by lazy {
        getPeerConnectionFactory()
    }

    private fun getPeerConnectionFactory(): PeerConnectionFactory {
        return PeerConnectionFactory
            .builder()
            .setVideoDecoderFactory(DefaultVideoDecoderFactory(rootEglBase.eglBaseContext))
            .setVideoEncoderFactory(
                DefaultVideoEncoderFactory(
                    rootEglBase.eglBaseContext,
                    true,
                    true
                )
            )
            .setOptions(PeerConnectionFactory.Options().apply {
                disableEncryption = true
                disableNetworkMonitor = true
            })
            .createPeerConnectionFactory()
    }
}