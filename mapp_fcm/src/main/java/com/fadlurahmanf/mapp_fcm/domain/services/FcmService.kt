package com.fadlurahmanf.mapp_fcm.domain.services

import android.util.Log
import com.fadlurahmanf.mapp_notification.domain.receivers.MappNotificationReceiver
import com.fadlurahmanf.mapp_notification.domain.repositories.MappNotificationRepositoryImpl
import com.fadlurahmanf.mapp_notification.domain.repositories.NotificationRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class FcmService : FirebaseMessagingService() {
    private lateinit var notificationRepository: NotificationRepository

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MappLogger", "onNewToken")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        notificationRepository = MappNotificationRepositoryImpl(applicationContext)
        Log.d("MappLogger", "TITLE NOT: ${message.notification?.title}")
        Log.d("MappLogger", "BODY NOT: ${message.notification?.body}")
        Log.d("MappLogger", "DATA: ${message.data}")
        when (message.data["type"]) {
            "GENERAL" -> {
                showGeneral(message)
            }

            "INCOMING_CALL" -> {
                showIncomingCall()
            }
        }
    }

    private fun showGeneral(message: RemoteMessage) {
        val title = message.data["title"]
        val body = message.data["body"]
        if (title != null && body != null) {
            val id = Random.nextInt()
            val intent =
                MappNotificationReceiver.getOnClickGeneralPendingIntent(applicationContext, id)
            notificationRepository.showNotification(id, title, body, intent)
        } else {
            showUnknownNotification()
        }
    }

    private fun showIncomingCall() {
        Log.d("MappLogger", "FcmService showIncomingCall")
        MappNotificationReceiver.showIncomingCallNotification(applicationContext)
    }

    private fun showUnknownNotification() {
        notificationRepository.showNotification(
            Random.nextInt(9999),
            "Mapp Notification",
            "New Notification",
            null,
        )
    }
}