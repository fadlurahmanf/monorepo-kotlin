package com.fadlurahmanf.bebas_api.data.dto.transaction.posting

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostingPulsaDataResponse(
    var transactionId: String? = null,
    var transactionDateTime: String? = null,
    var serialNumber: String? = null,
) : Parcelable
