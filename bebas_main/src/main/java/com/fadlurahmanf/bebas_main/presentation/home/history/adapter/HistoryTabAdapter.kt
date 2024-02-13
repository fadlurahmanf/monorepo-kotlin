package com.fadlurahmanf.bebas_main.presentation.home.history.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fadlurahmanf.bebas_main.presentation.home.history.EStatementFragment
import com.fadlurahmanf.bebas_main.presentation.home.history.MutationFragment

class HistoryTabAdapter(
    private val context: Context,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    val mutationFragment = MutationFragment()
    val estatementFragment = EStatementFragment()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> estatementFragment
            else -> mutationFragment
        }
    }
}