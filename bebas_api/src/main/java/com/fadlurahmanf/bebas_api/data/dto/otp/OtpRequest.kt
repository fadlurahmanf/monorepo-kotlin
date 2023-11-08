package com.fadlurahmanf.bebas_api.data.dto.otp

data class OtpRequest(
    val phoneNumber: String,
    val deviceId: String,
    val shouldSendOtp: Boolean = true,
)
