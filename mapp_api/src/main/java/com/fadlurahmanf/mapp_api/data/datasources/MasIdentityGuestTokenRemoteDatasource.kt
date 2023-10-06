package com.fadlurahmanf.mapp_api.data.datasources

import android.content.Context
import com.fadlurahmanf.mapp_api.data.api.MasIdentityApi
import com.fadlurahmanf.mapp_api.data.api.MasIdentityGuestTokenApi
import com.fadlurahmanf.mapp_api.data.dto.identity.LoginRequest
import com.fadlurahmanf.mapp_api.domain.network.MasIdentityGuestTokenNetwork
import com.fadlurahmanf.mapp_api.domain.network.MasIdentityNetwork
import com.fadlurahmanf.mapp_storage.domain.datasource.MappLocalDatasource
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.CreateGuestTokenRequest
import javax.inject.Inject

class MasIdentityGuestTokenRemoteDatasource @Inject constructor(
    context: Context,
    mappLocalDatasource: MappLocalDatasource
) :
    MasIdentityGuestTokenNetwork<MasIdentityGuestTokenApi>(context, mappLocalDatasource) {
    override fun getApi(): Class<MasIdentityGuestTokenApi> = MasIdentityGuestTokenApi::class.java

    fun login(request: LoginRequest) =
        networkService(30).login(request)
}