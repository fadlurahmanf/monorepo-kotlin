package com.fadlurahmanf.bebas_api.data.dto.favorite

import com.google.gson.annotations.SerializedName

data class LatestTransactionResponse(
    val id: String? = null,
    val userAccountId: String? = null,
    val sknId: String? = null,
    val rtgsId: String? = null,
    @SerializedName("counterpartBankNickname")
    val bankName: String? = null,
    @SerializedName("counterpartAccountNumber")
    val accountNumber: String? = null,
    @SerializedName("counterPartAccountName")
    val accountName: String? = null,
)
