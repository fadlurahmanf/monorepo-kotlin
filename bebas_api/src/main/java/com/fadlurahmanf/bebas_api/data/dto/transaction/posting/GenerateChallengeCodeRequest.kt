package com.fadlurahmanf.bebas_api.data.dto.transaction.posting

data class GenerateChallengeCodeRequest<T>(
    val data: T,
    val timestamp: String,
    val transactionType: String,
)
