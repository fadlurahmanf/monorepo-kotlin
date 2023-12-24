package com.fadlurahmanf.bebas_main.presentation.notification.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.presentation.notification.NotificationInformationFragment
import com.fadlurahmanf.bebas_main.presentation.notification.NotificationTransactionFragment


class NotificationTabAdapter(
    private val context: Context,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

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
        val v: View = LayoutInflater.from(context).inflate(R.layout.layout_tab_with_badge, null)
//        val tv = v.findViewById<View>(R.id.textView) as TextView
//        tv.setText(tabTitles.get(position))
//        val img = v.findViewById<View>(R.id.imgView) as ImageView
//        img.setImageResource(imageResId.get(position))
        return v
    }
}