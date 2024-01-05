package com.fadlurahmanf.bebas_transaction.data.dto.model.favorite

import com.fadlurahmanf.bebas_api.data.dto.favorite.LatestTransactionPostPaidResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.LatestTransactionPulsaPrePaidResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.LatestTransactionResponse

data class LatestTransactionModel(
    val label: String,
    val subLabel: String,
    val accountNumber: String,

    val additionalTransferData: LatestTransactionResponse? = null,
    val additionalPLNPrePaid: LatestTransactionResponse? = null,
    val additionalPLNPostPaid: LatestTransactionPostPaidResponse? = null,
    val additionalTelkomIndihome: LatestTransactionPostPaidResponse? = null,
    val additionalPulsaPrePaid: LatestTransactionPulsaPrePaidResponse? = null,
)
