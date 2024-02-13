package com.fadlurahmanf.bebas_loyalty.presentation.history.paging_source

import android.util.Log
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.fadlurahmanf.bebas_api.data.dto.general.BasePaginationTransactionResponse
import com.fadlurahmanf.bebas_api.data.dto.loyalty.HistoryLoyaltyResponse
import com.fadlurahmanf.bebas_loyalty.data.dto.HistoryLoyaltyModel
import com.fadlurahmanf.bebas_loyalty.domain.repositories.LoyaltyRepositoryImpl
import com.fadlurahmanf.bebas_shared.extension.formatHeaderHistoryLoyalty
import com.fadlurahmanf.bebas_shared.extension.utcToLocal
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class LoyaltyHistoryPagingSource(
    private val status: String? = null,
    private val loyaltyRepositoryImpl: LoyaltyRepositoryImpl
) : RxPagingSource<Int, HistoryLoyaltyModel>() {
    var offset = 0
    val mapDateHeader: HashMap<String, String> = hashMapOf()

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
            status = status
        ).subscribeOn(Schedulers.io()).map { resp ->
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
        }
        val contents = ArrayList(response.content ?: listOf())
        val newContents = arrayListOf<HistoryLoyaltyModel>()
        contents.forEach { content ->
            val formattedDate = content.createdDate?.utcToLocal()?.formatHeaderHistoryLoyalty()
            if (formattedDate != null) {
                if (!mapDateHeader.containsKey(formattedDate)) {
                    mapDateHeader[formattedDate] = formattedDate
                    newContents.add(
                        HistoryLoyaltyModel(
                            id = "${content.id}-$formattedDate",
                            header = formattedDate,
                            topLabel = formattedDate,
                            subLabel = formattedDate,
                            type = 0
                        )
                    )
                }
                newContents.add(
                    HistoryLoyaltyModel(
                        id = content.id ?: "-",
                        header = formattedDate,
                        topLabel = "Dari Rekening ${content.accountNumber ?: "-"}",
                        subLabel = content.label ?: "-",
                        response = content
                    )
                )
            }
        }
        return LoadResult.Page(
            data = newContents,
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