package com.fadlurahmanf.bebas_api.data.dto.email

data class CheckEmailIsVerifyRequest(
    val email: String,
    val deviceId: String,
    val deviceModel: String,
    val flowType: String,
    private val startDate: String = System.currentTimeMillis().toString(),
    val phoneNumber: String,
    val appVersion: String,
    val language: String,
    val voipToken: String? = null,
    val platform: String = "android",
)
