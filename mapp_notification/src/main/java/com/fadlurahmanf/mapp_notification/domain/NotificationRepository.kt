package com.fadlurahmanf.mapp_notification.domain

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
    fun areNotificationEnabled(): Boolean

    fun checkPostNotificationStatus(): Int

    fun showNotification(
        id: Int,
        title: String,
        body: String
    )

    fun showImageNotification(
        id: Int,
        title: String,
        body: String,
        imageUrl: String,
    )
}