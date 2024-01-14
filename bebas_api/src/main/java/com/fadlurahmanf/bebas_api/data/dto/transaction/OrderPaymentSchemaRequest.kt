package com.fadlurahmanf.bebas_api.data.dto.transaction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderPaymentSchemaRequest(
    val providerProductCode: String,
    val paymentTypeCode: String,
    val paymentConfigGroupId: String,
    val paymentSourceSchema: List<PaymentSourceSchemaRequest>,
    val customerNumber: String,
    val customerName: String,
) : Parcelable {
    @Parcelize
    data class PaymentSourceSchemaRequest(
        val code: String,
        val accountNumber: String? = null,
        val status: Boolean = true,
        val type: String,
    ) : Parcelable
}
