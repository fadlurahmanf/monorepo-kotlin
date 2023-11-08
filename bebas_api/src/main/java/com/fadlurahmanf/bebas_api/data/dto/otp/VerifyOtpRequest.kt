package com.fadlurahmanf.bebas_api.data.dto.otp

data class VerifyOtpRequest(
    val phoneNumber: String,
    val deviceId: String,
    val otp: String,
)
