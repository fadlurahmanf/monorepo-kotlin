package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import androidx.annotation.StyleRes
import com.fadlurahmanf.bebas_transaction.data.dto.model.PaymentSourceModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransferConfirmationArgument(
    val defaultPaymentSource: PaymentSourceModel? = null,
    val realAccountName: String,
    val destinationAccountNumber: String,
    var imageLogoUrl: String? = null,
    val bankNickName: String,
    val nominal: Long,
    val details: ArrayList<Detail> = arrayListOf()
) : Parcelable {
    @Parcelize
    data class Detail(
        val label: String,
        val value: String,
        @StyleRes val valueStyle: Int? = null
    ) : Parcelable
}
