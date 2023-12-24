package com.fadlurahmanf.bebas_main.data.dto.notification

import com.fadlurahmanf.bebas_api.data.dto.notification.NotificationResponse

data class NotificationModel(
    val id: String,
    val titleMessage: String,
    val bodyMessage: String,
    val time: String,

    var additionalData: NotificationResponse.Content? = null
)
