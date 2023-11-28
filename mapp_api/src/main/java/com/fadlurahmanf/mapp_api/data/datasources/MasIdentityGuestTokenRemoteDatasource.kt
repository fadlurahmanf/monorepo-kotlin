package com.fadlurahmanf.mapp_api.data.datasources

import android.content.Context
import com.fadlurahmanf.mapp_api.data.api.MasIdentityGuestTokenApi
import com.fadlurahmanf.mapp_api.data.dto.identity.LoginRequest
import com.fadlurahmanf.mapp_api.data.dto.identity.RefreshUserTokenRequest
import com.fadlurahmanf.mapp_api.domain.network.MasIdentityGuestTokenNetworkMapp
import com.fadlurahmanf.mapp_storage.domain.datasource.MappLocalDatasource
import javax.inject.Inject

class MasIdentityGuestTokenRemoteDatasource @Inject constructor(
    context: Context,
    mappLocalDatasource: MappLocalDatasource
) :
    MasIdentityGuestTokenNetworkMapp<MasIdentityGuestTokenApi>(context, mappLocalDatasource) {
    override fun getApi(): Class<MasIdentityGuestTokenApi> = MasIdentityGuestTokenApi::class.java

    fun login(request: LoginRequest) =
        networkService(30).login(request)

    fun refreshToken(request: RefreshUserTokenRequest) =
        networkService(30).refreshUserToken(request)
}