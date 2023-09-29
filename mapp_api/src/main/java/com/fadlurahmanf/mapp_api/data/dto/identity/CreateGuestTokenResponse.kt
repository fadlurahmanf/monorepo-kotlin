package com.fadlurahmanf.mapp_api.data.dto.identity

data class CreateGuestTokenResponse(
    var accessToken: String? = null,
    var expiresIn: Int? = null
)
