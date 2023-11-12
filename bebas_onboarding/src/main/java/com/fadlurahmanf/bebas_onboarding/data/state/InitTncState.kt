package com.fadlurahmanf.bebas_onboarding.data.state

import com.fadlurahmanf.bebas_api.data.exception.BebasException
import com.fadlurahmanf.bebas_storage.data.entity.BebasEntity

sealed class InitTncState {
    object SuccessToOtp : InitTncState()
    data class FAILED(val exception: BebasException) : InitTncState()
}
