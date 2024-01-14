package com.fadlurahmanf.bebas_api.data.dto.transfer

data class CheckoutGenerateChallengeCodeRequest(
    val data: CheckoutTransactionDataRequest,
    val timestamp: String,
)
