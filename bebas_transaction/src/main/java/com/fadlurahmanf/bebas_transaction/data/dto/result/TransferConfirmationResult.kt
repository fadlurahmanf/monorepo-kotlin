package com.fadlurahmanf.bebas_transaction.data.dto.result

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransferConfirmationResult(
    val selectedAccountNumber: String
) : Parcelable
