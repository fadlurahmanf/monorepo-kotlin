package com.fadlurahmanf.bebas_onboarding.presentation.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.dto.tnc.TncResponse
import com.fadlurahmanf.bebas_api.data.exception.BebasException
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.data.state.InitTncState
import com.fadlurahmanf.bebas_onboarding.domain.repositories.OnboardingRepositoryImpl
import com.fadlurahmanf.bebas_storage.domain.datasource.BebasLocalDatasource
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class TncViewModel @Inject constructor(
    private val onboardingRepositoryImpl: OnboardingRepositoryImpl,
    private val bebasLocalDatasource: BebasLocalDatasource,
) : BaseViewModel() {

    private val _isTncRead = MutableLiveData<Boolean>(false)
    val isTncRead: LiveData<Boolean> = _isTncRead

    fun switchIsTncRead(isChecked: Boolean) {
        _isTncRead.value = isChecked
    }

    private val _tncContentState = MutableLiveData<NetworkState<TncResponse>>()
    val tncContentState: LiveData<NetworkState<TncResponse>> = _tncContentState

    fun getTNC() {
        _tncContentState.value = NetworkState.LOADING
        compositeDisposable().add(onboardingRepositoryImpl.getTNC()
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              _tncContentState.value =
                                                  NetworkState.SUCCESS(data = it)
                                          },
                                          {
                                              _tncContentState.value = NetworkState.FAILED(
                                                  exception = BebasException.fromThrowable(it)
                                              )
                                          },
                                          {}
                                      ))
    }

    private val _initState = MutableLiveData<InitTncState>()
    val initState: LiveData<InitTncState> = _initState

    fun initState() {
        compositeDisposable().add(bebasLocalDatasource.getEntity().subscribe(
            {
                if (it.isFinishedReadTnc == true) {
                    _isTncRead.value = true
                    _initState.value = InitTncState.SuccessToInputPhoneAndEmail
                }
            },
            {
                _initState.value = InitTncState.FAILED(BebasException.fromThrowable(it))
            }
        ))
    }

    fun updateIsFinishedReadTnc(value: Boolean) {
        compositeDisposable().add(bebasLocalDatasource.updateIsFinishedReadTNC(value))
    }

    fun removeOnboardingFlow() {
        compositeDisposable().add(bebasLocalDatasource.removeOnboardingFlow())
    }
}