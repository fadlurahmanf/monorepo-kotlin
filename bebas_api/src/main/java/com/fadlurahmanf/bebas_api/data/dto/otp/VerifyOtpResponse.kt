package com.fadlurahmanf.bebas_api.data.dto.otp

data class VerifyOtpResponse(
    var isVerify: Boolean? = null,
    var message: String? = null,
    var otpToken: String? = null,
)
