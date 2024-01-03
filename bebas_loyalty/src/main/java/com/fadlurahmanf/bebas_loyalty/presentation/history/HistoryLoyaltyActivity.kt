package com.fadlurahmanf.bebas_loyalty.presentation.history

import android.os.Bundle
import com.fadlurahmanf.bebas_loyalty.databinding.ActivityHistoryLoyaltyBinding
import com.fadlurahmanf.bebas_loyalty.presentation.BaseLoyaltyActivity
import com.fadlurahmanf.bebas_loyalty.presentation.history.adapter.LoyaltyHistoryTabAdapter
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class HistoryLoyaltyActivity :
    BaseLoyaltyActivity<ActivityHistoryLoyaltyBinding>(ActivityHistoryLoyaltyBinding::inflate) {

    @Inject
    lateinit var viewModel: HistoryLoyaltyViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var tabAdapter: LoyaltyHistoryTabAdapter
    override fun onBebasCreate(savedInstanceState: Bundle?) {
        tabAdapter = LoyaltyHistoryTabAdapter(applicationContext, supportFragmentManager, lifecycle)

        binding.vp.adapter = tabAdapter
        supportActionBar?.elevation = 0f

        TabLayoutMediator(binding.tabLayout, binding.vp) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Semua"
                }

                1 -> {
                    tab.text = "Didapat"
                }

                2 -> {
                    tab.text = "Digunakan"
                }

                3 -> {
                    tab.text = "Hangus"
                }
            }
        }.attach()
    }

}