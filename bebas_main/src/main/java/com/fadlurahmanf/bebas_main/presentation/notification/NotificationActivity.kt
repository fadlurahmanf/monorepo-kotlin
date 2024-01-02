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
import javax.inject.Inject

class NotificationActivity :
    BaseMainActivity<ActivityNotificationBinding>(ActivityNotificationBinding::inflate) {
    override fun injectActivity() {
        component.inject(this)
    }

    @Inject
    lateinit var viewModel: NotificationViewModel

    private lateinit var adapter: NotificationTabAdapter
    override fun onBebasCreate(savedInstanceState: Bundle?) {
        adapter = NotificationTabAdapter(applicationContext, supportFragmentManager, lifecycle)

        binding.vp.adapter = adapter
        supportActionBar?.elevation = 0f

        TabLayoutMediator(binding.tabLayout, binding.vp) { tab, position ->
            tab.customView = adapter.getTabView(position)
        }.attach()

        viewModel.unreadTransaction.observe(this) {
            binding.tabLayout.getTabAt(0)?.customView = adapter.getTabViewWithBadge(0, it)
        }

        viewModel.getTransactionNotifCount()
    }

}