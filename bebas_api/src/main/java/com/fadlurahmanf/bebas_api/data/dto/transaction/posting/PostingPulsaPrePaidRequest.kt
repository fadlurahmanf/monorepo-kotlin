package com.fadlurahmanf.bebas_api.data.dto.transaction.posting

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PostingPulsaPrePaidRequest(
    val accountName: String,
    val accountNumber: String,
    val amount: Double,
    val billerCode: String,
    val ip: String = "0.0.0.0",
    val language: String = "id-ID",
    val latitude: Double = 0.0,
    val longitude: Double,
    val phoneNumber: String,
    val productCode: String,
    val providerName: String,
    val transactionFee: Double
) : Parcelable
