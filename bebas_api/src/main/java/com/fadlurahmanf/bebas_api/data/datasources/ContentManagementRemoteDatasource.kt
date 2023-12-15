package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.ContentManagementApi
import com.fadlurahmanf.bebas_api.domain.network.ContentManagementNetwork
import javax.inject.Inject

class ContentManagementRemoteDatasource @Inject constructor(
    context: Context
) :
    ContentManagementNetwork<ContentManagementApi>(context) {
    override fun getApi(): Class<ContentManagementApi> = ContentManagementApi::class.java

    fun getTransactionMenu() = networkService(30).getTransactionMenu()
}