package com.fadlurahmanf.bebas_onboarding.presentation.splash

import com.fadlurahmanf.bebas_api.data.exception.BebasException

sealed class SplashState<out T : Any> {
    object IDLE : SplashState<Nothing>()
    object LOADING : SplashState<Nothing>()
    object SUCCESS : SplashState<Nothing>()
    data class FAILED(val exception: BebasException) : SplashState<Nothing>()
}
