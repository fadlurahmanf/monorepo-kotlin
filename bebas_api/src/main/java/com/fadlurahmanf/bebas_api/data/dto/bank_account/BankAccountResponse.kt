package com.fadlurahmanf.bebas_api.data.dto.bank_account

import com.google.gson.annotations.SerializedName

data class BankAccountResponse(
    var accountName: String? = null,
    @SerializedName("accountNo")
    var accountNumber: String? = null,
    var cardNumber: String? = null,
    var balance: Double? = null,
    @SerializedName("mainAccount")
    var isMainAccount: Boolean = false
)
