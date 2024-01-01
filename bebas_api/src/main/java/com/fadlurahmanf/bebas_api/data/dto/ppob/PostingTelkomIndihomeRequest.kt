package com.fadlurahmanf.bebas_api.data.dto.ppob

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostingTelkomIndihomeRequest(
    val additionalDataPrivate: String,
    val amountTransaction: Double,
    val autodebitStatus: Boolean,
    val customerNumber: String,
    val deviceOs: String = "",
    val fromAccount: String,
    val fromAccountName: String,
    val ip: String = "0.0.0.0",
    val language: String = "id",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val transactionFee: Double
) : Parcelable
