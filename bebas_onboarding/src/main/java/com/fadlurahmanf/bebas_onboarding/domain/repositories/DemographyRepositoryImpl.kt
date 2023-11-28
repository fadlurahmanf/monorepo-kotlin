package com.fadlurahmanf.bebas_onboarding.domain.repositories

import com.fadlurahmanf.bebas_api.data.datasources.OnboardingGuestRemoteDatasource
import com.fadlurahmanf.bebas_shared.data.dto.BebasItemPickerBottomsheetModel
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class DemographyRepositoryImpl @Inject constructor(
    private val onboardingGuestRemoteDatasource: OnboardingGuestRemoteDatasource
) {
    fun getProvinceItems(): Observable<List<BebasItemPickerBottomsheetModel>> {
        return onboardingGuestRemoteDatasource.getProvinces().map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }

            it.data!!.map { demoResp ->
                BebasItemPickerBottomsheetModel(
                    id = demoResp.id ?: "",
                    label = demoResp.name ?: ""
                )
            }
        }
    }

    fun getCityItems(provinceId: String): Observable<List<BebasItemPickerBottomsheetModel>> {
        return onboardingGuestRemoteDatasource.getCities(provinceId).map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }

            it.data!!.map { demoResp ->
                BebasItemPickerBottomsheetModel(
                    id = demoResp.id ?: "",
                    label = demoResp.name ?: ""
                )
            }
        }
    }

    fun getSubDistrictItems(cityId: String): Observable<List<BebasItemPickerBottomsheetModel>> {
        return onboardingGuestRemoteDatasource.getSubDistricts(cityId).map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }

            it.data!!.map { demoResp ->
                BebasItemPickerBottomsheetModel(
                    id = demoResp.id ?: "",
                    label = demoResp.name ?: ""
                )
            }
        }
    }

    fun getWardItems(subDistrictId: String): Observable<List<BebasItemPickerBottomsheetModel>> {
        return onboardingGuestRemoteDatasource.getWards(subDistrictId).map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }

            it.data!!.map { demoResp ->
                BebasItemPickerBottomsheetModel(
                    id = demoResp.id ?: "",
                    label = demoResp.name ?: ""
                )
            }
        }
    }
}