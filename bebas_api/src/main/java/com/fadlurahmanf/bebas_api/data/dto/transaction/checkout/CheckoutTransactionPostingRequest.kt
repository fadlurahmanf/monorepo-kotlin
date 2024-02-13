package com.fadlurahmanf.bebas_api.data.dto.transaction.checkout

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class CheckoutTransactionPostingRequest(
    val data: CheckoutTransactionDataRequest,
    val signature: String,
    @SerializedName("clientTimeMillis")
    val timestamp: String,
)
