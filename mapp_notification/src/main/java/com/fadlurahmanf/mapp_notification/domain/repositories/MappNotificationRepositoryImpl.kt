package com.fadlurahmanf.mapp_notification.domain.repositories

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.fadlurahmanf.mapp_notification.R
import com.fadlurahmanf.mapp_notification.domain.receivers.MappNotificationReceiver
import com.fadlurahmanf.mapp_notification.presentation.call.IncomingCallActivity

class MappNotificationRepositoryImpl(
    val context: Context
) : NotificationRepositoryImpl(context) {

    companion object {
        const val VOIP_CHANNEL_ID = "VOIP_CHANNEL"
        const val VOIP_CHANNEL_NAME = "VOIP"
        const val VOIP_CHANNEL_DESCRIPTION = "VOIP Description"
    }

    override val CHANNEL_NAME: String
        get() = "MAPP"

    override val CHANNEL_ID: String
        get() = "MAPP_CHANNEL_ID"

    override val CHANNEL_DESCRIPTION: String
        get() = "Mapp Notifikasi"

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

    override fun notificationBuilder(title: String, body: String): NotificationCompat.Builder {
        return super.notificationBuilder(title, body)
            .setSmallIcon(R.drawable.il_logo_bankmas)
    }


    override fun showIncomingCallNotification(notificationId: Int) {
        createVoipChannel()
        val builder = NotificationCompat.Builder(context, VOIP_CHANNEL_ID)
            .setSmallIcon(R.drawable.il_logo_bankmas)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setWhen(0)
            .setTimeoutAfter(60000L)
            .setOnlyAlertOnce(true)
            .setFullScreenIntent(getFullScreenIntent(notificationId), true)
            .setDeleteIntent(getDeleteCallPendingIntent(notificationId))

        val notificationView =
            RemoteViews(context.packageName, R.layout.layout_incoming_call_notification)
        val smallNotificationView =
            RemoteViews(context.packageName, R.layout.layout_incoming_call_notification_small)

        notificationView.setTextViewText(
            R.id.tv_caller_name, "MAPP CALLER NAME"
        )
        notificationView.setTextViewText(
            R.id.tv_phone_caller, "+6281283602320"
        )
        notificationView.setOnClickPendingIntent(
            R.id.iv_decline,
            getDeclinedCallPendingIntent(notificationId)
        )
        notificationView.setOnClickPendingIntent(
            R.id.iv_accept,
            getAcceptCallPendingIntent(notificationId)
        )

        smallNotificationView.setTextViewText(
            R.id.tv_caller_name, "MAPP CALLER NAME"
        )
        smallNotificationView.setOnClickPendingIntent(
            R.id.iv_decline,
            getDeclinedCallPendingIntent(notificationId)
        )
        smallNotificationView.setOnClickPendingIntent(
            R.id.iv_accept,
            getAcceptCallPendingIntent(notificationId)
        )

        builder.setCustomContentView(smallNotificationView)
            .setCustomHeadsUpContentView(smallNotificationView)
            .setCustomBigContentView(notificationView)

        val notification = builder.build()
        notification.flags = Notification.FLAG_ONGOING_EVENT

        notificationManager().notify(notificationId, notification)
    }

    private fun getFullScreenIntent(notificationId: Int): PendingIntent {
        val intent = Intent(
            context,
            IncomingCallActivity::class.java
        )
        intent.apply {
            putExtra("NOTIFICATION_ID", notificationId)
        }
        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getActivity(context, notificationId, intent, flag)
    }

    private fun getAcceptCallPendingIntent(notificationId: Int): PendingIntent {
        val extraData = Bundle()
        return MappNotificationReceiver.getAcceptCallPendingIntent(context, notificationId)
    }

    private fun getDeclinedCallPendingIntent(notificationId: Int): PendingIntent {
        val extraData = Bundle()
        return MappNotificationReceiver.getDeclinedCallPendingIntent(context, notificationId)
    }

    private fun getDeleteCallPendingIntent(notificationId: Int): PendingIntent {
        val extraData = Bundle()
        return MappNotificationReceiver.getDeleteCallPendingIntent(context, notificationId)
    }
}