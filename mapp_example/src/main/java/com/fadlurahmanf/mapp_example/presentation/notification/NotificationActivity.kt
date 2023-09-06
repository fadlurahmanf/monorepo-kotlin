package com.fadlurahmanf.mapp_example.presentation.notification

import android.content.pm.PackageManager
import com.fadlurahmanf.mapp_notification.domain.NotificationRepository
import com.fadlurahmanf.mapp_example.databinding.ActivityNotificationBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import com.fadlurahmanf.mapp_notification.domain.MappNotificationRepositoryImpl
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
                title = "TITLE NOTIFICATION",
                body = "BODY NOTIFICATION"
            )
        }
    }
}