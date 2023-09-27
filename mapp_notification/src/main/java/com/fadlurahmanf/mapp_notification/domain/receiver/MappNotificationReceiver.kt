package com.fadlurahmanf.mapp_notification.domain.receiver

import android.app.PendingIntent
import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.fadlurahmanf.mapp_notification.data.model.SnoozeActionModel
import com.fadlurahmanf.mapp_notification.domain.repository.MappNotificationRepositoryImpl
import com.fadlurahmanf.mapp_notification.domain.repository.NotificationRepository

class MappNotificationReceiver : BroadcastReceiver() {
    private lateinit var notificationRepository: NotificationRepository

    companion object {
        const val ACTION_NOTIFICATION_SNOOZE =
            "com.fadlurahmanf.mapp_notification.ACTION_NOTIFICATION_SNOOZE"
        const val ACTION_NOTIFICATION_REPLY =
            "com.fadlurahmanf.mapp_notification.ACTION_NOTIFICATION_REPLY"
        const val ACTION_NOTIFICATION_ACCEPT =
            "com.fadlurahmanf.mapp_notification.ACTION_NOTIFICATION_ACCEPT"
        const val ACTION_NOTIFICATION_DECLINED_INCOMING_CALL =
            "com.fadlurahmanf.mapp_notification.ACTION_NOTIFICATION_DECLINED_INCOMING_CALL"
        const val ACTION_NOTIFICATION_DELETE =
            "com.fadlurahmanf.mapp_notification.ACTION_NOTIFICATION_DELETE"
        const val ACTION_NOTIFICATION_INCOMING_CALL =
            "com.fadlurahmanf.mapp_notification.ACTION_NOTIFICATION_INCOMING_CALL"

        fun showIncomingCallNotification(context: Context) {
            val intent = Intent(context, MappNotificationReceiver::class.java)
            intent.apply {
                action = ACTION_NOTIFICATION_INCOMING_CALL
            }
            context.sendBroadcast(intent)
        }

        fun getSnoozePendingIntent(
            context: Context,
            notificationId: Int,
            data: SnoozeActionModel? = null
        ): PendingIntent {
            val intent = Intent(context, MappNotificationReceiver::class.java)
            intent.apply {
                action = ACTION_NOTIFICATION_SNOOZE
                putExtra("DATA", data)
                putExtra("NOTIFICATION_ID", notificationId)
            }
            return PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                getFlagPendingIntent()
            )
        }

        fun getReplyPendingIntent(
            context: Context,
            notificationId: Int
        ): PendingIntent {
            val intent = Intent(context, MappNotificationReceiver::class.java)
            intent.apply {
                action = ACTION_NOTIFICATION_REPLY
                putExtra("NOTIFICATION_ID", notificationId)
            }
            return PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                PendingIntent.FLAG_MUTABLE
            )
        }

        fun getAcceptCallPendingIntent(
            context: Context,
            notificationId: Int
        ): PendingIntent {
            val intent = Intent(context, MappNotificationReceiver::class.java)
            intent.apply {
                action = ACTION_NOTIFICATION_ACCEPT
                putExtra("NOTIFICATION_ID", notificationId)
            }
            return PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                getFlagPendingIntent()
            )
        }

        fun getDeclinedCallPendingIntent(
            context: Context,
            notificationId: Int
        ): PendingIntent {
            val intent = Intent(context, MappNotificationReceiver::class.java)
            intent.apply {
                action = ACTION_NOTIFICATION_DECLINED_INCOMING_CALL
                putExtra("NOTIFICATION_ID", notificationId)
            }
            return PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                getFlagPendingIntent()
            )
        }

        fun getDeletePendingIntent(
            context: Context,
            notificationId: Int
        ): PendingIntent {
            val intent = Intent(context, MappNotificationReceiver::class.java)
            intent.apply {
                action = ACTION_NOTIFICATION_DELETE
                putExtra("NOTIFICATION_ID", notificationId)
            }
            return PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                getFlagPendingIntent()
            )
        }

        private fun getFlagPendingIntent(): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (!this::notificationRepository.isInitialized && context != null) {
            notificationRepository = MappNotificationRepositoryImpl(context)
        }
        when (intent?.action) {
            ACTION_NOTIFICATION_SNOOZE -> {
                onSnoozeClicked(context, intent.extras)
            }

            ACTION_NOTIFICATION_REPLY -> {
                onReplyClicked(context, intent, intent.extras)
            }
        }
    }

    private fun onReplyClicked(context: Context?, intent: Intent?, data: Bundle?) {
        Log.d("MappActivity", "onReplyClicked")
        val inputText = RemoteInput.getResultsFromIntent(intent).getCharSequence("KEY_TEXT_REPLY")
        val notificationId = data?.getInt("NOTIFICATION_ID")
        if (notificationId != null) {
            notificationRepository.showNotification(
                notificationId,
                "Reply Result",
                "$inputText"
            )
        }
    }

    private fun onSnoozeClicked(context: Context?, data: Bundle?) {
        Log.d("MappActivity", "onSnoozeClicked")
        val snoozeData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            data?.getParcelable("DATA", SnoozeActionModel::class.java)
        } else {
            data?.getParcelable<SnoozeActionModel>("DATA")
        }
        Log.d("MappActivity", "DATA onSnoozeClicked: $snoozeData")
        val notificationId = data?.getInt("NOTIFICATION_ID")
        if (notificationId != null) {
            notificationRepository.cancelNotification(notificationId)
        }
    }
}