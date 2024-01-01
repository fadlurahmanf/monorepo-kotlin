package com.fadlurahmanf.bebas_fcm.domain.services

import android.util.Log
import com.fadlurahmanf.bebas_notification.domain.BebasNotificationRepositoryImpl
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class BebasFcmService : FirebaseMessagingService() {
    lateinit var bebasNotificationRepositoryImpl: BebasNotificationRepositoryImpl

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("BebasLoggerFcm", "onNewToken")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        if (!::bebasNotificationRepositoryImpl.isInitialized) {
            bebasNotificationRepositoryImpl = BebasNotificationRepositoryImpl(applicationContext)
        }
        val notification = message.notification
        val title = notification?.title
        val body = notification?.body
        val data = message.data
        val transactionType = data["transactionType"]
        Log.d("BebasLoggerFcm", "TITLE: $title")
        Log.d("BebasLoggerFcm", "BODY: $body")
        Log.d("BebasLoggerFcm", "ADDITIONAL DATA: $data")

        if (title != null && body != null) {
            when (transactionType) {
                "Telepon/Internet" -> {
                    bebasNotificationRepositoryImpl.showTransactionNotification(
                        Random.nextInt(999),
                        title,
                        body,
                        null
                    )
                }
            }
        }
    }
}