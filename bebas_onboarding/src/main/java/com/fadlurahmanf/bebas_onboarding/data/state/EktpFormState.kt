package com.fadlurahmanf.bebas_onboarding.data.state

import com.fadlurahmanf.bebas_shared.data.dto.BebasItemPickerBottomsheetModel
import com.fadlurahmanf.bebas_shared.data.exception.BebasException

sealed class EktpFormState {
    object LOADING : EktpFormState()

    data class FetchedLocalData(
        val nik: String? = null,
        val fullName: String? = null,
        val birthPlace: String? = null,
        val birthDate: String? = null,
        val gender: String? = null,
        val province: String? = null,
        val city: String? = null,
        val subDistrict: String? = null,
        val ward: String? = null,
        val rtRw: String? = null,
        val address: String? = null,
    ) : EktpFormState()

    data class FetchedProvincesAndSelect(
        val provinces: List<BebasItemPickerBottomsheetModel>,
        val selectedProvince: BebasItemPickerBottomsheetModel
    ) : EktpFormState()

    data class FetchedProvinces(
        val provinces: List<BebasItemPickerBottomsheetModel>
    ) : EktpFormState()

    data class FetchedCities(
        val cities: List<BebasItemPickerBottomsheetModel>
    ) : EktpFormState()

    data class FetchedCitiesAndSelect(
        val cities: List<BebasItemPickerBottomsheetModel>,
        val selectedCity: BebasItemPickerBottomsheetModel
    ) : EktpFormState()

    data class FetchedSubDistricts(
        val subDistricts: List<BebasItemPickerBottomsheetModel>
    ) : EktpFormState()

    data class FetchedSubDistrictsAndSelect(
        val subDistricts: List<BebasItemPickerBottomsheetModel>,
        val selectedSubDistrict: BebasItemPickerBottomsheetModel
    ) : EktpFormState()

    data class FetchedWards(
        val wards: List<BebasItemPickerBottomsheetModel>
    ) : EktpFormState()

    data class FetchedWardAndSelect(
        val wards: List<BebasItemPickerBottomsheetModel>,
        val selectedWard: BebasItemPickerBottomsheetModel
    ) : EktpFormState()

    data class FAILED(val exception: BebasException) : EktpFormState()
}
