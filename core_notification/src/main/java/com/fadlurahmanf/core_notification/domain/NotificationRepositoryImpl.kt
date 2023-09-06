package com.fadlurahmanf.core_notification.domain

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import javax.inject.Inject

abstract class NotificationRepositoryImpl(
    private val context: Context
) : NotificationRepository {

    abstract val channelId: String
    abstract val channel: String
    abstract val description: String

    init {
        createChannel()
    }

    private fun notificationManager() =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    final override fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channel,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                this.description = this@NotificationRepositoryImpl.description
                setSound(null, null)
            }
            val nm =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }

    override fun notificationBuilder(
        title: String,
        body: String
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(title)
            .setContentText(body)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun areNotificationEnabled(): Boolean {
        return notificationManager().areNotificationsEnabled()
    }

    override fun showNotification(id: Int, title: String, body: String) {
        return notificationManager().notify(
            id,
            notificationBuilder(title, body).build()
        )
    }
}