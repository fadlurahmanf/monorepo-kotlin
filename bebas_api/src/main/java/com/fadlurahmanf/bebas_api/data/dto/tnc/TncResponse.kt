package com.fadlurahmanf.bebas_api.data.dto.tnc

import com.google.gson.annotations.SerializedName

data class TncResponse(
    @SerializedName(value = "value")
    val text: String? = null
)
