package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.IdentityWithoutGuestApi
import com.fadlurahmanf.bebas_api.domain.network.IdentityWithoutGuestNetwork
import com.fadlurahmanf.bebas_api.data.dto.auth.GenerateGuestTokenRequest
import javax.inject.Inject

class IdentityWithoutGuestRemoteDatasource @Inject constructor(context: Context) :
    IdentityWithoutGuestNetwork<IdentityWithoutGuestApi>(context) {
    override fun getApi(): Class<IdentityWithoutGuestApi> = IdentityWithoutGuestApi::class.java

    fun generateGuestToken(request: GenerateGuestTokenRequest) =
        networkService(30).createGuestToken(request)
}