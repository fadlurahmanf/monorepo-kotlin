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
    var additionalTransfer: Transfer? = null,
    var additionalTelkomIndihome: TelkomIndihome? = null,
) : Parcelable {
    @Parcelize
    data class Transfer(
        var destinationAccountName: String,
        var destinationBankNickName: String,
        var destinationAccountNumber: String,
        var nominal: Long
    ) : Parcelable

    @Parcelize
    data class TelkomIndihome(
        var destinationAccountName: String,
        var destinationAccountNumber: String,
        var totalTransaction: Double
    ) : Parcelable
}