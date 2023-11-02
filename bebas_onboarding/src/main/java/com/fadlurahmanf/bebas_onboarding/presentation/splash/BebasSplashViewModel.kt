package com.fadlurahmanf.bebas_onboarding.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_onboarding.domain.repositories.OnboardingRepositoryImpl
import com.fadlurahmanf.bebas_ui.presentation.viewmodel.BaseViewModel
import javax.inject.Inject

class BebasSplashViewModel @Inject constructor(
    private val onboardingRepositoryImpl: OnboardingRepositoryImpl
) : BaseViewModel() {

    private val _state = MutableLiveData<SplashState<Boolean>>()
    val state: LiveData<SplashState<Boolean>> = _state

    fun generateGuestToken() {
        _state.value = SplashState.LOADING
    }
}