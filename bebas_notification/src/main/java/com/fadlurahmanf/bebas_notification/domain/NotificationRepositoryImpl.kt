package com.fadlurahmanf.bebas_notification.domain

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.fadlurahmanf.bebas_notification.R

abstract class NotificationRepositoryImpl(
    private val context: Context
) : NotificationRepository {

    fun notificationManager() =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun notificationBuilder(
        title: String,
        body: String,
        channelId: String,
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.il_logo_bebas)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)

    }

    override fun areNotificationEnabled(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notificationManager().areNotificationsEnabled()
        } else {
            TODO("VERSION.SDK_INT < N")
        }
    }

    override fun checkPostNotificationStatus(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            TODO("VERSION.SDK_INT < TIRAMISU")
        }
    }


    override fun showNotification(
        id: Int,
        title: String,
        body: String,
        channelId: String,
        onClickPendingIntent: PendingIntent?
    ) {
        val builder = notificationBuilder(title, body, channelId)
        if (onClickPendingIntent != null) {
            builder.setContentIntent(onClickPendingIntent)
        }
        return notificationManager().notify(
            id,
            builder.build()
        )
    }

    override fun showImageNotification(
        id: Int,
        title: String,
        body: String,
        channelId: String,
        imageUrl: String,
    ) {
        val builder = notificationBuilder(title, body, channelId)
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    builder.setLargeIcon(resource)
                    builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(resource))
                    notificationManager()
                        .notify(id, builder.build())
                }

                override fun onLoadCleared(placeholder: Drawable?) {}

            })
    }

    override fun cancelNotification(id: Int) {
        notificationManager().cancel(id)
    }
}