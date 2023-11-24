package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_onboarding.data.state.EktpFormState
import com.fadlurahmanf.bebas_onboarding.domain.repositories.DemographyRepositoryImpl
import com.fadlurahmanf.bebas_shared.data.dto.BebasItemPickerBottomsheetModel
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class EktpVerificationFormViewModel @Inject constructor(
    private val demographyRepositoryImpl: DemographyRepositoryImpl
) : BaseViewModel() {

    var provinces: List<BebasItemPickerBottomsheetModel>? = null
    var cities: List<BebasItemPickerBottomsheetModel>? = null
    var subDistricts: List<BebasItemPickerBottomsheetModel>? = null
    var wards: List<BebasItemPickerBottomsheetModel>? = null

    private val _selectedProvince = MutableLiveData<BebasItemPickerBottomsheetModel?>()
    val selectedProvince: LiveData<BebasItemPickerBottomsheetModel?> = _selectedProvince

    private val _selectedCity = MutableLiveData<BebasItemPickerBottomsheetModel?>()
    val selectedCity: LiveData<BebasItemPickerBottomsheetModel?> = _selectedCity

    private val _selectedSubDistrict = MutableLiveData<BebasItemPickerBottomsheetModel?>()
    val selectedSubDistrict: LiveData<BebasItemPickerBottomsheetModel?> = _selectedSubDistrict

    private val _selectedWard = MutableLiveData<BebasItemPickerBottomsheetModel?>()
    val selectedWard: LiveData<BebasItemPickerBottomsheetModel?> = _selectedWard

    private val _ektpState = MutableLiveData<EktpFormState>()
    val ektpState: LiveData<EktpFormState> = _ektpState

    fun fetchProvinces() {
        _ektpState.value = EktpFormState.LOADING
        compositeDisposable().add(demographyRepositoryImpl.getProvinceItems()
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              provinces = it
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

    fun selectProvince(province: BebasItemPickerBottomsheetModel) {
        if (_selectedProvince.value?.id != province.id) {
            _selectedProvince.value = province

            _selectedCity.value = null
            cities = null

            _selectedSubDistrict.value = null
            subDistricts = null

            _selectedWard.value = null
            wards = null
        }
    }

    fun fetchCities(provinceId: String) {
        _ektpState.value = EktpFormState.LOADING
        compositeDisposable().add(demographyRepositoryImpl.getCityItems(provinceId)
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              cities = it
                                              _ektpState.value = EktpFormState.FetchedCities(it)
                                          },
                                          {
                                              _ektpState.value = EktpFormState.FAILED(
                                                  BebasException.fromThrowable(it)
                                              )
                                          },
                                          {}
                                      ))
    }

    fun selectCity(city: BebasItemPickerBottomsheetModel) {
        _selectedCity.value = city

        _selectedSubDistrict.value = null
        subDistricts = null

        _selectedWard.value = null
        wards = null
    }

    fun fetchSubDistricts(cityId: String) {
        _ektpState.value = EktpFormState.LOADING
        compositeDisposable().add(demographyRepositoryImpl.getSubDistrictItems(cityId)
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              subDistricts = it
                                              _ektpState.value =
                                                  EktpFormState.FetchedSubDistricts(it)
                                          },
                                          {
                                              _ektpState.value = EktpFormState.FAILED(
                                                  BebasException.fromThrowable(it)
                                              )
                                          },
                                          {}
                                      ))
    }

    fun selectSubDistrict(subDistrict: BebasItemPickerBottomsheetModel) {
        _selectedSubDistrict.value = subDistrict

        _selectedWard.value = null
        wards = null
    }

    fun fetchWards(subDistrictId: String) {
        _ektpState.value = EktpFormState.LOADING
        compositeDisposable().add(demographyRepositoryImpl.getWardItems(subDistrictId)
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          {
                                              wards = it
                                              _ektpState.value =
                                                  EktpFormState.FetchedWards(it)
                                          },
                                          {
                                              _ektpState.value = EktpFormState.FAILED(
                                                  BebasException.fromThrowable(it)
                                              )
                                          },
                                          {}
                                      ))
    }

    fun selectWard(ward: BebasItemPickerBottomsheetModel) {
        _selectedWard.value = ward
    }
}