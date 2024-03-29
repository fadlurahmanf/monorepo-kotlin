package com.fadlurahmanf.bebas_onboarding.presentation.vc

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.fadlurahmanf.bebas_api.data.dto.openvidu.ConnectionResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityDebugVideoCallBinding
import com.fadlurahmanf.bebas_onboarding.external.BebasWebSocket
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import org.webrtc.EglBase
import org.webrtc.MediaStream
import javax.inject.Inject

class DebugVideoCallActivity :
    BaseOnboardingActivity<ActivityDebugVideoCallBinding>(ActivityDebugVideoCallBinding::inflate) {

    @Inject
    lateinit var viewModel: VideoCallViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
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
                    showFailedBebasBottomsheet(it.exception)
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

    private fun getTokenSuccess(connection: ConnectionResponse) {
        if (connection.sessionId != null && connection.token != null) {
            startWebSocket(
                sessionId = connection.sessionId!!,
                sessionToken = connection.token!!,
            )
        } else {
            showFailedBebasBottomsheet(BebasException.generalRC("SESSION_ID_MISSING"))
        }
    }

    private fun initViews() {
        if (arePermissionGranted()) {
            binding.localGlSurfaceView.init(eglBaseContext, null)
            binding.localGlSurfaceView.setMirror(true)
            binding.localGlSurfaceView.setEnableHardwareScaler(true)
            binding.localGlSurfaceView.setZOrderMediaOverlay(true)
        } else {
            showFailedBebasBottomsheet(BebasException.generalRC("PERMISSION_MISSING"))
        }
    }

    private lateinit var webSocket: BebasWebSocket

    private fun startWebSocket(sessionId: String, sessionToken: String) {
        webSocket = BebasWebSocket(this.applicationContext, sessionId, sessionToken)
        webSocket.setCallback(bebasWebSocketCallback)
        webSocket.startCamera(eglBaseContext, this.applicationContext, binding.localGlSurfaceView)
    }

    private fun initAction() {
        binding.btnInitConnection.setOnClickListener {
            viewModel.initializeConnection("various-yellow-asp")
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

    private var bebasWebSocketCallback = object : BebasWebSocket.Callback {
        override fun onErrorMessage(errorMessage: String) {
            showSnackBarShort(binding.root, message = errorMessage)
        }

        override fun onRemoteParticipantAlreadyInRoom(
            connectionId: String,
            participantName: String
        ) {
            runOnUiThread {
                binding.remoteParticipantName.text = "$participantName - $connectionId"
                binding.remoteGlSurfaceView.init(eglBaseContext, null)
                binding.remoteGlSurfaceView.setMirror(false)
                binding.remoteGlSurfaceView.setEnableHardwareScaler(true)
                binding.remoteGlSurfaceView.setZOrderMediaOverlay(true)
            }
        }

        override fun onRemoteMediaStream(mediaStream: MediaStream) {
            val videoTrack = mediaStream.videoTracks[0]
            videoTrack.addSink(binding.remoteGlSurfaceView)
        }

        override fun onLocalCameraStarted() {
            showSnackBarShort(binding.root, message = "LOCAL CAMERA STARTED")
            webSocket.execute()
        }
    }
}