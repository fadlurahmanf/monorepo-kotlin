package com.fadlurahmanf.bebas_api.data.dto.notification

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("content")
    val contents: List<Content>? = null,
    @SerializedName("last")
    val isLast: Boolean? = null
) {
    data class Content(
        val messageId: String? = null,
        val headerMessage: String? = null,
        val bodyMessage: String? = null,
        val transactionStatus: String? = null,
        val flagRedirect: String? = null,
        val referenceId: String? = null,
        val transactionId: String? = null,
        val transactionDate: String? = null,
        val transactionType: String? = null,
        val read: Boolean? = null,
    )
}
