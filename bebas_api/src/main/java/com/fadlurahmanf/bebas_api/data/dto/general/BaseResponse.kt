package com.fadlurahmanf.bebas_api.data.dto.general

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("code")
    var code:String ?= null,
    @SerializedName("message")
    var message:String ?= null,
    @SerializedName("data")
    var data: T ?= null
)
