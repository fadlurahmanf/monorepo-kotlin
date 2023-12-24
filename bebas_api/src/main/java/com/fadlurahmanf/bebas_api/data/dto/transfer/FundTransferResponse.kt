package com.fadlurahmanf.bebas_api.data.dto.transfer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class FundTransferResponse(
    var transactionId: String? = null,
    var correlationId: String? = null,
    var referenceCode: String? = null,
    var transactionDateTime: String? = null,
)
