package com.fadlurahmanf.bebas_main.presentation.notification.paging_source

import android.content.Context
import android.util.Log
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.fadlurahmanf.bebas_api.data.dto.notification.NotificationResponse
import com.fadlurahmanf.bebas_main.data.dto.notification.NotificationModel
import com.fadlurahmanf.bebas_main.domain.repositories.MainRepositoryImpl
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class NotificationTransactionPagingSource @Inject constructor(
    private val context: Context,
    private val mainRepositoryImpl: MainRepositoryImpl
) : RxPagingSource<Int, NotificationModel>() {

    var defaultPage = 0

    override fun getRefreshKey(state: PagingState<Int, NotificationModel>): Int? {
        Log.d("BebasLogger", "REFRESH KEY SINI ${state.anchorPosition}")
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, NotificationModel>> {
        val page = params.key ?: defaultPage
        return mainRepositoryImpl.getTransactionNotification(
            page = page,
        ).subscribeOn(Schedulers.io()).map { resp ->
            toLoadSuccess(resp, page)
        }.onErrorReturn {
            toLoadError(it)
        }
    }

    private fun toLoadSuccess(
        response: NotificationResponse,
        key: Int
    ): LoadResult<Int, NotificationModel> {
        Log.d("BebasLogger", "KEY: $key")
        val prevKey = null
        val nextKey = if (response.isLast != true) key + 1 else null
        Log.d("BebasLogger", "PREV: $prevKey")
        Log.d("BebasLogger", "NEXT: $nextKey")
        return LoadResult.Page(
            data = ArrayList(response.contents ?: listOf()).map { content ->
                NotificationModel(
                    id = content.messageId ?: "-",
                    titleMessage = content.headerMessage ?: "-",
                    bodyMessage = content.bodyMessage ?: "-",
                    time = content.transactionDate ?: "",
                )
            },
            prevKey = prevKey,
            nextKey = nextKey
        )
    }

    private fun toLoadError(throwable: Throwable): LoadResult<Int, NotificationModel> {
        return LoadResult.Error(
            throwable
        )
    }
}