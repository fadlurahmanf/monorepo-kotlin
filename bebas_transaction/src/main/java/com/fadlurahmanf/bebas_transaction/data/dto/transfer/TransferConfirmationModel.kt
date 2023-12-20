package com.fadlurahmanf.bebas_transaction.data.dto.transfer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransferConfirmationModel(
    val realAccountName: String,
    val destinationAccountNumber: String,
    var imageLogoUrl: String? = null,
    val nominal: Double,
) : Parcelable
