package com.fadlurahmanf.mapp_fcm.domain.services

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FcmService:FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("MappLogger", "onNewToken")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("MappLogger", "TITLE NOT: ${message.notification?.title}")
        Log.d("MappLogger", "BODY NOT: ${message.notification?.body}")
        Log.d("MappLogger", "DATA: ${message.data}")
        when(message.data["type"]){
            "INCOMING_CALL" -> {
                showIncomingCall()
            }
        }
    }

    private fun showIncomingCall() {
        Log.d("MappLogger", "showIncomingCall")
    }
}