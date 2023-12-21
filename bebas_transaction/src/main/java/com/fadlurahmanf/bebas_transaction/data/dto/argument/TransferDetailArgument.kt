package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoriteTransferResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.LatestTransactionResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransferDetailArgument(
    var isFavorite: Boolean,
    var accountName: String,
    var accountNumber: String,
    var bankImageUrl: String? = null,
    var bankName: String,

    var favoriteResponse: FavoriteTransferResponse? = null,
    var latestTransactionResponse: LatestTransactionResponse? = null,
) : Parcelable
