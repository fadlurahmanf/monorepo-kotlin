package com.fadlurahmanf.bebas_onboarding.presentation.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.banner.WelcomeBannerResponse
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.data.state.InitWelcomeState
import com.fadlurahmanf.bebas_onboarding.domain.repositories.OnboardingRepositoryImpl
import com.fadlurahmanf.bebas_shared.data.dto.BebasAppLanguage
import com.fadlurahmanf.bebas_shared.data.flow.OnboardingFlow
import com.fadlurahmanf.bebas_storage.domain.datasource.BebasLocalDatasource
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class WelcomeOnboardingViewModel @Inject constructor(
    private val onboardingRepositoryImpl: OnboardingRepositoryImpl,
    private val bebasLocalDatasource: BebasLocalDatasource
) : BaseViewModel() {

    private val _initState = MutableLiveData<InitWelcomeState>()
    val initState: LiveData<InitWelcomeState> = _initState

    fun initLastStorage() {
        compositeDisposable().add(bebasLocalDatasource.getEntity().subscribe(
            {
                if (it.onboardingFlow != null) {
                    _initState.value = InitWelcomeState.SuccessToTnc
                }
            },
            {
                _initState.value = InitWelcomeState.FAILED(BebasException.fromThrowable(it))
            }
        ))
    }

    private val _lang = MutableLiveData<BebasAppLanguage>()
    val lang: LiveData<BebasAppLanguage> = _lang


    fun getExistingLanguage() {
        compositeDisposable().add(onboardingRepositoryImpl.getLanguage().subscribe(
            {
                _lang.value = it
            },
            {}
        ))
    }

    fun switchLanguage() {
        if (_lang.value?.entityLanguage == "in-ID") {
            compositeDisposable().add(bebasLocalDatasource.updateLanguage("en-US").subscribe(
                {
                    _lang.value = BebasAppLanguage(
                        entityLanguage = "en-US",
                        deviceLocaleLanguage = _lang.value?.deviceLocaleLanguage ?: "en-US"
                    )
                },
                {}
            ))
        } else {
            compositeDisposable().add(bebasLocalDatasource.updateLanguage("in-ID").subscribe(
                {
                    _lang.value = BebasAppLanguage(
                        entityLanguage = "in-ID",
                        deviceLocaleLanguage = _lang.value?.deviceLocaleLanguage ?: "in-ID"
                    )
                },
                {}
            ))
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

    fun updateOobFlow(flow: OnboardingFlow) {
        compositeDisposable().add(bebasLocalDatasource.updateFlowOnboarding(flow))
    }
}