package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_onboarding.data.state.EktpFormState
import com.fadlurahmanf.bebas_onboarding.domain.repositories.DemographyRepositoryImpl
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class EktpVerificationFormViewModel @Inject constructor(
    private val demographyRepositoryImpl: DemographyRepositoryImpl
) : BaseViewModel() {

    private val _ektpState = MutableLiveData<EktpFormState>()
    val ektpState: LiveData<EktpFormState> = _ektpState

    fun fetchProvinces() {
        _ektpState.value = EktpFormState.LOADING
        compositeDisposable().add(demographyRepositoryImpl.getProvinceItems()
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              _ektpState.value = EktpFormState.FetchedProvinces(it)
                                          },
                                          {
                                              _ektpState.value = EktpFormState.FAILED(
                                                  BebasException.fromThrowable(it)
                                              )

                                          },
                                          {}
                                      ))
    }
}