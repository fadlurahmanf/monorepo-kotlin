package com.fadlurahmanf.bebas_transaction.data.dto.model.transfer

import com.fadlurahmanf.bebas_api.data.dto.transaction.posting.PostingCheckoutResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.posting.PostingPulsaDataResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.posting.PostingTelkomIndihomeResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.posting.PostingFundTransferResponse

data class PostingPinVerificationResultModel(
    val transactionStatus: String,
    val tranferBankMas: PostingFundTransferResponse? = null,
    val pulsaPrePaid: PostingPulsaDataResponse? = null,
    val telkomIndihome: PostingTelkomIndihomeResponse? = null,
    val plnPrePaidCheckout: PostingCheckoutResponse? = null,
)