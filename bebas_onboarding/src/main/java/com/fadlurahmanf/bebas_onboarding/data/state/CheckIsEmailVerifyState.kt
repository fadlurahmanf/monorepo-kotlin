package com.fadlurahmanf.bebas_onboarding.data.state

import com.fadlurahmanf.bebas_shared.data.exception.BebasException

sealed class CheckIsEmailVerifyState {
    data class IsVerified(val emailToken:String) : CheckIsEmailVerifyState()
    object IsNotVerified : CheckIsEmailVerifyState()
    data class FAILED(val exception: BebasException) : CheckIsEmailVerifyState()
}
