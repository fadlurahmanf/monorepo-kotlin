package com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InquiryTvCableResponse(
    val customerName: String? = null,
    val customerNumber: String? = null,
    val periode: String? = null,
    val amountTransactionFee: Double? = null,
    val additionalDataPrivate: String? = null,
    val operationalHourStart: String? = null,
    val operationalHourEnd: String? = null,
    val autodebitStatus: Boolean? = null,
) : Parcelable
