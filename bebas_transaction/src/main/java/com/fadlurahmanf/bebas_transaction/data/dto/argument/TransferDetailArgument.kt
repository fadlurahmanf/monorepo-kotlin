package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransferDetailArgument(
    var isFavorite: Boolean,
    var isFavoriteEnabled: Boolean = false,
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
