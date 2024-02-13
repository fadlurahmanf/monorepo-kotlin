package com.fadlurahmanf.bebas_api.data.dto.favorite

import com.google.gson.annotations.SerializedName

data class PinFavoriteRequest(
    val id: String,
    @SerializedName("pin")
    val isPinned: Boolean = false
)
