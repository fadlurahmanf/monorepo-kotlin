package com.fadlurahmanf.bebas_onboarding.presentation.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.tnc.TncResponse
import com.fadlurahmanf.bebas_api.data.exception.BebasException
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.domain.repositories.OnboardingRepositoryImpl
import com.fadlurahmanf.bebas_ui.presentation.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class TncViewModel @Inject constructor(
    private val onboardingRepositoryImpl: OnboardingRepositoryImpl
) : BaseViewModel() {

    private val _state = MutableLiveData<NetworkState<TncResponse>>()
    val state: LiveData<NetworkState<TncResponse>> = _state

    fun getTNC() {
        _state.value = NetworkState.LOADING
        compositeDisposable().add(onboardingRepositoryImpl.getTNC()
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              _state.value = NetworkState.SUCCESS(data = it)
                                          },
                                          {
                                              _state.value = NetworkState.FAILED(
                                                  exception = BebasException.fromThrowable(it)
                                              )
                                          },
                                          {}
                                      ))
    }
}