package com.fadlurahmanf.bebas_onboarding.presentation.splash

sealed class SplashState<out T : Any> {
    object IDLE : SplashState<Nothing>()
    object LOADING : SplashState<Nothing>()
    object SUCCESS : SplashState<Nothing>()
    object FAILED : SplashState<Nothing>()
}
