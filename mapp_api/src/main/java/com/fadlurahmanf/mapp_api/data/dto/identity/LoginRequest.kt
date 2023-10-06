package com.fadlurahmanf.mapp_api.data.dto.identity

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    var nik: String? = null,
    var deviceId: String? = null,
    var deviceToken: String? = null,
    var phoneNumber: String? = null,
    var password: String? = null,
    var activationId: String? = null,
    @SerializedName("clientTimeMilis")
    var timestamp: String? = null,
)
