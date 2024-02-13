package com.fadlurahmanf.bebas_loyalty.data.dto

import com.fadlurahmanf.bebas_api.data.dto.loyalty.HistoryLoyaltyResponse

data class HistoryLoyaltyModel(
    var id: String,
    var header: String,
    var topLabel: String,
    var subLabel: String,

    // type = 0 -> HEADER
    // type = 1 -> CONTENT
    var type: Int = 1,
    var response: HistoryLoyaltyResponse? = null
)
