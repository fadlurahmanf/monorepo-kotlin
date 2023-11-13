package com.fadlurahmanf.bebas_onboarding.data.state

import com.fadlurahmanf.bebas_api.data.exception.BebasException

sealed class InitTncState {
    object SuccessToInputPhoneAndEmail : InitTncState()
    data class FAILED(val exception: BebasException) : InitTncState()
}
