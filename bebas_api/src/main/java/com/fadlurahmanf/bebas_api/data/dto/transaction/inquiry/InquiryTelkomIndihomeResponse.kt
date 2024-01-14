package com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InquiryTelkomIndihomeResponse(
    val customerName: String? = null,
    val customerNumber: String? = null,
    val periode: String? = null,
    val additionalDataPrivate: String? = null,
    val amountTransaction: Double? = null,
    val transactionFee: Double? = null,
    val autoDebitStatus: Boolean = false,
) : Parcelable
