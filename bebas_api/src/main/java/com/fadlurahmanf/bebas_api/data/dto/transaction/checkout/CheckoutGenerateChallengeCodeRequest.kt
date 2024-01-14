package com.fadlurahmanf.bebas_api.data.dto.transaction.checkout

data class CheckoutGenerateChallengeCodeRequest(
    val data: CheckoutTransactionDataRequest,
    val timestamp: String,
)
