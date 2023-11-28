package com.fadlurahmanf.core_logger.presentation

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.fadlurahmanf.core_logger.R
import com.google.android.material.tabs.TabLayout

class LogHistoryActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    private lateinit var adapter: LogHistoryPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_history)

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)

        tabLayout.addTab(tabLayout.newTab().setText("ALL").setId(0))
        tabLayout.addTab(tabLayout.newTab().setText("DEBUG").setId(1))
        tabLayout.addTab(tabLayout.newTab().setText("INFO").setId(2))
        tabLayout.addTab(tabLayout.newTab().setText("ERROR").setId(3))

        adapter = LogHistoryPagerAdapter(supportFragmentManager, tabLayout.tabCount)

        viewPager.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.id
                tab.select()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })
    }
}