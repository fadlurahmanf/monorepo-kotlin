package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.InboxApi
import com.fadlurahmanf.bebas_api.domain.network.CifNetwork
import javax.inject.Inject

class InboxRemoteDatasource @Inject constructor(
    context: Context
) : CifNetwork<InboxApi>(context) {
    override fun getApi(): Class<InboxApi> = InboxApi::class.java

    fun getTransactionNotification(
        page: Int,
        startDate: String? = null,
        endDate: String? = null,
        searchText: String? = null,
        status: String? = null,
    ) = networkService().getNotification(
        type = "TRANSACTION",
        page = page,
        startDate = startDate,
        endDate = endDate,
        searchText = searchText,
        status = status
    )
}