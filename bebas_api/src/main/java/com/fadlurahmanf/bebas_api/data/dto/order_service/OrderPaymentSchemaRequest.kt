package com.fadlurahmanf.bebas_api.data.dto.order_service

data class OrderPaymentSchemaRequest(
    val providerProductCode: String,
    val paymentTypeCode: String,
    val paymentConfigGroupId: String,
    val paymentSourceSchema: List<PaymentSourceSchemaRequest>,
    val customerNumber: String,
    val customerName: String,
) {
    data class PaymentSourceSchemaRequest(
        val code: String,
        val accountNumber: String ?= null,
        val status: Boolean = true,
    )
}
