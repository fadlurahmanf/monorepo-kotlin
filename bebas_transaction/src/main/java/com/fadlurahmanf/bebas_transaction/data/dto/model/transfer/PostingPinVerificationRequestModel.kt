package com.fadlurahmanf.bebas_transaction.data.dto.model.transfer

import com.fadlurahmanf.bebas_api.data.dto.ppob.PostingPulsaPrePaidRequest
import com.fadlurahmanf.bebas_api.data.dto.ppob.PostingTelkomIndihomeRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferBankMASRequest

sealed class PostingPinVerificationRequestModel() {
    data class FundTranfeerBankMas(
        val fundTransferBankMASRequest: FundTransferBankMASRequest,
    ) : PostingPinVerificationRequestModel()

    data class PostingPulsaPrePaid(
        val postingPulsaPrePaidRequest: PostingPulsaPrePaidRequest
    ) : PostingPinVerificationRequestModel()

    data class PostingTelkomIndihome(
        val postingTelkomIndihomeRequest: PostingTelkomIndihomeRequest
    ) : PostingPinVerificationRequestModel()
}
