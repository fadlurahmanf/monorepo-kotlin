package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionConfirmationCheckoutArgument(
    val destinationLabel: String,
    val destinationSubLabel: String,
    var imageLogoUrl: String? = null,
    val additionalPLNPrePaid:PLNPrePaid?=null
) : Parcelable {
    @Parcelize
    data class PLNPrePaid(
        val productCode: String,
    ) : Parcelable
}
