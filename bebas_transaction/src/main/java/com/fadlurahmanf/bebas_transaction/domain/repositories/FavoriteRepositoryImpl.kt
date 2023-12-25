package com.fadlurahmanf.bebas_transaction.domain.repositories

import android.content.Context
import com.fadlurahmanf.bebas_api.data.datasources.CifRemoteDatasource
import com.fadlurahmanf.bebas_api.data.dto.favorite.PinFavoriteRequest
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.model.favorite.FavoriteContactModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.favorite.LatestTransactionModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    context: Context,
    private val cifRemoteDatasource: CifRemoteDatasource
) {

    fun getLatestTransactionTransfer(): Observable<List<LatestTransactionModel>> {
        return cifRemoteDatasource.getLatestTransactionTransfer().map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }

            val latest = it.data!!
            latest.map { latestResp ->
                LatestTransactionModel(
                    name = latestResp.accountName ?: "-",
                    labelLatest = latestResp.bankName ?: "-",
                    accountNumber = latestResp.accountNumber ?: "-",
                    additionalTransferData = latestResp
                )
            }.toList()
        }
    }

    fun getLatestTransactionPLNPrePaid(): Observable<List<LatestTransactionModel>> {
        return cifRemoteDatasource.getLatestTransactionPLNPrePaid().map {
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
                    nameInFavoriteContact = favResp.aliasName ?: "-",
                    labelTypeOfFavorite = favResp.bankName ?: "-",
                    accountNumber = favResp.bankAccountNumber ?: "-",
                    isPinned = favResp.isPinned ?: false,
                    additionalTransferData = favResp
                )
            }.toList()
        }
    }

    fun getFavoritePLNPrePaid(): Observable<List<FavoriteContactModel>> {
        return cifRemoteDatasource.getFavoritePLNPrePaid().map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }

            val favorites = it.data!!
            favorites.map { favResp ->
                FavoriteContactModel(
                    id = favResp.id ?: "-",
                    nameInFavoriteContact = favResp.aliasName ?: "-",
                    labelTypeOfFavorite = favResp.aliasName ?: "-",
                    accountNumber = favResp.accountNumber ?: "-",
                    isPinned = favResp.isPinned ?: false,
                    additionalPlnPrePaidData = favResp
                )
            }.toList()
        }
    }

    fun getFavoritePulsaPrePaid(): Observable<List<FavoriteContactModel>> {
        return cifRemoteDatasource.getFavoritePulsaPrePaid().map {
            if (it.data == null) {
                throw BebasException.generalRC("DATA_MISSING")
            }

            val favorites = it.data!!
            favorites.map { favResp ->
                FavoriteContactModel(
                    id = favResp.id ?: "-",
                    nameInFavoriteContact = favResp.aliasName ?: "-",
                    labelTypeOfFavorite = favResp.aliasName ?: "-",
                    accountNumber = favResp.accountNumber ?: "-",
                    isPinned = favResp.isPinned ?: false,
                    additionalPulsaPrePaidData = favResp
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