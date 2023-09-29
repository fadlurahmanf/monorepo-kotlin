package com.fadlurahmanf.mapp_notification.data.model

import android.app.PendingIntent

data class NotificationActionModel(
    var icon: Int,
    var title: String,
    var pendingIntent: PendingIntent? = null
)
