package com.fadlurahmanf.bebas_api.data.dto.auth

import com.google.gson.annotations.SerializedName

data class RefreshUserTokenRequest(
    @SerializedName("token")
    var refreshToken: String
)
