package com.fadlurahmanf.bebas_api.data.dto.favorite

import com.google.gson.annotations.SerializedName

data class FavoriteTransferResponse(
    val id: String? = null,
    val aliasName: String? = null,
    val bankName: String? = null,
    @SerializedName("accountNumber")
    val bankAccountNumber: String? = null,
    val sknId: String? = null,
    val rtgsId: String? = null,
    val isPinned: Boolean? = null,
)
