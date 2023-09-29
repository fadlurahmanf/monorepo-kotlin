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
        const val ACTION_NOTIFICATION_ACCEPT_INCOMING_CALL =
            "com.fadlurahmanf.mapp_notification.ACTION_NOTIFICATION_ACCEPT_INCOMING_CALL"
        const val ACTION_NOTIFICATION_DECLINED_INCOMING_CALL =
            "com.fadlurahmanf.mapp_notification.ACTION_NOTIFICATION_DECLINED_INCOMING_CALL"
        const val ACTION_NOTIFICATION_DELETE_CALL =
            "com.fadlurahmanf.mapp_notification.ACTION_NOTIFICATION_DELETE_CALL"
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

        fun sendBroadcastShowIncomingCall(context: Context) {
            Log.d("MappLogger", "sendBroadcastShowIncomingCall")
            val intent = Intent(context, MappNotificationReceiver::class.java)
            intent.apply {
                action = ACTION_NOTIFICATION_SHOW_INCOMING_CALL
            }
            context.sendBroadcast(intent)
        }

        fun sendBroadcastAcceptIncomingCall(context: Context, notificationId: Int) {
            Log.d("MappLogger", "sendBroadcastAcceptIncomingCall")
            val intent = Intent(context, MappNotificationReceiver::class.java)
            intent.apply {
                action = ACTION_NOTIFICATION_ACCEPT_INCOMING_CALL
                putExtra("NOTIFICATION_ID", notificationId)
            }
            context.sendBroadcast(intent)
        }

        fun sendBroadcastDeclinedIncomingCall(context: Context) {
            Log.d("MappLogger", "sendBroadcastDecilnedIncomingCall")
            val intent = Intent(context, MappNotificationReceiver::class.java)
            intent.apply {
                action = ACTION_NOTIFICATION_DECLINED_INCOMING_CALL
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
                action = ACTION_NOTIFICATION_ACCEPT_INCOMING_CALL
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

        fun getDeleteCallPendingIntent(
            context: Context,
            notificationId: Int
        ): PendingIntent {
            val intent = Intent(context, MappNotificationReceiver::class.java)
            intent.apply {
                action = ACTION_NOTIFICATION_DELETE_CALL
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
        Log.d("MappLogger", "onReceive ${intent?.action}")
        if (!this::notificationRepository.isInitialized && context != null) {
            notificationRepository = MappNotificationRepositoryImpl(context)
        }
        if (context == null) return
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

            ACTION_NOTIFICATION_DELETE_CALL -> {
                onDeleteCall(context, intent.extras)
            }

            ACTION_NOTIFICATION_DECLINED_INCOMING_CALL -> {
                onDeclinedIncomingCall(context, intent.extras)
            }

            ACTION_NOTIFICATION_ACCEPT_INCOMING_CALL -> {
                onAcceptIncomingCall(context, intent.extras)
            }
        }
    }

    private fun onAcceptIncomingCall(context: Context, data: Bundle?) {
        val notificationId = data?.getInt("NOTIFICATION_ID")
        MappNotificationPlayerService.stopIncomingCallNotificationPlayer(context)
        if (notificationId != null) {
            notificationRepository.cancelNotification(notificationId)
        }
        val intent = Intent(
            context,
            Class.forName("com.fadlurahmanf.mapp_example.presentation.call.CallAcceptActivity")
        )
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    private fun onDeclinedIncomingCall(context: Context, data: Bundle?) {
        val notificationId = data?.getInt("NOTIFICATION_ID")
        MappNotificationPlayerService.stopIncomingCallNotificationPlayer(context)
        if (notificationId != null) {
            notificationRepository.cancelNotification(notificationId)
        }
    }

    private fun onDeleteCall(context: Context, data: Bundle?) {
        val notificationId = data?.getInt("NOTIFICATION_ID")
        MappNotificationPlayerService.stopIncomingCallNotificationPlayer(context)
        if (notificationId != null) {
            notificationRepository.cancelNotification(notificationId)
        }
    }

    private fun onIncomingCall(context: Context, data: Bundle?) {
        Log.d("MappLogger", "onIncomingCall")
        val notificationId = Random.nextInt(9999)
        notificationRepository.showIncomingCallNotification(notificationId)
        MappNotificationPlayerService.startIncomingCallNotificationPlayer(context)
    }

    private fun onGeneralNotificationClicked(context: Context, data: Bundle?) {
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
        context.startActivity(intent)
    }

    private fun onReplyClicked(context: Context, intent: Intent?, data: Bundle?) {
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

    private fun onSnoozeClicked(context: Context, data: Bundle?) {
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