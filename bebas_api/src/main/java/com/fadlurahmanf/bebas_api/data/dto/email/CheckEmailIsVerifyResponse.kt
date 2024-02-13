package com.fadlurahmanf.bebas_api.data.dto.email

import com.google.gson.annotations.SerializedName

data class CheckEmailIsVerifyResponse(
    var isVerify: Boolean? = null,
    var emailToken: String? = null,
    @SerializedName("id")
    var onboardingId: String? = null,
)
