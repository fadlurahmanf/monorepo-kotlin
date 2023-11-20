package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_onboarding.data.state.InitInputPhoneAndEmailState
import com.fadlurahmanf.bebas_shared.data.flow.OnboardingFlow
import com.fadlurahmanf.bebas_storage.domain.datasource.BebasLocalDatasource
import com.fadlurahmanf.bebas_shared.state.EditTextFormState
import com.fadlurahmanf.bebas_shared.validator.EmailValidator
import com.fadlurahmanf.bebas_shared.validator.PhoneValidator
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import javax.inject.Inject

class InputPhoneEmailViewModel @Inject constructor(
    private val bebasLocalDatasource: BebasLocalDatasource
) : BaseViewModel() {

    private val _state = MutableLiveData<Boolean>()
    val state: LiveData<Boolean> = _state

    var processFormThroughButton: Boolean = false

    private val _phoneState = MutableLiveData<EditTextFormState<String>>()
    val phoneState: LiveData<EditTextFormState<String>> = _phoneState

    fun setPhone(phone: String) {
        _phoneState.value = PhoneValidator.validatePhone(phone)
    }

    private val _emailState = MutableLiveData<EditTextFormState<String>>()
    val emailState: LiveData<EditTextFormState<String>> = _emailState

    fun setEmail(email: String) {
        _emailState.value = EmailValidator.validateEmail(email)
    }

    fun process(phone: String, email: String) {
        setPhone(phone)
        setEmail(email)

        if (_phoneState.value is EditTextFormState.SUCCESS && _emailState.value is EditTextFormState.SUCCESS) {
            compositeDisposable().add(bebasLocalDatasource.updatePhoneAndEmailAndReturn(
                phone,
                email
            )
                                          .subscribe(
                                              {
                                                  _state.value = true
                                              },
                                              {}
                                          ))
        }
    }

    var onboardingFlow: OnboardingFlow? = null

    private val _initState = MutableLiveData<InitInputPhoneAndEmailState>()
    val initState: LiveData<InitInputPhoneAndEmailState> = _initState

    fun initLastStorage() {
        compositeDisposable().add(bebasLocalDatasource.getDecryptedEntity().subscribe(
            {
                onboardingFlow = it.onboardingFlow

                if (it.phone != null && it.email != null) {
                    if (it.isFinishedOtpVerification == true && it.isFinishedEmailVerification == true && it.onboardingFlow == OnboardingFlow.ONBOARDING) {
                        _initState.value = InitInputPhoneAndEmailState.SuccessFlowOnboarding(
                            it.phone ?: "",
                            it.email ?: ""
                        )
                    } else if (it.isFinishedOtpVerification == true && it.isFinishedEmailVerification == true && it.onboardingFlow == OnboardingFlow.SELF_ACTIVATION) {
                        _initState.value = InitInputPhoneAndEmailState.SuccessFlowSelfActivation(
                            it.phone ?: "",
                            it.email ?: ""
                        )
                    } else if (it.isFinishedOtpVerification == true && it.isFinishedEmailVerification != true) {
                        _initState.value = InitInputPhoneAndEmailState.SuccessToEmail(
                            it.phone ?: "",
                            it.email ?: ""
                        )
                    } else if (it.isFinishedOtpVerification != true) {
                        _initState.value = InitInputPhoneAndEmailState.SuccessToOtp(
                            it.phone ?: "",
                            it.email ?: ""
                        )
                    }
                }
            },
            {}
        ))
    }

    fun updateIsFinishedOtpVerification(isFinished: Boolean? = null) {
        compositeDisposable().add(bebasLocalDatasource.updateIsFinishedOtpVerification(isFinished))
    }

    fun updateIsFinishedEmailVerification(isFinished: Boolean? = null) {
        compositeDisposable().add(bebasLocalDatasource.updateIsFinishedEmailVerification(isFinished))
    }
}