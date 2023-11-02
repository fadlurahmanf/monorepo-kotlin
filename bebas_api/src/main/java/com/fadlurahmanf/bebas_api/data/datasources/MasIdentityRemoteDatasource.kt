package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.MasIdentityApi
import com.fadlurahmanf.bebas_api.domain.network.MasIdentityNetwork
import com.fadlurahmanf.bebas_api.data.dto.identity.GenerateGuestTokenRequest
import javax.inject.Inject

class MasIdentityRemoteDatasource @Inject constructor(context: Context) :
    MasIdentityNetwork<MasIdentityApi>(context) {
    override fun getApi(): Class<MasIdentityApi> = MasIdentityApi::class.java

    fun generateGuestToken(request: GenerateGuestTokenRequest) =
        networkService(30).createGuestToken(request)
}