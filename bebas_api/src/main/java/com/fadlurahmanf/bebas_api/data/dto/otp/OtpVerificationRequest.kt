package com.fadlurahmanf.bebas_api.data.dto.otp

data class OtpVerificationRequest(
    val phoneNumber: String,
    val deviceId: String,
    val shouldSendOtp: Boolean = true,
)
