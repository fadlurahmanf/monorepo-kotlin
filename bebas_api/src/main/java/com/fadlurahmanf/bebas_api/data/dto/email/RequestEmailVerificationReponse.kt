package com.fadlurahmanf.bebas_api.data.dto.email

data class RequestEmailVerificationReponse(
    var status: Boolean? = null,
    var requestAttempt: Int? = null,
    var verifyAttempt: Int? = null,
    var requestDate: String? = null,
    var lastRequestDate: String? = null,
)
