package com.fadlurahmanf.bebas_transaction.data.dto

import com.fadlurahmanf.bebas_api.data.dto.favorite.LatestTransactionResponse

data class LatestTransactionModel(
    val name: String,
    val labelLatest: String,
    val accountNumber: String,

    val additionalTransferData: LatestTransactionResponse? = null
)
