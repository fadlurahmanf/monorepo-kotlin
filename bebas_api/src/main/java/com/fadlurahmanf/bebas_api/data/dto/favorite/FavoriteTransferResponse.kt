package com.fadlurahmanf.bebas_api.data.dto.favorite

import com.google.gson.annotations.SerializedName

data class FavoriteTransferResponse(
    @SerializedName("aliasName")
    var nameInFavorite: String? = null,
    var bankName: String? = null,
    @SerializedName("accountNumber")
    var bankAccountNumber: String? = null,
    var sknId: String? = null,
    var rtgsId: String? = null,
)
