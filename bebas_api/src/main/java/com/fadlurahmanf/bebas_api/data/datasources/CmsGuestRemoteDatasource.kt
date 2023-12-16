package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.CmsGuestApi
import com.fadlurahmanf.bebas_api.domain.network.CmsGuestNetwork
import javax.inject.Inject

class CmsGuestRemoteDatasource @Inject constructor(
    context: Context
) :
    CmsGuestNetwork<CmsGuestApi>(context) {
    override fun getApi(): Class<CmsGuestApi> = CmsGuestApi::class.java

    fun getWelcomeBanner() = networkService(30).getWelcomeBanner("id")

    fun getTNC() =
        networkService(30).getTNC("id")
}