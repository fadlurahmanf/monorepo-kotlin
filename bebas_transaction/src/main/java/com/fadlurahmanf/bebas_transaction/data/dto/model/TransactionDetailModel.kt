package com.fadlurahmanf.bebas_transaction.data.dto.model

import androidx.annotation.StyleRes

data class TransactionDetailModel(
    val label: String,
    val value: String,
    @StyleRes val valueStyle: Int? = null
)
