package com.fadlurahmanf.mapp_notification.domain.repository

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.fadlurahmanf.mapp_notification.R
import com.fadlurahmanf.mapp_notification.domain.receiver.MappNotificationReceiver

class MappNotificationRepositoryImpl(
    val context: Context
) : NotificationRepositoryImpl(context) {

    companion object {
        const val VOIP_CHANNEL_ID = "VOIP_CHANNEL"
    }

    override val CHANNEL_NAME: String
        get() = "MAPP"

    override val CHANNEL_ID: String
        get() = "MAPP_CHANNEL_ID"

    override val CHANNEL_DESCRIPTION: String
        get() = "Mapp Notifikasi"

    override fun notificationBuilder(title: String, body: String): NotificationCompat.Builder {
        return super.notificationBuilder(title, body)
            .setSmallIcon(R.drawable.il_logo_bankmas)
    }


    fun showIncomingCall(notificationId: Int) {
        val builder = NotificationCompat.Builder(context, VOIP_CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_CALL)
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOngoing(true)
            .setWhen(0)
            .setTimeoutAfter(60000L)
            .setOnlyAlertOnce(true)
//            .setFullScreenIntent(getFullScreenIntent(notificationId), true)
            .setDeleteIntent(getDeletePendingIntent(notificationId))

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
        notification.flags = Notification.FLAG_INSISTENT

        notificationManager().notify(notificationId, notification)
    }

    private fun getFullScreenIntent() {}

    private fun getAcceptCallPendingIntent(notificationId: Int): PendingIntent {
        val extraData = Bundle()
        return MappNotificationReceiver.getAcceptCallPendingIntent(context, notificationId)
    }

    private fun getDeclinedCallPendingIntent(notificationId: Int): PendingIntent {
        val extraData = Bundle()
        return MappNotificationReceiver.getDeclinedCallPendingIntent(context, notificationId)
    }

    private fun getDeletePendingIntent(notificationId: Int): PendingIntent {
        val extraData = Bundle()
        return MappNotificationReceiver.getDeletePendingIntent(context, notificationId)
    }
}