package com.fadlurahmanf.bebas_api.data.dto.demography

import com.google.gson.annotations.SerializedName

data class CityResponse(
    @SerializedName("recid")
    var id: String? = null,
    @SerializedName("description")
    var name: String? = null,
)
