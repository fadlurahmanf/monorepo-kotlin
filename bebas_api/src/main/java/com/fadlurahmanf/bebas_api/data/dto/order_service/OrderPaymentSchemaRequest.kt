package com.fadlurahmanf.bebas_api.data.dto.order_service

data class OrderPaymentSchemaRequest(
    val providerProductCode: String? = null,
    val paymentTypeCode: String? = null,
    val paymentConfigGroupId: String? = null,
    val paymentSourceSchema: String? = null,
    val customerNumber: String? = null,
    val customerName: String? = null,
) {
    data class PaymentSourceSchemaRequest(
        val code: String,
        val accountNumber: String,
        val status: String,
    )
}
