package com.fadlurahmanf.mapp_api.data.dto.identity

data class AuthResponse(
    var accessToken: String? = null,
    var refreshToken: String? = null,
    var expiresIn: Int? = null,
    var refreshExpiresIn: Int? = null,
)
