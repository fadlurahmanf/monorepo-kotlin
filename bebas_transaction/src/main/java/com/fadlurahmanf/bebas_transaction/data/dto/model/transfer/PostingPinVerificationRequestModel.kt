package com.fadlurahmanf.bebas_transaction.data.dto.model.transfer

import com.fadlurahmanf.bebas_api.data.dto.transaction.posting.PostingPulsaPrePaidRequest
import com.fadlurahmanf.bebas_api.data.dto.transaction.posting.PostingTelkomIndihomeRequest
import com.fadlurahmanf.bebas_api.data.dto.transaction.checkout.CheckoutTransactionDataRequest
import com.fadlurahmanf.bebas_api.data.dto.transaction.posting.FundTransferBankMASRequest

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

    data class PostingPLNPrePaidCheckout(
        val postingData: CheckoutTransactionDataRequest
    ) : PostingPinVerificationRequestModel()
}
