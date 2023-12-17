package com.fadlurahmanf.bebas_transaction.domain.repositories

import android.content.Context
import com.fadlurahmanf.bebas_api.data.datasources.CifRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.favorite.LatestTransactionResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.PinFavoriteRequest
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.FavoriteContactModel
import com.fadlurahmanf.bebas_transaction.data.dto.LatestTransactionModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    context: Context,
    private val cifRemoteDatasource: CifRemoteDatasource
) {

    fun getLatestTransfer(): Observable<List<LatestTransactionModel>> {
        return cifRemoteDatasource.getLatestTransactionTransfer().map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }

            val latest = it.data!!
            latest.map { latestResp ->
                LatestTransactionModel(
                    name = latestResp.accountName ?: "-",
                    labelLatest = latestResp.bankName ?: "-",
                    accountNumber = latestResp.accountNumber ?: "-"
                )
            }.toList()
        }
    }

    fun getFavoriteTransfer(): Observable<List<FavoriteContactModel>> {
        return cifRemoteDatasource.getFavoriteTransfer().map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }

            val favorites = it.data!!
            favorites.map { favResp ->
                FavoriteContactModel(
                    id = favResp.id ?: "-",
                    nameInFavoriteContact = favResp.nameInFavorite ?: "-",
                    labelTypeOfFavorite = favResp.bankName ?: "-",
                    accountNumber = favResp.bankAccountNumber ?: "-",
                    isPinned = favResp.isPinned ?: false,
                )
            }.toList()
        }
    }

    fun pinFavorite(id: String, isPinned: Boolean): Observable<Boolean> {
        val request = PinFavoriteRequest(
            id, isPinned
        )
        return cifRemoteDatasource.pinFavorite(request).map {
            it.code == "200" && it.message == "SUCCESS"
        }
    }
}