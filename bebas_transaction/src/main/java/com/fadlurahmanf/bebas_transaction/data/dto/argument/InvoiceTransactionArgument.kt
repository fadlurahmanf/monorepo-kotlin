package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryPulsaDataResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryTelkomIndihomeResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.posting.PostingPulsaDataResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.posting.PostingTelkomIndihomeResponse
import com.fadlurahmanf.bebas_api.data.dto.others.PulsaDenomResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryBankResponse
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
        val fromAccountNumber: String,
        val destinationAccountName: String,
        val destinationBankNickName: String,
        val destinationAccountNumber: String,
        val nominal: Long,
        val inquiryResponse: InquiryBankResponse
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