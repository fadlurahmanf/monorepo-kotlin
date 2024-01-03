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
        return loyaltyRepositoryImpl.getHistoryLoyalty(
            offset = offset,
        ).subscribeOn(Schedulers.io()).map { resp ->
            toLoadSuccess(resp)
        }.onErrorReturn {
            toLoadError(it)
        }
    }

    private fun toLoadSuccess(
        response: BasePaginationTransactionResponse<List<HistoryLoyaltyResponse>>
    ): LoadResult<Int, HistoryLoyaltyModel> {
        return LoadResult.Page(
            data = ArrayList(response.content ?: listOf()).map { content ->
                HistoryLoyaltyModel(
                    id = content.id ?: "",
                    header = "Dari Rekening ${content.accountNumber ?: "-"}",
                    body = content.label ?: "-",
                    point = content.point ?: -1,
                    response = content
                )
            },
            prevKey = null,
            nextKey = null
        )
    }

    private fun toLoadError(throwable: Throwable): LoadResult<Int, HistoryLoyaltyModel> {
        return LoadResult.Error(
            throwable
        )
    }
}