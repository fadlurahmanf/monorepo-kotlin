package com.fadlurahmanf.bebas_transaction.data.dto.model.transfer

import com.fadlurahmanf.bebas_api.data.dto.ppob.PostingPulsaDataResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.PostingTelkomIndihomeResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.PostingFundTransferResponse

data class PostingPinVerificationResultModel(
    val transactionStatus: String,
    val tranferBankMas: PostingFundTransferResponse? = null,
    val pulsaPrePaid: PostingPulsaDataResponse? = null,
    val telkomIndihome: PostingTelkomIndihomeResponse? = null,
)