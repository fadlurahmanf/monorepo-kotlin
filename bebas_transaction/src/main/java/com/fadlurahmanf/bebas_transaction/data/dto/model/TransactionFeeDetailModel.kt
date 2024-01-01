package com.fadlurahmanf.bebas_transaction.data.dto.model

import androidx.annotation.StyleRes

data class TransactionFeeDetailModel(
    val label: String,
    val value: Double,
    @StyleRes val valueStyle: Int? = null
)
