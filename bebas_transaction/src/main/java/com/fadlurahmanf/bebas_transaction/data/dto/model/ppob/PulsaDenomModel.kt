package com.fadlurahmanf.bebas_transaction.data.dto.model.ppob

import com.fadlurahmanf.bebas_api.data.dto.ppob.PulsaDenomResponse

data class PulsaDenomModel(
    val total: Double,
    val denom: Double,
    var providerImage: String? = null,

    var pulsaDenomResponse: PulsaDenomResponse? = null
)
