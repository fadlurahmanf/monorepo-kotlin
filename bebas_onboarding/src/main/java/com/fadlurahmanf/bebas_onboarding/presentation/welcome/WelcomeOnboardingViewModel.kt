package com.fadlurahmanf.bebas_onboarding.presentation.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.banner.WelcomeBannerResponse
import com.fadlurahmanf.bebas_api.data.exception.BebasException
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.domain.repositories.OnboardingRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class WelcomeOnboardingViewModel @Inject constructor(
    private val onboardingRepositoryImpl: OnboardingRepositoryImpl
) : BaseViewModel() {

    private val _lang = MutableLiveData<String>("id-ID")
    val lang: LiveData<String> = _lang


    fun getExistingLanguage() {
        compositeDisposable().add(onboardingRepositoryImpl.getLanguage().subscribe(
            {
                _lang.value = it
            },
            {}
        ))
    }

    fun switchLanguage() {
        if (_lang.value == "id-ID") {
            _lang.value = "en-EN"
            onboardingRepositoryImpl.switchLanguage("en-EN")
        } else {
            _lang.value = "id-ID"
            onboardingRepositoryImpl.switchLanguage("id-ID")
        }
    }

    private val _state = MutableLiveData<NetworkState<List<WelcomeBannerResponse>>>()
    val state: LiveData<NetworkState<List<WelcomeBannerResponse>>> = _state

    fun getWelcomeBanner() {
        _state.value = NetworkState.LOADING
        compositeDisposable().add(onboardingRepositoryImpl.getWelcomeBanner()
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              _state.value = NetworkState.SUCCESS(it)
                                          },
                                          {
                                              _state.value = NetworkState.FAILED(
                                                  BebasException.fromThrowable(it)
                                              )
                                          },
                                          {}
                                      ))
    }
}