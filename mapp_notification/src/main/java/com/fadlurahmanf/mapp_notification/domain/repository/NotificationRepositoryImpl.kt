package com.fadlurahmanf.mapp_notification.domain.repository

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.fadlurahmanf.mapp_notification.data.model.NotificationActionModel

abstract class NotificationRepositoryImpl(
    private val context: Context
) : NotificationRepository {

    abstract val CHANNEL_ID: String
    abstract val CHANNEL_NAME: String
    abstract val CHANNEL_DESCRIPTION: String

    init {
        createChannel()
    }

    fun notificationManager() =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    final override fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                this.description = this@NotificationRepositoryImpl.CHANNEL_DESCRIPTION
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
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(title)
            .setContentText(body)

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

    override fun showNotification(id: Int, title: String, body: String) {
        return notificationManager().notify(
            id,
            notificationBuilder(title, body).build()
        )
    }

    override fun showNotification(
        id: Int,
        title: String,
        body: String,
        actions: List<NotificationActionModel>
    ) {
        val builder = notificationBuilder(title, body)
        actions.forEach {
            builder.addAction(it.icon, it.title, it.pendingIntent)
        }
        notificationManager().notify(id, builder.build())
    }

    override fun showLongNotification(id: Int, title: String, body: String) {
        val builder = notificationBuilder(title, body)
        builder.setStyle(
            NotificationCompat.BigTextStyle()
                .bigText(body)
        )
        notificationManager().notify(id, builder.build())
    }

    override fun showRawNotification(
        id: Int,
        title: String,
        body: String,
        actions: List<NotificationCompat.Action>
    ) {
        val builder = notificationBuilder(title, body)
        actions.forEach {
            builder.addAction(it)
        }
        notificationManager().notify(id, builder.build())
    }

    override fun showImageNotification(
        id: Int,
        title: String,
        body: String,
        imageUrl: String,
    ) {
        val builder = notificationBuilder(title, body)
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

    override fun showMessagingSyleNotification(
        id: Int, title: String, body: String
    ) {
        val builder = notificationBuilder(title, body)

        val person = Person.fromBundle(Bundle().apply {
            putString("name", "SEND NAME")
            putString("uri", "https://raw.githubusercontent.com/TutorialsBuzz/cdn/main/android.jpg")
        })

        val mes =
            NotificationCompat.MessagingStyle.Message("TES", System.currentTimeMillis(), person)

        builder.setStyle(
            NotificationCompat.MessagingStyle("Me")
                .setConversationTitle("CONVERSATION TITLE")
                .addMessage("TEXT 1", System.currentTimeMillis(), "SENDER")
                .addMessage("TEXT 2", System.currentTimeMillis(), "SENDER 2")

        )
        notificationManager().notify(
            id, builder.build()
        )
    }

    override fun cancelNotification(id: Int) {
        notificationManager().cancel(id)
    }
}