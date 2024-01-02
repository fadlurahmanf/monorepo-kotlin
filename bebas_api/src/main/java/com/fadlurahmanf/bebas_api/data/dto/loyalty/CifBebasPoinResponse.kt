package com.fadlurahmanf.bebas_api.data.dto.loyalty

import com.google.gson.annotations.SerializedName

data class CifBebasPoinResponse(
    @SerializedName("points")
    val point: Int? = null,
    @SerializedName("is_onboarded")
    val isOnBoarded: Boolean? = null
)
