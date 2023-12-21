package com.fadlurahmanf.bebas_api.data.dto.transfer

import com.google.gson.annotations.SerializedName

data class PostingRequest<T>(
    val data: T,
    val signature: String,
    @SerializedName("clientTimeMillis")
    val timestamp: String
)
