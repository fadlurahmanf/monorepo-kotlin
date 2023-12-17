package com.fadlurahmanf.bebas_transaction.domain.repositories

import android.content.Context
import com.fadlurahmanf.bebas_api.data.datasources.CifRemoteDatasource
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.FavoriteContactModel
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    context: Context,
    private val cifRemoteDatasource: CifRemoteDatasource
) {

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
}