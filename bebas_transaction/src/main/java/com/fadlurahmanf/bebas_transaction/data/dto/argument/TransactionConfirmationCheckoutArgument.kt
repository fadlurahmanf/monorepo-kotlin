package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryCheckoutFlowResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionConfirmationCheckoutArgument(
    val destinationLabel: String,
    val destinationSubLabel: String,
    var imageLogoUrl: String? = null,
    val additionalPLNPrePaid: PLNPrePaid? = null
) : Parcelable {
    @Parcelize
    data class PLNPrePaid(
        val paymentTypeCode: String,
        val inquiryResponse: InquiryCheckoutFlowResponse,
    ) : Parcelable
}
