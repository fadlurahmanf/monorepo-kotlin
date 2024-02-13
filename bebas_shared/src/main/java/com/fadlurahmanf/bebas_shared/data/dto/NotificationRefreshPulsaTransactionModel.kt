package com.fadlurahmanf.bebas_shared.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationRefreshPulsaTransactionModel(
    val transactionStatus: String? = null,
    val description: String? = null,
    val language: String? = null,
    val productName: String? = null,
    val phoneNumber: String? = null,
    val provider: String? = null,
    val adminFee: Double? = null,
    val status: String? = null,
    val providerLogo: String? = null,
    val prepaidCategory: String? = null,
    val type: String? = null,
    val serialNumber: String? = null,
    val fromAccountNo: String? = null,
    val transactionId: String? = null,
    val transactionDate: String? = null,
    val transactionType: String? = null,
    val nominal: String? = null,
) : Parcelable
