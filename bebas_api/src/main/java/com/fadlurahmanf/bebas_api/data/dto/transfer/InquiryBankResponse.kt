package com.fadlurahmanf.bebas_api.data.dto.transfer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InquiryBankResponse(
    var destinationAccountName: String? = null
) : Parcelable
