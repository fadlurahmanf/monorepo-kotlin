package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentDetailArgument(
    var isFavorite: Boolean,
    var isFavoriteEnabled: Boolean = false,
    var labelIdentity: String,
    var subLabelIdentity: String,
    var ppobImageUrl: String? = null,
) : Parcelable {
    data class FundTransferBankMAS(
        val sknId: String = ""
    )
}
