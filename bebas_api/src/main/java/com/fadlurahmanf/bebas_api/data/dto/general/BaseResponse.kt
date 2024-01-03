package com.fadlurahmanf.bebas_api.data.dto.general

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("code")
    val code:String ?= null,
    @SerializedName("message")
    val message:String ?= null,
    @SerializedName("data")
    val data: T ?= null
)
