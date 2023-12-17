package com.fadlurahmanf.bebas_api.data.dto.transfer

import com.google.gson.annotations.SerializedName

data class BankResponse(
    val sknId: String? = null,
    val rtgsId: String? = null,
    @SerializedName("bankName")
    val name: String? = null,
    @SerializedName("bankNickName")
    val nickName: String? = null,
    @SerializedName("bankLogoUrl")
    val image: String? = null,
    val order: Int? = null
)
