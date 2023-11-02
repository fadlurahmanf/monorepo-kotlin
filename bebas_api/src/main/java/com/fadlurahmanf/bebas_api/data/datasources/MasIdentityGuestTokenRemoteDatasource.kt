package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.MasIdentityGuestTokenApi
import com.fadlurahmanf.bebas_api.data.dto.identity.LoginRequest
import com.fadlurahmanf.bebas_api.data.dto.identity.RefreshUserTokenRequest
import com.fadlurahmanf.bebas_api.domain.network.MasIdentityGuestTokenNetwork
import javax.inject.Inject

class MasIdentityGuestTokenRemoteDatasource @Inject constructor(
    context: Context
) :
    MasIdentityGuestTokenNetwork<MasIdentityGuestTokenApi>(context) {
    override fun getApi(): Class<MasIdentityGuestTokenApi> = MasIdentityGuestTokenApi::class.java

    fun login(request: LoginRequest) =
        networkService(30).login(request)

    fun refreshToken(request: RefreshUserTokenRequest) =
        networkService(30).refreshUserToken(request)
}