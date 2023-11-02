package com.fadlurahmanf.bebas_onboarding.domain.repositories

import com.fadlurahmanf.bebas_api.data.datasources.MasIdentityRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.identity.CreateGuestTokenResponse
import com.fadlurahmanf.bebas_api.data.dto.identity.GenerateGuestTokenRequest
import io.reactivex.rxjava3.core.Observable
import java.util.UUID
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val identityRemoteDatasource: MasIdentityRemoteDatasource
) {

    fun generateGuestToken(): Observable<BaseResponse<CreateGuestTokenResponse>> {
        val request = GenerateGuestTokenRequest(
            guestId = UUID.randomUUID().toString()
        )
        return identityRemoteDatasource.generateGuestToken(request)
    }
}