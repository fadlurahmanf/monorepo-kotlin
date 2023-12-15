package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.IdentityGuestApi
import com.fadlurahmanf.bebas_api.data.dto.auth.LoginRequest
import com.fadlurahmanf.bebas_api.data.dto.auth.RefreshUserTokenRequest
import com.fadlurahmanf.bebas_api.domain.network.IdentityGuestNetwork
import javax.inject.Inject

class IdentityGuestRemoteDatasource @Inject constructor(
    context: Context
) :
    IdentityGuestNetwork<IdentityGuestApi>(context) {
    override fun getApi(): Class<IdentityGuestApi> = IdentityGuestApi::class.java

    fun login(request: LoginRequest) =
        networkService(30).login(request)

    fun refreshToken(request: RefreshUserTokenRequest) =
        networkService(30).refreshUserToken(request)
}