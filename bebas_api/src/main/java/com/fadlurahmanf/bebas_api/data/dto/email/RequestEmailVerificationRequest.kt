package com.fadlurahmanf.bebas_api.data.dto.email

data class RequestEmailVerificationRequest(
    val email: String,
    val phoneNumber: String,
    val otpToken: String,
    val deviceId: String,
    val shouldSendEmail: Boolean,
    val flowType: String
)
