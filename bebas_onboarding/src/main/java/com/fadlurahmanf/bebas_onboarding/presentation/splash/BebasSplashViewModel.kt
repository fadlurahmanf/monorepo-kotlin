package com.fadlurahmanf.bebas_onboarding.presentation.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_onboarding.domain.repositories.OnboardingRepositoryImpl
import com.fadlurahmanf.bebas_ui.presentation.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class BebasSplashViewModel @Inject constructor(
    private val onboardingRepositoryImpl: OnboardingRepositoryImpl,
) : BaseViewModel() {

    private val _state = MutableLiveData<SplashState<Boolean>>()
    val state: LiveData<SplashState<Boolean>> = _state

    fun generateGuestToken() {
        _state.value = SplashState.LOADING

        compositeDisposable().add(
            onboardingRepositoryImpl
                .generateCryptoKeyOrFetchTheExisting().subscribe(
                    {
                        Log.d("BebasLoggerDev", "RESULT: $it")
                    },
                    {
                        Log.d("BebasLoggerDev", "THROW: $it")
                    },
                )
        )

        compositeDisposable().add(onboardingRepositoryImpl
                                      .generateGuestToken()
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              _state.value = SplashState.SUCCESS
                                          },
                                          {
                                              _state.value = SplashState.FAILED
                                          },
                                          {}
                                      ))
    }
}