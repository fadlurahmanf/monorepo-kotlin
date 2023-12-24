package com.fadlurahmanf.bebas_api.data.dto.notification

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("content")
    val contents: List<Content>? = null,
    @SerializedName("last")
    val isLast: Boolean? = null
) {
    data class Content(
        val messageId: String? = null
    )
}
