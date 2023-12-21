package com.fadlurahmanf.bebas_api.data.dto.transfer

import com.google.gson.annotations.SerializedName

data class GenerateChallengeCodeRequest<T>(
    val data: T,
    @SerializedName("transactionType")
    val type: String,
    val timestamp: String
)
