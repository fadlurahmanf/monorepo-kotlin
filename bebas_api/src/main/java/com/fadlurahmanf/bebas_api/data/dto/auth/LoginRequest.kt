package com.fadlurahmanf.bebas_api.data.dto.auth

data class LoginRequest(
    val nik:String,
    val deviceId:String,
    val deviceToken:String,
    val password:String,
    val phoneNumber:String,
    val clientTimeMilis:String = System.currentTimeMillis().toString(),
    val activationId:String
)
