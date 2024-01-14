package com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InquiryCheckoutFlowResponse(
    val productCode: String? = null,
    val clientNumber: String? = null,
    val clientName: String? = null,
    val amount: Double? = null,
    val additionalInfo: List<AdditionalInfo>? = null,
) : Parcelable {
    @Parcelize
    data class AdditionalInfo(
        @SerializedName("name")
        val label: String? = null,
        val value: String? = null,
    ) : Parcelable
}
