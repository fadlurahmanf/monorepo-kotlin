package com.fadlurahmanf.bebas_loyalty.presentation.history.paging_source

import android.content.Context
import android.util.Log
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.fadlurahmanf.bebas_api.data.dto.general.BasePaginationTransactionResponse
import com.fadlurahmanf.bebas_api.data.dto.loyalty.HistoryLoyaltyResponse
import com.fadlurahmanf.bebas_loyalty.data.dto.HistoryLoyaltyModel
import com.fadlurahmanf.bebas_loyalty.domain.repositories.LoyaltyRepositoryImpl
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LoyaltyHistoryPagingSource @Inject constructor(
    private val context: Context,
    private val loyaltyRepositoryImpl: LoyaltyRepositoryImpl
) : RxPagingSource<Int, HistoryLoyaltyModel>() {

    var offset = 0

    override fun getRefreshKey(state: PagingState<Int, HistoryLoyaltyModel>): Int? {
        Log.d("BebasLogger", "REFRESH KEY SINI ${state.anchorPosition}")
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, HistoryLoyaltyModel>> {
        Log.d("BebasLogger", "OFFSET: $offset")
        return loyaltyRepositoryImpl.getHistoryLoyalty(
            offset = offset,
        ).subscribeOn(Schedulers.io()).map { resp ->
            Log.d("BebasLogger", "CONTENT LENGTH: ${resp.content?.size}")
            toLoadSuccess(resp, offset)
        }.onErrorReturn {
            toLoadError(it)
        }
    }

    private fun toLoadSuccess(
        response: BasePaginationTransactionResponse<List<HistoryLoyaltyResponse>>,
        offset: Int
    ): LoadResult<Int, HistoryLoyaltyModel> {
        val isNextAvailable = response.next == true
        var nextKey: Int? = null
        if (isNextAvailable) {
            nextKey = offset + (response.content?.size ?: 0)
            this.offset = nextKey
            Log.d("BebasLogger", "IS NEXT AVAILABLE -> NEXT KEY: $nextKey")
        }
        return LoadResult.Page(
            data = ArrayList(response.content ?: listOf()).map { content ->
                HistoryLoyaltyModel(
                    id = content.id ?: "",
                    header = "Dari Rekening ${content.accountNumber ?: "-"}",
                    body = content.label ?: "-",
//                    body = "ID: ${content.id ?: "-"}",
                    point = content.point ?: -1,
                    response = content
                )
            },
            prevKey = null,
            nextKey = nextKey
        )
    }

    private fun toLoadError(throwable: Throwable): LoadResult<Int, HistoryLoyaltyModel> {
        return LoadResult.Error(
            throwable
        )
    }
}