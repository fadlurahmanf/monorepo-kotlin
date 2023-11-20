package com.fadlurahmanf.bebas_onboarding.data.state

import com.fadlurahmanf.bebas_shared.data.exception.BebasException

sealed class CheckIsEmailVerifyState {
    object IsVerified : CheckIsEmailVerifyState()
    object IsNotVerified : CheckIsEmailVerifyState()
    data class FAILED(val exception: BebasException) : CheckIsEmailVerifyState()
}
