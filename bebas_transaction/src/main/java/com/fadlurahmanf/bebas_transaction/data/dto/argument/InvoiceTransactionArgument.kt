package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvoiceTransactionArgument(
    var statusTransaction: String,
    var transactionId: String,
    var isFavorite: Boolean,
    var isFavoriteEnabled: Boolean,
    var additionalTransfer: Transfer? = null
) : Parcelable {
    @Parcelize
    data class Transfer(
        var destinationAccountName: String,
        var destinationBankNickName: String,
        var destinationAccountNumber: String,
        var nominal: Long
    ) : Parcelable
}