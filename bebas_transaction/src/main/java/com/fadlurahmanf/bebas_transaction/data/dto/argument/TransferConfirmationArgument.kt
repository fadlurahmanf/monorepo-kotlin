package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import androidx.annotation.StyleRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransferConfirmationArgument(
    val realAccountName: String,
    val destinationAccountNumber: String,
    var imageLogoUrl: String? = null,
    val bankNickName: String,
    val nominal: Double,
    val details: ArrayList<Detail> = arrayListOf()
) : Parcelable {
    @Parcelize
    data class Detail(
        val label: String,
        val value: String,
        @StyleRes val valueStyle: Int? = null
    ) : Parcelable
}
