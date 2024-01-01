package com.fadlurahmanf.bebas_transaction.data.dto.model.transfer

import com.fadlurahmanf.bebas_api.data.dto.ppob.PostingTelkomIndihomeResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.FundTransferResponse

data class PostingPinVerificationResultModel(
    val tranferBankMas: FundTransferResponse? = null,
    val pulsaPrePaid: FundTransferResponse? = null,
    val telkomIndihome: PostingTelkomIndihomeResponse? = null,
)