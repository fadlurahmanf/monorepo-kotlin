package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryTelkomIndihomeResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.PostingPulsaPrePaidRequest
import com.fadlurahmanf.bebas_api.data.dto.ppob.PostingTelkomIndihomeRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferBankMASRequest
import kotlinx.parcelize.Parcelize

@Parcelize
data class PinVerificationArgument(
    val fundTransferBankMAS: FundTransferBankMASRequest? = null,
    val pulsaPrePaidRequest: PostingPulsaPrePaidRequest? = null,
    val additionalTelkomIndihome: TelkomIndihome? = null
) : Parcelable {
    @Parcelize
    data class TelkomIndihome(
        val postingRequest: PostingTelkomIndihomeRequest,
        val inquiryResponse: InquiryTelkomIndihomeResponse,
    ) : Parcelable
}
