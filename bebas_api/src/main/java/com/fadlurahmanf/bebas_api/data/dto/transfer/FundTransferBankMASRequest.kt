package com.fadlurahmanf.bebas_api.data.dto.transfer

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FundTransferBankMASRequest(
    val accountNumber: String,
    val amountTransaction: Long,
    val description: String,
    val destinationAccountName: String,
    val destinationAccountNumber: String,
    val ip: String,
    val latitude: Double,
    val longitude: Double,
) : Parcelable
