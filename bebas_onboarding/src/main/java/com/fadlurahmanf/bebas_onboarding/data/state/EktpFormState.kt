package com.fadlurahmanf.bebas_onboarding.data.state

import com.fadlurahmanf.bebas_shared.data.dto.BebasItemPickerBottomsheetModel
import com.fadlurahmanf.bebas_shared.data.exception.BebasException

sealed class EktpFormState {
    object LOADING : EktpFormState()
    data class FetchedProvinces(
        val provinces: List<BebasItemPickerBottomsheetModel>
    ) : EktpFormState()

    data class FetchedCities(
        val cities: List<BebasItemPickerBottomsheetModel>
    ) : EktpFormState()

    data class FetchedSubDistricts(
        val subDistricts: List<BebasItemPickerBottomsheetModel>
    ) : EktpFormState()

    data class FetchedWards(
        val wards: List<BebasItemPickerBottomsheetModel>
    ) : EktpFormState()

    data class FAILED(val exception: BebasException) : EktpFormState()
}
