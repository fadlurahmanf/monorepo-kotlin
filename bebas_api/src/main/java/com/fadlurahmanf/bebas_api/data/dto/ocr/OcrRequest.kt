package com.fadlurahmanf.bebas_api.data.dto.ocr

import com.google.gson.annotations.SerializedName

data class OcrRequest(
    @SerializedName("id")
    val onboardingId: String,
    val emailToken: String,
    val email: String,
    val deviceId: String,
    val startDate: String = System.currentTimeMillis().toString(),
    @SerializedName("image")
    val base64Image: String,
)
