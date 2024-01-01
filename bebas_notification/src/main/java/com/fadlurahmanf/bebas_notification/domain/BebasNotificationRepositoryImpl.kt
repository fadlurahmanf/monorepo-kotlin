package com.fadlurahmanf.bebas_notification.domain

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.util.Log

class BebasNotificationRepositoryImpl(
    context: Context
) : NotificationRepositoryImpl(context) {

    companion object {
        const val VOIP_CHANNEL_ID = "VOIP_CHANNEL"
        const val VOIP_CHANNEL_NAME = "VOIP"
        const val VOIP_CHANNEL_DESCRIPTION = "VOIP Description"

        const val TRANSACTION_CHANNEL_ID = "TRANSACTION"
        const val TRANSACTION_CHANNEL_NAME = "Transaction"
        const val TRANSACTION_CHANNEL_DESCRIPTION = "Transaction Bebas By Bank MAS"
    }

    private fun createVoipChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentChannel = notificationManager().notificationChannels.firstOrNull { channel ->
                channel.id == VOIP_CHANNEL_ID
            }
            if (currentChannel != null) {
                Log.d("MappLogger", "Channel $VOIP_CHANNEL_ID EXIST")
                return
            }
            val channel = NotificationChannel(
                VOIP_CHANNEL_ID,
                VOIP_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                this.description = VOIP_CHANNEL_DESCRIPTION
                setSound(null, null)
            }
            notificationManager().createNotificationChannel(channel)
        }
    }

    private fun createTransactionChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val currentChannel = notificationManager().notificationChannels.firstOrNull { channel ->
                channel.id == TRANSACTION_CHANNEL_ID
            }
            if (currentChannel != null) {
                Log.d("BebasLoggerNotification", "Channel $TRANSACTION_CHANNEL_ID EXIST")
                return
            }
            val channel = NotificationChannel(
                TRANSACTION_CHANNEL_ID,
                TRANSACTION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                this.description =
                    TRANSACTION_CHANNEL_DESCRIPTION
                setSound(null, null)
            }
            notificationManager().createNotificationChannel(channel)
        }
    }

    override fun showTransactionNotification(
        id: Int,
        title: String,
        body: String,
        onClickPendingIntent: PendingIntent?
    ) {
        createTransactionChannel()
        showNotification(
            id, title, body, TRANSACTION_CHANNEL_ID, onClickPendingIntent
        )
    }
}