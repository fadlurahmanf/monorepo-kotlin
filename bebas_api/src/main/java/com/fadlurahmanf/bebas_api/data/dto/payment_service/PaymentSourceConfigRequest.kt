package com.fadlurahmanf.bebas_api.data.dto.payment_service

data class PaymentSourceConfigRequest(
    val paymentTypeCode: String,
    val amount: Double,
)
