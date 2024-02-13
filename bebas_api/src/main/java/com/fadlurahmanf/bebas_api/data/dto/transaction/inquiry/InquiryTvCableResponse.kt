package com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class InquiryTvCableResponse(
    val customerName: String? = null,
    val customerNumber: String? = null,
    @SerializedName("period")
    val periode: String? = null,
    val amountTransactionFee: Double? = null,
    val amountTransaction: Double? = null,
    val additionalDataPrivate: String? = null,
    val autodebitStatus: Boolean? = null,
) : Parcelable
