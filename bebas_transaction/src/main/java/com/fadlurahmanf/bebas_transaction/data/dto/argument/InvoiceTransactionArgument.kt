package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvoiceTransactionArgument(
    val transactionId: String,
    var statusTransaction: String,
    var transactionDate: String,
    var isFavorite: Boolean,
    val isFavoriteEnabled: Boolean,
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