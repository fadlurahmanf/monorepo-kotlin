package com.fadlurahmanf.mapp_notification.domain.receivers

import android.app.PendingIntent
import android.app.RemoteInput
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.fadlurahmanf.mapp_notification.data.model.SnoozeActionModel
import com.fadlurahmanf.mapp_notification.domain.repositories.MappNotificationRepositoryImpl
import com.fadlurahmanf.mapp_notification.domain.repositories.NotificationRepository
import com.fadlurahmanf.mapp_notification.domain.services.MappNotificationPlayerService
import kotlin.random.Random

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
        const val ACTION_NOTIFICATION_SHOW_INCOMING_CALL =
            "com.fadlurahmanf.mapp_notification.ACTION_NOTIFICATION_SHOW_INCOMING_CALL"
        const val ACTION_NOTIFICATION_ON_CLICK_GENERAL =
            "com.fadlurahmanf.mapp_notification.ACTION_NOTIFICATION_ON_CLICK_GENERAL"

        fun getOnClickGeneralPendingIntent(
            context: Context,
            notificationId: Int,
        ): PendingIntent {
            val intent = Intent(context, MappNotificationReceiver::class.java)
            intent.apply {
                action = ACTION_NOTIFICATION_ON_CLICK_GENERAL
                putExtra("NOTIFICATION_ID", notificationId)
            }
            return PendingIntent.getBroadcast(
                context,
                notificationId,
                intent,
                getFlagPendingIntent()
            )
        }

        fun showIncomingCallNotification(context: Context) {
            Log.d("MappLogger", "showIncomingCallNotification")
            val intent = Intent(context, MappNotificationReceiver::class.java)
            intent.apply {
                action = ACTION_NOTIFICATION_SHOW_INCOMING_CALL
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
        Log.d("MappLogger", "onReceive MappNotificationReceiver.kt")
        if (!this::notificationRepository.isInitialized && context != null) {
            notificationRepository = MappNotificationRepositoryImpl(context)
        }
        when (intent?.action) {
            ACTION_NOTIFICATION_ON_CLICK_GENERAL -> {
                onGeneralNotificationClicked(context, intent.extras)
            }

            ACTION_NOTIFICATION_SNOOZE -> {
                onSnoozeClicked(context, intent.extras)
            }

            ACTION_NOTIFICATION_REPLY -> {
                onReplyClicked(context, intent, intent.extras)
            }

            ACTION_NOTIFICATION_SHOW_INCOMING_CALL -> {
                onIncomingCall(context, intent.extras)
            }
        }
    }

    private fun onIncomingCall(context: Context?, data: Bundle?) {
        Log.d("MappLogger", "onIncomingCall")
        if (context != null) {
            val notificationId = Random.nextInt(9999)
            notificationRepository.showIncomingCallNotification(notificationId)
//            MappNotificationPlayerService.startIncomingCallNotificationPlayer(context)
        }
    }

    private fun onGeneralNotificationClicked(context: Context?, data: Bundle?) {
        Log.d("MappLogger", "onGeneralNotificationClicked")
        val notificationId = data?.getInt("NOTIFICATION_ID")
        if (notificationId != null) {
            notificationRepository.cancelNotification(notificationId)
        }
        val intent = Intent(
            context,
            Class.forName("com.fadlurahmanf.mapp_example.presentation.notification.NotificationActivity")
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context?.startActivity(intent)
    }

    private fun onReplyClicked(context: Context?, intent: Intent?, data: Bundle?) {
        Log.d("MappLogger", "onReplyClicked")
        val inputText = RemoteInput.getResultsFromIntent(intent).getCharSequence("KEY_TEXT_REPLY")
        val notificationId = data?.getInt("NOTIFICATION_ID")
        if (notificationId != null) {
            notificationRepository.showNotification(
                notificationId,
                "Reply Result",
                "$inputText",
                null
            )
        }
    }

    private fun onSnoozeClicked(context: Context?, data: Bundle?) {
        Log.d("MappLogger", "onSnoozeClicked")
        val snoozeData = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            data?.getParcelable("DATA", SnoozeActionModel::class.java)
        } else {
            data?.getParcelable<SnoozeActionModel>("DATA")
        }
        Log.d("MappLogger", "DATA onSnoozeClicked: $snoozeData")
        val notificationId = data?.getInt("NOTIFICATION_ID")
        if (notificationId != null) {
            notificationRepository.cancelNotification(notificationId)
        }
    }
}