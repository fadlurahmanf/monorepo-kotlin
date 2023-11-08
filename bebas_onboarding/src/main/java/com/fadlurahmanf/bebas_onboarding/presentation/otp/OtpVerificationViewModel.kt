package com.fadlurahmanf.bebas_onboarding.presentation.otp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.data.exception.BebasException
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.domain.repositories.OnboardingRepositoryImpl
import com.fadlurahmanf.bebas_shared.data.dto.OtpModel
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class OtpVerificationViewModel @Inject constructor(
    private val onboardingRepositoryImpl: OnboardingRepositoryImpl
) : BaseViewModel() {

    private val _otpTick = MutableLiveData<Long>()
    val otpTick: LiveData<Long> = _otpTick

    fun setOtpTick(value: Long) {
        _otpTick.value = value
    }

    private val _requestOtpState = MutableLiveData<NetworkState<OtpModel>>()
    val requestOtpState: LiveData<NetworkState<OtpModel>> = _requestOtpState

    fun requestOtp(phoneNumber: String) {
        _requestOtpState.value = NetworkState.LOADING
        compositeDisposable().add(onboardingRepositoryImpl.requestOtp(phoneNumber)
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              _requestOtpState.value = NetworkState.SUCCESS(it)
                                          },
                                          {
                                              _requestOtpState.value = NetworkState.FAILED(
                                                  BebasException.fromThrowable(it)
                                              )
                                          },
                                          {}
                                      ))
    }

    private val _verifyOtpState = MutableLiveData<NetworkState<Boolean>>()
    val verifyOtpState: LiveData<NetworkState<Boolean>> = _verifyOtpState
    fun verifyOtp(otp: String, phoneNumber: String) {
        _verifyOtpState.value = NetworkState.LOADING
        compositeDisposable().add(onboardingRepositoryImpl.verifyOtp(
            otp, phoneNumber
        ).subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              _verifyOtpState.value =
                                                  NetworkState.SUCCESS(data = true)
                                          },
                                          {
                                              _verifyOtpState.value = NetworkState.FAILED(
                                                  BebasException.fromThrowable(it)
                                              )
                                          },
                                          {}
                                      ))
    }
}