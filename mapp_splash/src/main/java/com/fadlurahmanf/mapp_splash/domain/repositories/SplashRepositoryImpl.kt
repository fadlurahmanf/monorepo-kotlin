package com.fadlurahmanf.mapp_splash.domain.repositories

import com.fadlurahmanf.mapp_api.data.datasources.MasIdentityRemoteDatasource
import com.fadlurahmanf.mapp_api.data.dto.general.BaseResponse
import com.fadlurahmanf.mapp_api.data.dto.identity.CreateGuestTokenResponse
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.CreateGuestTokenRequest
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class SplashRepositoryImpl @Inject constructor(
    private val identityDatasource: MasIdentityRemoteDatasource,
) {
    fun generateGuestToken(request: CreateGuestTokenRequest): Observable<BaseResponse<CreateGuestTokenResponse>> {
        return identityDatasource.generateGuestToken(request).doOnNext {
            println("MASUK SUCCESS")
        }
    }
}