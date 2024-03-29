package com.fadlurahmanf.bebas_transaction.data.dto.model.ppob

import com.fadlurahmanf.bebas_api.data.dto.others.PLNDenomResponse
import com.fadlurahmanf.bebas_api.data.dto.others.PulsaDenomResponse
import com.fadlurahmanf.bebas_transaction.data.flow.PPOBDenomFlow

data class PPOBDenomModel(
    val flow: PPOBDenomFlow,
    val id: String,
    val totalBayar: Double,
    val nominal: Double,
    var imageUrl: String? = null,
    var isSelected: Boolean = false,
    var isAvailable: Boolean = true,

    var pulsaDenomResponse: PulsaDenomResponse? = null,
    var plnPrePaidDenomResponse: PLNDenomResponse? = null,
)
