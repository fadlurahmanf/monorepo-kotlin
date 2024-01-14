package com.fadlurahmanf.bebas_api.data.dto.transfer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CheckoutTransactionDataRequest(
    val orderId: String,
    val paymentConfigGroupId: String,
    val paymentSourceSchema: List<Schema> = listOf(),
    val paymentTypeCode: String,
) : Parcelable {
    @Parcelize
    data class Schema(
        val accountNumber: String? = null,
        val amount: Double,
        val code: String,
        val status: Boolean,
        val type: String
    ) : Parcelable
}
