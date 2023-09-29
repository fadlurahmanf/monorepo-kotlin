package com.fadlurahmanf.mapp_notification.domain.repositories

import android.app.PendingIntent
import androidx.core.app.NotificationCompat
import com.fadlurahmanf.mapp_notification.data.model.NotificationActionModel

interface NotificationRepository {

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
        body: String,
        onClickPendingIntent: PendingIntent?
    )

    fun showLongNotification(
        id: Int,
        title: String,
        body: String,
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

    fun showMessagingSyleNotification(
        id: Int, title: String, body: String
    )

    fun cancelNotification(
        id: Int
    )

    fun showIncomingCallNotification(notificationId: Int)
}