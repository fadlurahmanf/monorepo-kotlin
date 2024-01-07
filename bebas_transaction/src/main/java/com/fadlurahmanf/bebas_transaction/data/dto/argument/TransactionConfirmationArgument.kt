package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import androidx.annotation.StyleRes
import com.fadlurahmanf.bebas_transaction.data.dto.model.PaymentSourceModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionConfirmationArgument(
    val destinationLabel: String,
    val destinationSubLabel: String,
    var imageLogoUrl: String? = null,
    var feeDetail: FeeDetail,
    val defaultPaymentSource: PaymentSourceModel? = null,
    val details: ArrayList<Detail> = arrayListOf()
) : Parcelable {
    @Parcelize
    data class FeeDetail(
        val total: Double,
        val details: ArrayList<Detail> = arrayListOf()
    ) : Parcelable {
        @Parcelize
        data class Detail(
            val label: String,
            val value: Double,
        ) : Parcelable
    }

    @Parcelize
    data class Detail(
        val label: String,
        val value: String,
        @StyleRes val valueStyle: Int? = null,
        // contoh: nama alias di transaction confirmation
        val isShowOnlyInInquiry: Boolean = false,
    ) : Parcelable
}
