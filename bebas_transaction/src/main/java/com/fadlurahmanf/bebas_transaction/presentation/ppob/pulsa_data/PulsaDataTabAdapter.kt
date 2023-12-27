package com.fadlurahmanf.bebas_transaction.presentation.ppob.pulsa_data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.fadlurahmanf.bebas_transaction.R

class PulsaDataTabAdapter(
    private val context: Context,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    val pulsaFragment = PulsaDenomFragment()
    val paketDataFragment = PaketDataDenomFragment()

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> paketDataFragment
            else -> pulsaFragment
        }
    }

    fun getTabView(position: Int): View {
        val v: View =
            LayoutInflater.from(context).inflate(R.layout.layout_tab, null, false)
        val title = v.findViewById<TextView>(R.id.tv_tab_title)
        title.text = when (position) {
            1 -> "Paket"
            else -> {
                "Pulsa"
            }
        }
        return v
    }
}