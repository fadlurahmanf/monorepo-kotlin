package com.fadlurahmanf.bebas_onboarding.presentation.email

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.data.state.CheckIsEmailVerifyState
import com.fadlurahmanf.bebas_onboarding.domain.repositories.OnboardingRepositoryImpl
import com.fadlurahmanf.bebas_shared.data.dto.EmailModel
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class EmailVerificationViewModel @Inject constructor(
    private val onboardingRepositoryImpl: OnboardingRepositoryImpl
) : BaseViewModel() {

    private val _requestEmailState = MutableLiveData<NetworkState<EmailModel>>()
    val requestEmailState: LiveData<NetworkState<EmailModel>> = _requestEmailState

    fun requestEmail(email: String) {
        _requestEmailState.value = NetworkState.LOADING
        compositeDisposable().add(onboardingRepositoryImpl.requestEmail(email)
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              _requestEmailState.value = NetworkState.SUCCESS(it)
                                          },
                                          {
                                              _requestEmailState.value = NetworkState.FAILED(
                                                  BebasException.fromThrowable(it)
                                              )
                                          },
                                          {}
                                      ))
    }

    private val _checkEmailVerifyState = MutableLiveData<CheckIsEmailVerifyState>()
    val checkEmailVerifyState: LiveData<CheckIsEmailVerifyState> = _checkEmailVerifyState

    fun checkIsEmailVerify(email: String) {
        compositeDisposable().add(onboardingRepositoryImpl.checkIsEmailVerify(email)
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              if (it.isVerify == true) {
                                                  _checkEmailVerifyState.value =
                                                      CheckIsEmailVerifyState.IsVerified(it.emailToken!!)
                                              } else {
                                                  _checkEmailVerifyState.value =
                                                      CheckIsEmailVerifyState.IsNotVerified
                                              }
                                          },
                                          {
                                              _checkEmailVerifyState.value =
                                                  CheckIsEmailVerifyState.FAILED(
                                                      BebasException.fromThrowable(it)
                                                  )
                                          },
                                          {}
                                      ))
    }
}