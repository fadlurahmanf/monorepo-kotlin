package com.fadlurahmanf.bebas_api.data.dto.notification

import com.google.gson.annotations.SerializedName

data class UnreadNotificationCountResponse(
    val total: Int? = null,
    @SerializedName("detail")
    val details: List<Detail>? = null
) {
    data class Detail(
        val type: String? = null,
        val total: Int? = null
    )
}
