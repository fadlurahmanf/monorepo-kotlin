package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PulsaDataArgument(
    var providerName: String,
    var providerImage: String? = null
) : Parcelable
