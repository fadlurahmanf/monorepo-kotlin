package com.fadlurahmanf.bebas_onboarding.presentation.vc

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_api.data.dto.openvidu.ConnectionResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityVideoCallBinding
import com.fadlurahmanf.bebas_onboarding.external.CustomWebSocket
import com.fadlurahmanf.bebas_onboarding.external.LocalParticipant
import com.fadlurahmanf.bebas_onboarding.external.RTCSession
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import org.webrtc.Camera1Enumerator
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraEnumerator
import org.webrtc.EglBase
import org.webrtc.MediaConstraints
import org.webrtc.SurfaceTextureHelper
import org.webrtc.VideoCapturer
import org.webrtc.VideoSource
import javax.inject.Inject


class VideoCallActivity :
    BaseOnboardingActivity<ActivityVideoCallBinding>(ActivityVideoCallBinding::inflate) {

    @Inject
    lateinit var viewModel: VideoCallViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        initViews()

        initObserver()
        initAction()
    }

    private fun initObserver() {
        viewModel.initConnectionState.observe(this) {
            when (it) {
                is NetworkState.LOADING -> {
                    showLoadingDialog()
                }

                is NetworkState.FAILED -> {
                    dismissLoadingDialog()
                    showFailedBottomsheet(it.exception)
                }

                is NetworkState.IDLE -> {

                }

                is NetworkState.SUCCESS -> {
                    dismissLoadingDialog()
                    getTokenSuccess(it.data)
                }
            }
        }
    }

    lateinit var rtcSession: RTCSession
    lateinit var localParticipant: LocalParticipant

    private fun getTokenSuccess(connection: ConnectionResponse) {
        if (connection.sessionId != null && connection.token != null) {
            rtcSession = RTCSession(connection.sessionId!!, connection.token!!, this)
            localParticipant = LocalParticipant(
                participantName = "participantName",
                session = rtcSession,
                context = this.applicationContext,
                localVideoView = binding.localGlSurfaceView
            )
            localParticipant.startCamera(eglBaseContext)
            startWebSocket()
        } else {
            showFailedBottomsheet(BebasException.generalRC("SESSION_ID"))
        }
    }

    private fun initViews() {
        if (arePermissionGranted()) {
            binding.localGlSurfaceView.init(eglBaseContext, null)
            binding.localGlSurfaceView.setMirror(true)
            binding.localGlSurfaceView.setEnableHardwareScaler(true)
            binding.localGlSurfaceView.setZOrderMediaOverlay(true)
        } else {
            showFailedBottomsheet(BebasException.generalRC("PERMISSION_MISSING"))
        }
    }

    private fun startWebSocket() {
        val webSocket = CustomWebSocket(rtcSession)
        webSocket.execute()
        rtcSession.setWebSocket(webSocket)
    }

    private fun initAction() {
        binding.btnInitConnection.setOnClickListener {
            viewModel.initializeConnection("toxic-plum-porcupine")
        }
    }

    private fun arePermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) != PackageManager.PERMISSION_DENIED && ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) != PackageManager.PERMISSION_DENIED
    }

    private var eglBaseContext = EglBase.create().eglBaseContext
}