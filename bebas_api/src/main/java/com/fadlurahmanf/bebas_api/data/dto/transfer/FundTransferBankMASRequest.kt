package com.fadlurahmanf.bebas_api.data.dto.transfer

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FundTransferBankMASRequest(
    val accountNumber: String,
    val amountTransaction: Long,
    val notes: String,
    val destinationAccountName: String,
    val destinationAccountNumber: String,
    val ip: String,
    val latitude: Double,
    val longitude: Double,
) : Parcelable
