package com.fadlurahmanf.bebas_loyalty.presentation.history.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fadlurahmanf.bebas_loyalty.presentation.history.HistoryLoyaltyFragment

class LoyaltyHistoryTabAdapter(
    private val context: Context,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    val allFragment = HistoryLoyaltyFragment.newInstance("ALL")
    val earnFragment = HistoryLoyaltyFragment.newInstance("EARNING")
    val redemptionFragment = HistoryLoyaltyFragment.newInstance("REDEMPTION")
    val expiredFragment = HistoryLoyaltyFragment.newInstance("EXPIRY")

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> earnFragment
            2 -> redemptionFragment
            3 -> expiredFragment
            else -> allFragment
        }
    }
}