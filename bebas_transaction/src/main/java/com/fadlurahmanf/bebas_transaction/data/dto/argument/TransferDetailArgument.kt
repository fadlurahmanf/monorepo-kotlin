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
    var realAccountName: String,
    var accountNumber: String,
    var bankImageUrl: String? = null,
    var bankName: String
) : Parcelable {
    data class FundTransferBankMAS(
        val sknId: String = ""
    )
}
