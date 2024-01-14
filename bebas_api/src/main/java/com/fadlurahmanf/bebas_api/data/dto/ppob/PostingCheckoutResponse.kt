package com.fadlurahmanf.bebas_api.data.dto.ppob

import com.google.gson.annotations.SerializedName

data class PostingCheckoutResponse(
    val transactionId: String,
    val transactionReference: String,
    val transactionStatus: String,
    @SerializedName("transactionDateTime")
    val utcTransactionDateTime: String,
)
