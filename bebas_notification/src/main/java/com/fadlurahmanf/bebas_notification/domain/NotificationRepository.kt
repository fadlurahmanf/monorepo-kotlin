package com.fadlurahmanf.bebas_notification.domain

import android.app.PendingIntent
import androidx.core.app.NotificationCompat

interface NotificationRepository {

    fun notificationBuilder(
        title: String,
        body: String,
        channelId: String,
    ): NotificationCompat.Builder

    /**
     * cek untuk android 12 kebawah
     * */
    fun areNotificationEnabled(): Boolean

    fun checkPostNotificationStatus(): Int

    fun showNotification(
        id: Int,
        title: String,
        body: String,
        channelId: String,
        onClickPendingIntent: PendingIntent?
    )

    fun showImageNotification(
        id: Int,
        title: String,
        body: String,
        channelId: String,
        imageUrl: String,
    )

    fun showTransactionNotification(
        id: Int,
        title: String,
        body: String,
        onClickPendingIntent: PendingIntent?
    )

    fun cancelNotification(
        id: Int
    )
}