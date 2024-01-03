package com.fadlurahmanf.bebas_loyalty.data.dto

import com.fadlurahmanf.bebas_api.data.dto.loyalty.HistoryLoyaltyResponse

data class HistoryLoyaltyModel(
    var id: String,
    var header: String,
    var body: String,
    var point: Int,

    var response: HistoryLoyaltyResponse? = null
)
