package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryPulsaDataResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryTelkomIndihomeResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.posting.PostingPulsaPrePaidRequest
import com.fadlurahmanf.bebas_api.data.dto.transaction.posting.PostingTelkomIndihomeRequest
import com.fadlurahmanf.bebas_api.data.dto.others.PulsaDenomResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.checkout.CheckoutTransactionDataRequest
import com.fadlurahmanf.bebas_api.data.dto.transaction.posting.FundTransferBankMASRequest
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryBankResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class PinVerificationArgument(
    val additionalTransfer: Transfer? = null,
    val additionalPulsaData: PulsaData? = null,
    val additionalTelkomIndihome: TelkomIndihome? = null,
    val additionalPlnPrePaidCheckout: PLNPrePaidCheckout? = null,
) : Parcelable {
    @Parcelize
    data class Transfer(
        val request: FundTransferBankMASRequest,
        val inquiry: InquiryBankResponse,
    ) : Parcelable

    @Parcelize
    data class PulsaData(
        val pulsaDenomClicked: PulsaDenomResponse,
        val postingRequest: PostingPulsaPrePaidRequest,
        val inquiryResponse: InquiryPulsaDataResponse,
    ) : Parcelable

    @Parcelize
    data class TelkomIndihome(
        val postingRequest: PostingTelkomIndihomeRequest,
        val inquiryResponse: InquiryTelkomIndihomeResponse,
    ) : Parcelable

    @Parcelize
    data class PLNPrePaidCheckout(
        val dataRequest: CheckoutTransactionDataRequest,
    ) : Parcelable
}
