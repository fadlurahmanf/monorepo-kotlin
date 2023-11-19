package com.fadlurahmanf.bebas_onboarding.data.state

import com.fadlurahmanf.bebas_shared.data.exception.BebasException

sealed class InitInputPhoneAndEmailState {
    data class SuccessToOtp(val phone: String, val email: String) : InitInputPhoneAndEmailState()
    data class SuccessToEmail(val phone: String, val email: String) : InitInputPhoneAndEmailState()
    data class SuccessLoadData(var phone: String?, val email: String?) : InitInputPhoneAndEmailState()
    data class FAILED(val exception: BebasException) : InitInputPhoneAndEmailState()
}
