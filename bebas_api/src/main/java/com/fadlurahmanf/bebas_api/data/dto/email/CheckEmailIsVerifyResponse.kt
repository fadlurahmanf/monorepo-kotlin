package com.fadlurahmanf.bebas_api.data.dto.email

data class CheckEmailIsVerifyResponse(
    var isVerify: Boolean? = null,
    var emailToken: String? = null
)
