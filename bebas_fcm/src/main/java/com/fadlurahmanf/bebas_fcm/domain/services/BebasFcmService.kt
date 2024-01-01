package com.fadlurahmanf.bebas_fcm.domain.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class BebasFcmService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("BebasLoggerFcm", "onNewToken")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("BebasLoggerFcm", "TITLE: ${message.notification?.title}")
        Log.d("BebasLoggerFcm", "BODY: ${message.notification?.body}")
        Log.d("BebasLoggerFcm", "ADDITIONAL DATA: ${message.data}")
//        when (message.data["type"]) {
//            "GENERAL" -> {
//                showGeneral(message)
//            }
//
//            "INCOMING_CALL" -> {
//                showIncomingCall()
//            }
//        }
    }

    private fun showGeneral(message: RemoteMessage) {
//        val title = message.data["title"]
//        val body = message.data["body"]
//        if (title != null && body != null) {
//            val id = Random.nextInt()
//            val intent =
//                MappNotificationReceiver.getOnClickGeneralPendingIntent(applicationContext, id)
//            notificationRepository.showNotification(id, title, body, intent)
//        } else {
//            showUnknownNotification()
//        }
    }

    private fun showIncomingCall() {
//        Log.d("MappLogger", "FcmService showIncomingCall")
//        MappNotificationReceiver.sendBroadcastShowIncomingCall(applicationContext)
    }

    private fun showUnknownNotification() {
//        notificationRepository.showNotification(
//            Random.nextInt(9999),
//            "Mapp Notification",
//            "New Notification",
//            null,
//        )
    }
}