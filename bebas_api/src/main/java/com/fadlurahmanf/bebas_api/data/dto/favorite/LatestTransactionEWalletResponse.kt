package com.fadlurahmanf.bebas_api.data.dto.favorite

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LatestTransactionEWalletResponse(
    val userAccountId: String? = null,
    val userAccountNumber: String? = null,
    val providerName: String? = null,
    val ewalletAccountNumber: String? = null,
    val ewalletAccountName: String? = null,
    val transactionDate: String? = null,
    val transactionCurrency: String? = null,
    val totalPayment: Double? = null,
    val providerFee: Double? = null,
    val isProviderFeeDeducted: Boolean? = null,
    val isProviderUsingOwnFee: Boolean? = null,
    val bankFee: Double? = null,
    val aggregatorFee: Double? = null,
    val additionalInfo: String? = null,
    val transactionId: String? = null,
    val transactionReference: String? = null,
    val cifNumber: String? = null,
    val phoneNumber: String? = null,
    val referenceCode: String? = null,
    val correlationId: String? = null,
    val transactionStatusDescription: String? = null,
    val id: String? = null,
    val aliasName: String? = null,
    val isFavorite: Boolean? = null,
) : Parcelable
