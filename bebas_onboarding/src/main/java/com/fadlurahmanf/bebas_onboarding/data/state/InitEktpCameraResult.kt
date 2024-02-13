package com.fadlurahmanf.bebas_onboarding.data.state

import com.fadlurahmanf.bebas_shared.data.exception.BebasException

sealed class InitEktpCameraResult {
    data class SuccessLoadData(val base64Image: String) : InitEktpCameraResult()

    data class FAILED(val exception: BebasException) : InitEktpCameraResult()
}
