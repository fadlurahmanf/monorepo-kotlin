package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import android.util.Log
import com.fadlurahmanf.bebas_api.data.api.InboxApi
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.notification.NotificationResponse
import com.fadlurahmanf.bebas_api.domain.network.CifNetwork
import com.fadlurahmanf.bebas_api.domain.network.InboxNetwork
import com.fadlurahmanf.bebas_shared.extension.formatFetchNotification
import com.fadlurahmanf.bebas_shared.extension.formatNotification
import com.fadlurahmanf.bebas_shared.extension.formatToEktpForm
import io.reactivex.rxjava3.core.Single
import java.util.Calendar
import javax.inject.Inject

class InboxRemoteDatasource @Inject constructor(
    context: Context
) : InboxNetwork<InboxApi>(context) {
    override fun getApi(): Class<InboxApi> = InboxApi::class.java

    fun getTransactionNotification(
        page: Int,
        startDate: String? = null,
        endDate: String? = null,
        searchText: String? = null,
        status: String? = null,
    ): Single<BaseResponse<NotificationResponse>> {
        val startDateNew = Calendar.getInstance()
        startDateNew.add(Calendar.YEAR, -1)
        val endDateNew = Calendar.getInstance()
        return networkService().getNotification(
            type = "TRANSACTION",
            page = page,
            startDate = startDate ?: startDateNew.time.formatFetchNotification(),
            endDate = endDate ?: endDateNew.time.formatFetchNotification(),
            searchText = searchText,
            status = status
        )
    }

    fun getUnreadNotificationCount() = networkService().getUnreadNotificationCount()
}