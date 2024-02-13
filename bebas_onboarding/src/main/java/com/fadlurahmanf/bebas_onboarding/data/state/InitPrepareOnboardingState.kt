package com.fadlurahmanf.bebas_onboarding.data.state

import com.fadlurahmanf.bebas_shared.data.exception.BebasException

sealed class InitPrepareOnboardingState {
    object SuccessToEktpVerification : InitPrepareOnboardingState()
    object SuccessToEktpCamera : InitPrepareOnboardingState()
    data class FAILED(val exception: BebasException) : InitPrepareOnboardingState()
}
