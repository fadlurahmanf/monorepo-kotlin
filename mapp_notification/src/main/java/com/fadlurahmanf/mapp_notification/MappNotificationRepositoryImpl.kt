package com.fadlurahmanf.mapp_notification

import android.content.Context
import androidx.core.app.NotificationCompat
import com.fadlurahmanf.core_notification.domain.NotificationRepositoryImpl

class MappNotificationRepositoryImpl(
    private val context: Context
) : NotificationRepositoryImpl(context) {

    override val channel: String
        get() = "MAPP"

    override val channelId: String
        get() = "MAPP_CHANNEL_ID"

    override val description: String
        get() = "Mapp Notifikasi"

    override fun notificationBuilder(title: String, body: String): NotificationCompat.Builder {
        return super.notificationBuilder(title, body)
            .setSmallIcon(R.drawable.il_logo_bankmas)
    }


}