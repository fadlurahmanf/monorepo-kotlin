package com.fadlurahmanf.bebas_onboarding.presentation.splash

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.exception.BebasException
import com.fadlurahmanf.bebas_onboarding.domain.repositories.OnboardingRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
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

        var errorGenerateCryptoKeyOrFetchTheExisting: Throwable? = null
        var isErrorGenerateCryptoKeyOrFetchTheExisting: Boolean? = null

        compositeDisposable().add(
            onboardingRepositoryImpl
                .generateCryptoKeyOrFetchTheExisting().subscribe(
                    {
                        isErrorGenerateCryptoKeyOrFetchTheExisting = false
                    },
                    {
                        errorGenerateCryptoKeyOrFetchTheExisting = it
                        isErrorGenerateCryptoKeyOrFetchTheExisting = true
                    },
                )
        )

        if (isErrorGenerateCryptoKeyOrFetchTheExisting == true && errorGenerateCryptoKeyOrFetchTheExisting != null) {
            _state.value = SplashState.FAILED(
                exception = BebasException.fromThrowable(
                    errorGenerateCryptoKeyOrFetchTheExisting ?: BebasException.generalRC("SPLASH_01")
                )
            )
            return
        }

        compositeDisposable().add(onboardingRepositoryImpl
                                      .generateGuestToken()
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              _state.value = SplashState.SUCCESS
                                          },
                                          {
                                              _state.value = SplashState.FAILED(
                                                  exception = BebasException.fromThrowable(it)
                                              )
                                          },
                                          {}
                                      ))
    }
}