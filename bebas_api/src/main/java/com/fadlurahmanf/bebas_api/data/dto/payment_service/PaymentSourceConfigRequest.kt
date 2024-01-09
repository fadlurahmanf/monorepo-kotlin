package com.fadlurahmanf.bebas_api.data.dto.payment_service

import com.google.gson.annotations.SerializedName

data class PaymentSourceConfigRequest(
    @SerializedName("payment_type_code")
    val paymentTypeCode: String,
    val amount: Double,
)
