package com.fadlurahmanf.bebas_api.data.dto.bank_account

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BankAccountResponse(
    var accountName: String? = null,
    @SerializedName("accountNo")
    var accountNumber: String? = null,
    var productName: String? = null,
    var cardNumber: String? = null,
    var onlineClearedBalance: Double? = null,
    var workingBalance: Double? = null,
) : Parcelable
