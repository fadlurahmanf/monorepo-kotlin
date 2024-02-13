package com.fadlurahmanf.bebas_api.data.dto.others

import com.google.gson.annotations.SerializedName

data class PLNDenomResponse(
    @SerializedName("value")
    val denom: Double? = null,
    val nominal: Double? = null,
    val adminFee: Double? = null,
    val productCode: String? = null,
    val productDescription: String? = null,
    val billerCode: String? = null,
    val isAvailable: Boolean? = null,
    val paymentTypeCode: String? = null,
)
