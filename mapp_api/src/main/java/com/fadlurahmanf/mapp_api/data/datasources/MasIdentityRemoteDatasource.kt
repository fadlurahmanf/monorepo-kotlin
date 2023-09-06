package com.fadlurahmanf.mapp_api.data.datasources

import android.content.Context
import com.fadlurahmanf.mapp_api.data.api.MasIdentityApi
import com.fadlurahmanf.mapp_api.domain.network.MasIdentityNetwork
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.CreateGuestTokenRequest
import javax.inject.Inject

class MasIdentityRemoteDatasource @Inject constructor(context: Context) :
    MasIdentityNetwork<MasIdentityApi>(context) {
    override fun getApi(): Class<MasIdentityApi> = MasIdentityApi::class.java

    fun generateGuestToken(request: CreateGuestTokenRequest) =
        networkService(30).createGuestToken(request)
}