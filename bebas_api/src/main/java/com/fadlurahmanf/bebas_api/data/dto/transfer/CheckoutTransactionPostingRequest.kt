package com.fadlurahmanf.bebas_api.data.dto.transfer

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = false)
data class CheckoutTransactionPostingRequest(
    val data: CheckoutTransactionDataRequest,
    val signature: String,
    val clientTimeMillis: String,
)
