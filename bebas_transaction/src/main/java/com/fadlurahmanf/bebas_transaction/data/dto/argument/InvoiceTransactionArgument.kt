package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryPulsaDataResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryTelkomIndihomeResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.PostingPulsaDataResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.PostingTelkomIndihomeRequest
import com.fadlurahmanf.bebas_api.data.dto.ppob.PostingTelkomIndihomeResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.PulsaDenomResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class InvoiceTransactionArgument(
    val transactionId: String,
    var statusTransaction: String,
    var transactionDate: String,
    var isFavorite: Boolean,
    val isFavoriteEnabled: Boolean,
    val additionalTransfer: Transfer? = null,
    val additionalPulsaData: PulsaData? = null,
    val additionalTelkomIndihome: TelkomIndihome? = null,
) : Parcelable {
    @Parcelize
    data class Transfer(
        val destinationAccountName: String,
        val destinationBankNickName: String,
        val destinationAccountNumber: String,
        val nominal: Long
    ) : Parcelable

    @Parcelize
    data class PulsaData(
        val phoneNumber: String,
        val fromAccount: String,
        val totalTransaction: Double,

        val pulsaDenomClicked: PulsaDenomResponse,
        val postingResponse: PostingPulsaDataResponse,
        val inquiryResponse: InquiryPulsaDataResponse,
    ) : Parcelable

    @Parcelize
    data class TelkomIndihome(
        val destinationAccountName: String,
        val destinationAccountNumber: String,
        val totalTransaction: Double,
        val fromAccount: String,

        val postingResponse: PostingTelkomIndihomeResponse,
        val inquiryResponse: InquiryTelkomIndihomeResponse,
    ) : Parcelable
}