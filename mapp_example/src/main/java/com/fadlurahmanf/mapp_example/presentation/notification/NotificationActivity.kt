package com.fadlurahmanf.mapp_example.presentation.notification

import android.util.Log
import com.fadlurahmanf.core_notification.domain.NotificationRepository
import com.fadlurahmanf.mapp_example.databinding.ActivityNotificationBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import com.fadlurahmanf.mapp_notification.MappNotificationRepositoryImpl

class NotificationActivity :
    BaseExampleActivity<ActivityNotificationBinding>(ActivityNotificationBinding::inflate) {
    private lateinit var notificationRepository: NotificationRepository

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        notificationRepository = MappNotificationRepositoryImpl(this)


        binding.btnNotificationPermission.onClicked {
            Log.d(this.packageName, "setup: ")
        }
    }
}