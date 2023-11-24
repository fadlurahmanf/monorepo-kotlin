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

            it.data!!.map { provinceResp ->
                BebasItemPickerBottomsheetModel(
                    id = provinceResp.id ?: "",
                    label = provinceResp.name ?: ""
                )
            }
        }
    }

    fun getCityItems(provinceId: String): Observable<List<BebasItemPickerBottomsheetModel>> {
        return onboardingGuestRemoteDatasource.getCities(provinceId).map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }

            it.data!!.map { provinceResp ->
                BebasItemPickerBottomsheetModel(
                    id = provinceResp.id ?: "",
                    label = provinceResp.name ?: ""
                )
            }
        }
    }
}