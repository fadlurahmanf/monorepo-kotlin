package com.fadlurahmanf.bebas_onboarding.presentation.vc

import com.fadlurahmanf.bebas_api.data.dto.openvidu.ConnectionResponse
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityVideoCallBinding
import com.fadlurahmanf.bebas_onboarding.external.CustomWebSocket
import com.fadlurahmanf.bebas_onboarding.external.RTCSession
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import javax.inject.Inject

class VideoCallActivity :
    BaseOnboardingActivity<ActivityVideoCallBinding>(ActivityVideoCallBinding::inflate) {

    @Inject
    lateinit var viewModel: VideoCallViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
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

    private fun getTokenSuccess(connection: ConnectionResponse) {
        if (connection.sessionId != null && connection.token != null) {
            rtcSession = RTCSession(connection.sessionId!!, connection.token!!, this)
            startWebSocket()
        } else {
            showFailedBottomsheet(BebasException.generalRC("SESSION_ID"))
        }
    }

    private fun startWebSocket() {
        val webSocket = CustomWebSocket(rtcSession)
        webSocket.execute()
    }

    private fun initAction() {
        binding.btnInitConnection.setOnClickListener {
            viewModel.initializeConnection("toxic-plum-porcupine")
        }
    }

}