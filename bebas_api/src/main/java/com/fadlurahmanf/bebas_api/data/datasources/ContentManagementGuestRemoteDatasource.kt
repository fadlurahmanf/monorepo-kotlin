package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.ContentManagementApi
import com.fadlurahmanf.bebas_api.data.api.IdentityGuestTokenApi
import com.fadlurahmanf.bebas_api.data.dto.identity.LoginRequest
import com.fadlurahmanf.bebas_api.data.dto.identity.RefreshUserTokenRequest
import com.fadlurahmanf.bebas_api.domain.network.ContentManagementGuestNetwork
import com.fadlurahmanf.bebas_api.domain.network.IdentityGuestNetwork
import javax.inject.Inject

class ContentManagementGuestRemoteDatasource @Inject constructor(
    context: Context
) :
    ContentManagementGuestNetwork<ContentManagementApi>(context) {
    override fun getApi(): Class<ContentManagementApi> = ContentManagementApi::class.java

    fun getTNC() =
        networkService(30).getTNC("id")
}