package com.fadlurahmanf.bebas_api.data.dto.transfer

import com.google.gson.annotations.SerializedName

data class GenerateChallengeCodeRequest<T>(
    val data: T,
    val timestamp: String,
    @SerializedName("transactionType")
    val t1ransactionType: String,
)
