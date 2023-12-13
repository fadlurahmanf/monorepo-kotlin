package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.IdentityApi
import com.fadlurahmanf.bebas_api.domain.network.IdentityNetwork
import com.fadlurahmanf.bebas_api.data.dto.auth.GenerateGuestTokenRequest
import javax.inject.Inject

class IdentityRemoteDatasource @Inject constructor(context: Context) :
    IdentityNetwork<IdentityApi>(context) {
    override fun getApi(): Class<IdentityApi> = IdentityApi::class.java

    fun generateGuestToken(request: GenerateGuestTokenRequest) =
        networkService(30).createGuestToken(request)
}