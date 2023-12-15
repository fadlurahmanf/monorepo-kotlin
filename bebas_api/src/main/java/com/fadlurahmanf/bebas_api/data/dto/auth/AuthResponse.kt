package com.fadlurahmanf.bebas_api.data.dto.auth

data class AuthResponse(
    var accessToken: String? = null,
    var refreshToken: String? = null,
    var expiresIn: Int? = null,
    var refreshExpiresIn: Int? = null,
)