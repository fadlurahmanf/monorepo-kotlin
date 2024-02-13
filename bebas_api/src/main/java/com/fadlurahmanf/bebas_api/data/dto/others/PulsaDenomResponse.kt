package com.fadlurahmanf.bebas_api.data.dto.others

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PulsaDenomResponse(
    val value: Double? = null,
    val nominal: Double? = null,
    val adminFee: Double? = null,
    val productCode: String? = null,
    val productDescription: String? = null,
    val billerCode: String? = null,
    val isAvailable: Boolean? = null,
    val paymentTypeCode: String? = null,
) : Parcelable
