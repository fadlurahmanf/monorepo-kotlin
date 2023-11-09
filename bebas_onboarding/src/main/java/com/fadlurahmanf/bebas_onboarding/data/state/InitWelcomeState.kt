package com.fadlurahmanf.bebas_onboarding.data.state

import com.fadlurahmanf.bebas_api.data.exception.BebasException
import com.fadlurahmanf.bebas_storage.data.entity.BebasEntity

sealed class InitWelcomeState {
    object SuccessToTnc : InitWelcomeState()
    data class FAILED(val exception: BebasException) : InitWelcomeState()
}
