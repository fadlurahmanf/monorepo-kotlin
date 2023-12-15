package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.ContentManagementGuestApi
import com.fadlurahmanf.bebas_api.domain.network.ContentManagementGuestNetwork
import javax.inject.Inject

class ContentManagementGuestRemoteDatasource @Inject constructor(
    context: Context
) :
    ContentManagementGuestNetwork<ContentManagementGuestApi>(context) {
    override fun getApi(): Class<ContentManagementGuestApi> = ContentManagementGuestApi::class.java

    fun getWelcomeBanner() = networkService(30).getWelcomeBanner("id")

    fun getTNC() =
        networkService(30).getTNC("id")
}