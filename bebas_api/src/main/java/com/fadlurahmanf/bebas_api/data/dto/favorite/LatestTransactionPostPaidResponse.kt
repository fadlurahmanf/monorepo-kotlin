package com.fadlurahmanf.bebas_api.data.dto.favorite

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LatestTransactionPostPaidResponse(
    val userAccountId: String? = null,
    val billingCategory: String? = null,
    val providerName: String? = null,
    val billPaymentNumber: String? = null,
    val transactionDate: String? = null,
    val transactionCurrency: String? = null,
    val billedAmount: String? = null,
    val additionalInfo: String? = null,
    val transactionStatus: String? = null,
    val customerName: String? = null,
    val adminFeeRetail: String? = null,
    val correlationId: String? = null,
    val referenceCode: String? = null,
    val accountName: String? = null,
    val id: String? = null,
    val aliasName: String? = null,
    val isFavorite: Boolean? = null,
    val providerLogo: String? = null,
) : Parcelable
