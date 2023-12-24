package com.fadlurahmanf.bebas_main.presentation.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.presentation.notification.NotificationInformationFragment
import com.fadlurahmanf.bebas_main.presentation.notification.NotificationTransactionFragment
import kotlinx.coroutines.ExperimentalCoroutinesApi

class NotificationTabAdapter(
    private val context: Context,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    @ExperimentalCoroutinesApi
    val transactionFragment = NotificationTransactionFragment()
    val informationFragment = NotificationInformationFragment()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> informationFragment
            else -> transactionFragment
        }
    }

    fun getTabView(position: Int): View {
        val v: View =
            LayoutInflater.from(context).inflate(R.layout.layout_tab_with_badge, null, false)
        val title = v.findViewById<TextView>(R.id.tv_tab_title)
        title.text = when (position) {
            1 -> "Informasi"
            else -> {
                "Transaksi"
            }
        }
        return v
    }
}