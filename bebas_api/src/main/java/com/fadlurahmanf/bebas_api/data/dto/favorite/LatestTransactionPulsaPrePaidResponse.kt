package com.fadlurahmanf.bebas_api.data.dto.favorite

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LatestTransactionPulsaPrePaidResponse(
    val userAccountId: String? = null,
    val userAccountNumber: String? = null,
    val prepaidCategory: String? = null,
    val providerName: String? = null,
    val prepaidNumber: String? = null,
    val transactionId: String? = null,
    val transactionDate: String? = null,
    val transactionCurrency: String? = null,
    val transactionFee: Double? = null,
    val additionalInfo: String? = null,
    val responseToken: String? = null,
    val transactionStatus: String? = null,
    val aggregatorName: String? = null,
    val aliasName: String? = null,
    val isFavorite: Boolean? = null,
    val providerLogo: String? = null,
) : Parcelable
