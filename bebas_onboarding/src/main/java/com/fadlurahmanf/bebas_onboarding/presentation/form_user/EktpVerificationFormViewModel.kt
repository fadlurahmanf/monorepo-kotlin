package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fadlurahmanf.bebas_onboarding.data.state.EktpFormState
import com.fadlurahmanf.bebas_onboarding.domain.repositories.DemographyRepositoryImpl
import com.fadlurahmanf.bebas_shared.data.dto.BebasItemPickerBottomsheetModel
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_storage.domain.datasource.BebasLocalDatasource
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class EktpVerificationFormViewModel @Inject constructor(
    private val demographyRepositoryImpl: DemographyRepositoryImpl,
    private val bebasLocalDatasource: BebasLocalDatasource
) : BaseViewModel() {

    var initSelectedProvinceLabel: String? = null
    var initSelectedCityLabel: String? = null
    var initSelectedSubDistrictLabel: String? = null
    var initSelectedWardLabel: String? = null

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

    fun fetchProvinces(selectedProvinceLabel: String? = null) {
        _ektpState.value = EktpFormState.LOADING
        compositeDisposable().add(demographyRepositoryImpl.getProvinceItems()
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          { models ->
                                              provinces = models
                                              var selected: BebasItemPickerBottomsheetModel? = null

                                              if (selectedProvinceLabel != null) {
                                                  selected = models.first { model ->
                                                      model.label.equals(
                                                          selectedProvinceLabel,
                                                          ignoreCase = true
                                                      )
                                                  }
                                              }

                                              if (selected != null) {
                                                  _selectedProvince.value = selected
                                                  _ektpState.value =
                                                      EktpFormState.FetchedProvincesAndSelect(
                                                          provinces = models,
                                                          selectedProvince = selected
                                                      )
                                              } else {
                                                  _ektpState.value =
                                                      EktpFormState.FetchedProvinces(models)
                                              }
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

    fun fetchCities(provinceId: String, selectedCityLabel: String? = null) {
        _ektpState.value = EktpFormState.LOADING
        compositeDisposable().add(demographyRepositoryImpl.getCityItems(provinceId)
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          { models ->
                                              cities = models

                                              var selected: BebasItemPickerBottomsheetModel? = null

                                              if (selectedCityLabel != null) {
                                                  selected = models.first { model ->
                                                      model.label.equals(
                                                          selectedCityLabel,
                                                          ignoreCase = true
                                                      )
                                                  }
                                              }

                                              if (selected != null) {
                                                  _selectedCity.value = selected
                                                  _ektpState.value =
                                                      EktpFormState.FetchedCitiesAndSelect(
                                                          cities = models,
                                                          selectedCity = selected
                                                      )
                                              } else {
                                                  _ektpState.value =
                                                      EktpFormState.FetchedCities(models)
                                              }

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

    fun fetchSubDistricts(cityId: String, selectedSubDistrictLabel: String? = null) {
        _ektpState.value = EktpFormState.LOADING
        compositeDisposable().add(demographyRepositoryImpl.getSubDistrictItems(cityId)
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          { models ->
                                              subDistricts = models

                                              var selected: BebasItemPickerBottomsheetModel? = null

                                              if (selectedSubDistrictLabel != null) {
                                                  selected = models.first { model ->
                                                      model.label.equals(
                                                          selectedSubDistrictLabel,
                                                          ignoreCase = true
                                                      )
                                                  }
                                              }

                                              if (selected != null) {
                                                  _ektpState.value =
                                                      EktpFormState.FetchedSubDistrictsAndSelect(
                                                          subDistricts = models,
                                                          selectedSubDistrict = selected
                                                      )
                                              } else {
                                                  _ektpState.value =
                                                      EktpFormState.FetchedSubDistricts(models)
                                              }
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

    fun fetchWards(subDistrictId: String, selectedWardLabel: String? = null) {
        _ektpState.value = EktpFormState.LOADING
        compositeDisposable().add(demographyRepositoryImpl.getWardItems(subDistrictId)
                                      .subscribeOn(Schedulers.io())
                                      .observeOn(AndroidSchedulers.mainThread())
                                      .subscribe(
                                          { models ->
                                              wards = models

                                              var selected: BebasItemPickerBottomsheetModel? = null

                                              if (selectedWardLabel != null) {
                                                  selected = models.first { model ->
                                                      model.label.equals(
                                                          selectedWardLabel,
                                                          ignoreCase = true
                                                      )
                                                  }
                                              }

                                              if (selected != null) {
                                                  _ektpState.value =
                                                      EktpFormState.FetchedWardAndSelect(
                                                          wards = models,
                                                          selectedWard = selected
                                                      )
                                              } else {
                                                  _ektpState.value =
                                                      EktpFormState.FetchedWards(models)
                                              }
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

    private val _initState = MutableLiveData<EktpFormState>()
    val initState: LiveData<EktpFormState> = _initState

    fun initData() {
        compositeDisposable().add(
            bebasLocalDatasource.getDecryptedEntity().subscribe(
                {
                    _initState.value = EktpFormState.FetchedLocalData(
                        nik = it.idCardNumber,
                        fullName = it.fullName,
                        birthPlace = it.birthPlace,
                        birthDate = it.birthDate,
                        gender = it.gender,
                        province = it.province,
                        city = it.city,
                        subDistrict = it.subDistrict,
                        ward = it.ward,
                        address = it.address,
                        rtRw = it.rtRw
                    )
                },
                {
                    _initState.value = EktpFormState.FAILED(BebasException.fromThrowable(it))
                },
            )
        )
    }
}