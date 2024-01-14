package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import androidx.annotation.StyleRes
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryCheckoutFlowResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionConfirmationCheckoutArgument(
    val destinationLabel: String,
    val destinationSubLabel: String,
    val imageLogoUrl: String? = null,
    val details: ArrayList<Detail> = arrayListOf(),
    val additionalPLNPrePaid: PLNPrePaid? = null
) : Parcelable {
    @Parcelize
    data class PLNPrePaid(
        val paymentTypeCode: String,
        val inquiryResponse: InquiryCheckoutFlowResponse,
    ) : Parcelable

    @Parcelize
    data class Detail(
        val label: String,
        val value: String,
        @StyleRes val valueStyle: Int? = null,
    ) : Parcelable
}
