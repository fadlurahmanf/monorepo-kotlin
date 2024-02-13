package com.fadlurahmanf.bebas_main.presentation.notification

import android.os.Bundle
import com.fadlurahmanf.bebas_main.databinding.ActivityNotificationBinding
import com.fadlurahmanf.bebas_main.presentation.BaseMainActivity
import com.fadlurahmanf.bebas_main.presentation.notification.adapter.NotificationTabAdapter
import com.fadlurahmanf.bebas_shared.RxBus
import com.fadlurahmanf.bebas_shared.RxEvent
import com.google.android.material.tabs.TabLayoutMediator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class NotificationActivity :
    BaseMainActivity<ActivityNotificationBinding>(ActivityNotificationBinding::inflate) {
    override fun injectActivity() {
        component.inject(this)
    }

    val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var viewModel: NotificationViewModel

    private lateinit var adapter: NotificationTabAdapter
    override fun onBebasCreate(savedInstanceState: Bundle?) {
        adapter = NotificationTabAdapter(applicationContext, supportFragmentManager, lifecycle)

        binding.vp.adapter = adapter
        binding.vp.isUserInputEnabled = false
        supportActionBar?.elevation = 0f

        TabLayoutMediator(binding.tabLayout, binding.vp) { tab, position ->
            tab.customView = adapter.getTabView(position)
        }.attach()

        viewModel.unreadTransaction.observe(this) {
            binding.tabLayout.getTabAt(0)?.customView = adapter.getTabViewWithBadge(0, it)
        }

        compositeDisposable.add(
            RxBus.listen(RxEvent.UpdateReadNotificationTransaction::class.java)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
                    val badgeTransaction = viewModel.unreadTransaction.value
                    if (badgeTransaction != null) {
                        viewModel.updateUnreadTransaction(badgeTransaction.minus(1))
                    }
                })

        viewModel.getTransactionNotifCount()
    }

}