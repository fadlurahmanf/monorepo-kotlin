package com.fadlurahmanf.mapp_example.presentation.notification

import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.fadlurahmanf.mapp_example.R
import com.fadlurahmanf.mapp_notification.domain.repository.NotificationRepository
import com.fadlurahmanf.mapp_example.databinding.ActivityNotificationBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import com.fadlurahmanf.mapp_notification.data.model.NotificationActionModel
import com.fadlurahmanf.mapp_notification.domain.receiver.MappNotificationReceiver
import com.fadlurahmanf.mapp_notification.domain.repository.MappNotificationRepositoryImpl
import kotlin.random.Random

class NotificationActivity :
    BaseExampleActivity<ActivityNotificationBinding>(ActivityNotificationBinding::inflate) {
    private lateinit var notificationRepository: NotificationRepository

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        notificationRepository = MappNotificationRepositoryImpl(this)

        binding.btnNotificationPermission.onClicked {
            val rawStatus = notificationRepository.checkPostNotificationStatus()
            showInfoBottomsheet(
                title = "NOTIFICATION",
                desc = "STATUS: $rawStatus && IS GRANTED = ${PackageManager.PERMISSION_GRANTED == rawStatus}"
            )
        }

        binding.btnShowNotification.onClicked {
            notificationRepository.showNotification(
                Random.nextInt(999),
                title = "Title Notification",
                body = "Body Notification"
            )
        }

        binding.btnShowLongNotification.onClicked {
            notificationRepository.showNotification(
                Random.nextInt(999),
                title = "Title Notification",
                body = "Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification Body Notification"
            )
        }

        binding.btnShowImageNotification.onClicked {
            notificationRepository.showImageNotification(
                Random.nextInt(999),
                title = "Example Image Notification",
                body = "Example Image Body Notification",
                imageUrl = "https://raw.githubusercontent.com/TutorialsBuzz/cdn/main/android.jpg"
            )
        }

        binding.btnShowActionNotification.onClicked {
            val id = Random.nextInt()
            notificationRepository.showNotification(
                id,
                title = "Action Notification",
                body = "Body Action Notification",
                actions = listOf(
                    NotificationActionModel(
                        R.drawable.outline_featured_play_list_24,
                        "SNOOZE",
                        MappNotificationReceiver.getSnoozePendingIntent(
                            this,
                            id,
                        )
                    )
                )
            )
        }

        binding.btnReplyActionNotification.onClicked {
            val id = Random.nextInt()
            val remoteInput: RemoteInput = RemoteInput.Builder("KEY_TEXT_REPLY").run {
                setLabel("REPLAB")
                build()
            }
            val replyAction = NotificationCompat.Action.Builder(
                com.fadlurahmanf.mapp_notification.R.drawable.round_reply_24,
                "REPTIT",
                MappNotificationReceiver.getReplyPendingIntent(this, id)
            ).addRemoteInput(remoteInput).build()
            notificationRepository.showRawNotification(
                id,
                title = "Reply Notification",
                body = "Body Reply Notification",
                actions = listOf(
                    replyAction
                )
            )
        }
    }
}