package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_storage.domain.datasource.BebasLocalDatasource
import com.fadlurahmanf.bebas_shared.state.EditTextFormState
import com.fadlurahmanf.bebas_shared.validator.PhoneValidator
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import javax.inject.Inject

class InputPhoneEmailViewModel @Inject constructor(
    private val bebasLocalDatasource: BebasLocalDatasource
) : BaseViewModel() {

    private val _state = MutableLiveData<Boolean>()
    val state: LiveData<Boolean> = _state

    private val _phoneState = MutableLiveData<EditTextFormState<String>>()
    val phoneState: LiveData<EditTextFormState<String>> = _phoneState

    fun setPhone(phone: String) {
        _phoneState.value = PhoneValidator.validatePhone(phone)
    }

    fun process(phone: String, email: String) {
        compositeDisposable().add(bebasLocalDatasource.updatePhoneAndEmailAndReturn(phone, email)
                                      .subscribe(
                                          {
                                              _state.value = true
                                          },
                                          {}
                                      ))
    }
}