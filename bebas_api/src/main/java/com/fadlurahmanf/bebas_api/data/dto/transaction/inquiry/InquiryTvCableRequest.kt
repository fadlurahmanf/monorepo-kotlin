package com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InquiryTvCableRequest(
    val fromAccount: String,
    val accountId: String,
    val providerName: String,
) : Parcelable
