package com.fadlurahmanf.bebas_api.data.dto.identity

data class CreateGuestTokenResponse(
    var accessToken: String? = null,
    var expiresIn: Int? = null
)
