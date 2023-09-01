package com.fadlurahmanf.core_notification.domain

import androidx.core.app.NotificationCompat

interface NotificationRepository {
    fun createChannel()

    fun notificationBuilder(
        title: String,
        body: String
    ): NotificationCompat.Builder

    /**
     * cek untuk android 12 kebawah
     * */
    fun areNotificationEnabled():Boolean

    fun showNotification(
        id: Int,
        title: String,
        body: String
    )
}