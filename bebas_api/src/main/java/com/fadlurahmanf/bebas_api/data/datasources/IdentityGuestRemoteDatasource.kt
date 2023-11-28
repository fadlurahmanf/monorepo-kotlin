package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.IdentityGuestTokenApi
import com.fadlurahmanf.bebas_api.data.dto.identity.LoginRequest
import com.fadlurahmanf.bebas_api.data.dto.identity.RefreshUserTokenRequest
import com.fadlurahmanf.bebas_api.domain.network.IdentityGuestNetwork
import javax.inject.Inject

class IdentityGuestRemoteDatasource @Inject constructor(
    context: Context
) :
    IdentityGuestNetwork<IdentityGuestTokenApi>(context) {
    override fun getApi(): Class<IdentityGuestTokenApi> = IdentityGuestTokenApi::class.java

    fun login(request: LoginRequest) =
        networkService(30).login(request)

    fun refreshToken(request: RefreshUserTokenRequest) =
        networkService(30).refreshUserToken(request)
}