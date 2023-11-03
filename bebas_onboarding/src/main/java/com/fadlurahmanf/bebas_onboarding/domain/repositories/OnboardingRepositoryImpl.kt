package com.fadlurahmanf.bebas_onboarding.domain.repositories

import com.fadlurahmanf.bebas_api.data.datasources.MasIdentityRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.identity.CreateGuestTokenResponse
import com.fadlurahmanf.bebas_api.data.dto.identity.GenerateGuestTokenRequest
import com.fadlurahmanf.core_platform.domain.repositories.DeviceRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val identityRemoteDatasource: MasIdentityRemoteDatasource,
    private val deviceRepository: DeviceRepository
) {
    fun generateGuestToken(): Observable<CreateGuestTokenResponse> {
        val guestId = deviceRepository.randomUUID()
        val request = GenerateGuestTokenRequest(
            guestId = guestId
        )
        return identityRemoteDatasource.generateGuestToken(request).map {
            it.data!!
        }
    }
}