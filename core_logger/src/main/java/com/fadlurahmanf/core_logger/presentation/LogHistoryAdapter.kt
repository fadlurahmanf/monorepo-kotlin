package com.fadlurahmanf.core_logger.presentation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class LogHistoryAdapter(
    fm: FragmentManager,
    private var totalTabs: Int
) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int = totalTabs

    override fun getItem(position: Int): Fragment {
        return when (position) {
            1 -> LogHistoryFragment.newInstance("DEBUG")
            2 -> LogHistoryFragment.newInstance("INFO")
            3 -> LogHistoryFragment.newInstance("ERROR")
            else -> LogHistoryFragment.newInstance("ALL")
        }
    }
}