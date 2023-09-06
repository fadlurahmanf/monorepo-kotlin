package com.fadlurahmanf.mapp_notification.domain.repository

import androidx.core.app.NotificationCompat
import com.fadlurahmanf.mapp_notification.data.model.NotificationActionModel

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

    fun showNotification(
        id: Int,
        title: String,
        body: String,
        actions: List<NotificationActionModel>
    )

    fun showRawNotification(
        id: Int,
        title: String,
        body: String,
        actions: List<NotificationCompat.Action>
    )

    fun showImageNotification(
        id: Int,
        title: String,
        body: String,
        imageUrl: String,
    )

    fun cancelNotification(
        id: Int
    )
}