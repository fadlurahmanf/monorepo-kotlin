package com.fadlurahmanf.bebas_main.presentation.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.databinding.ActivityNotificationBinding
import com.fadlurahmanf.bebas_main.presentation.BaseMainActivity
import com.fadlurahmanf.bebas_main.presentation.notification.adapter.NotificationTabAdapter
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.ExperimentalCoroutinesApi

class NotificationActivity :
    BaseMainActivity<ActivityNotificationBinding>(ActivityNotificationBinding::inflate) {
    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var adapter: NotificationTabAdapter
    override fun onBebasCreate(savedInstanceState: Bundle?) {
        adapter = NotificationTabAdapter(applicationContext, supportFragmentManager, lifecycle)

        binding.vp.adapter = adapter
        supportActionBar?.elevation = 0f

        TabLayoutMediator(binding.tabLayout, binding.vp) { tab, position ->
//            tab.text = "TES $position"
            tab.customView = adapter.getTabView(position)
        }.attach()
    }

}