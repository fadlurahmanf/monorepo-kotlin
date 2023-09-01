package com.fadlurahmanf.mapp_example.presentation.notification

import android.Manifest
import android.app.Notification
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.fadlurahmanf.core_notification.domain.NotificationRepository
import com.fadlurahmanf.mapp_example.R
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

        binding.btnNotificationPermission.setOnClickListener {

        }
    }
}