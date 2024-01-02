package com.fadlurahmanf.bebas_api.data.dto.ppob

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class InquiryPulsaDataResponse(
    val phoneNumber: String? = null,
    val providerName: String? = null,
    val imageProvider: String? = null
) : Parcelable
