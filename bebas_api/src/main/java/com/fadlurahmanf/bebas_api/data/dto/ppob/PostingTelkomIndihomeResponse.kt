package com.fadlurahmanf.bebas_api.data.dto.ppob

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostingTelkomIndihomeResponse(
    var transactionId: String? = null,
    var transactionDateTime: String? = null,
    var autoDebitStatus: Boolean = false,
) : Parcelable
