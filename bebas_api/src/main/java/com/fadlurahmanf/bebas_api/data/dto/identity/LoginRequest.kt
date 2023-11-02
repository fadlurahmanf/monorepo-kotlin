package com.fadlurahmanf.bebas_api.data.dto.identity

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    var nik: String,
    var deviceId: String,
    var deviceToken: String,
    var phoneNumber: String,
    var password: String,
    var activationId: String,
    @SerializedName("clientTimeMilis")
    var timestamp: String,
)
