package com.fadlurahmanf.mapp_api.data.datasources.remote

import android.content.Context
import com.fadlurahmanf.mapp_api.data.api.MasIdentityApi
import com.fadlurahmanf.mapp_api.domain.network.MasIdentityNetwork
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.CreateGuestTokenRequest

class MasIdentityRemoteDatasource(context: Context) : MasIdentityNetwork<MasIdentityApi>(context) {
    override fun getApi(): Class<MasIdentityApi> = MasIdentityApi::class.java

    fun generateGuestToken(request: CreateGuestTokenRequest) =
        networkService(30).createGuestToken(request)
}