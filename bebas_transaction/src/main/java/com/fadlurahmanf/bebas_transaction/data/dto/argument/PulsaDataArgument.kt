package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryPulsaDataResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class PulsaDataArgument(
    var providerName: String,
    var providerImage: String? = null,
    var phoneNumber: String,

    var inquiry: InquiryPulsaDataResponse,
) : Parcelable
