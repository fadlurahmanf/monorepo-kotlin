package com.fadlurahmanf.bebas_loyalty.domain.repositories

import android.content.Context
import com.fadlurahmanf.bebas_api.data.datasources.TransactionRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.general.BasePaginationTransactionResponse
import com.fadlurahmanf.bebas_api.data.dto.loyalty.HistoryLoyaltyResponse
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class LoyaltyRepositoryImpl @Inject constructor(
    context: Context,
    private val transactionRemoteDatasource: TransactionRemoteDatasource
) {

    fun getHistoryLoyalty(offset: Int): Single<BasePaginationTransactionResponse<List<HistoryLoyaltyResponse>>> {
        return transactionRemoteDatasource.getAllHistory(offset).map {
            if (it.data == null) {
                throw BebasException.generalRC("HL_00")
            }
            it.data!!
        }
    }
}