package com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InquiryBankResponse(
    var destinationAccountName: String? = null
) : Parcelable
