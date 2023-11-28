package com.fadlurahmanf.bebas_api.data.dto.otp

data class OtpResponse(
    var requestAttempt: Int? = null,
    var verifyAttempt: Int? = null,
    var requestDate: String? = null,
    var lastRequestDate: String? = null,
)
