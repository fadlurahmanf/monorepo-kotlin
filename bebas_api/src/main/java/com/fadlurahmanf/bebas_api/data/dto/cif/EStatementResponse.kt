package com.fadlurahmanf.bebas_api.data.dto.cif

import com.google.gson.annotations.SerializedName

data class EStatementResponse(
    val statements: List<Statement>? = null,
    val account: Account? = null
) {
    data class Statement(
        val year: Int? = null,
        val month: Int? = null
    )

    data class Account(
        @SerializedName("holderName")
        val accountName: String? = null,
        @SerializedName("number")
        val accountNumber: String? = null,
        val openDate: String? = null,
    )
}
