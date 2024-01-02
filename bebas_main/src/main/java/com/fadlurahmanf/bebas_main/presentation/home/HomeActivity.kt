package com.fadlurahmanf.bebas_main.presentation.home

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.databinding.ActivityHomeBinding
import com.fadlurahmanf.bebas_main.presentation.BaseMainActivity
import com.fadlurahmanf.bebas_main.presentation.home.home.HomeFragment
import com.fadlurahmanf.bebas_shared.RxBus
import com.fadlurahmanf.bebas_shared.RxEvent

class HomeActivity : BaseMainActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    val homeFragment = HomeFragment()
    val historyFragment = HistoryFragment()
    val fragmentManager = supportFragmentManager
    var activeIndexFragment: Int = -1
    var activeFragment: Fragment? = null

    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        loadFragment(homeFragment, "0", 0)
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(homeFragment, "0", 0)
                    true
                }

                else -> {
                    loadFragment(historyFragment, "1", 1)
                    true
                }
            }
        }

        binding.fab.setOnClickListener {
            RxBus.publish(
                RxEvent.ResetTimerForceLogout(
                    expiresIn = 5,
                    refreshExpiresIn = 5
                )
            )
        }

        setStatusBarTextColor(true)
        setStatusBarColor()
    }

    private fun loadFragment(fragment: Fragment, tag: String, position: Int) {
        if (activeIndexFragment == position) return
        val existingFragment = fragmentManager.findFragmentByTag(tag)

        if (existingFragment?.isAdded == true && activeFragment != null) {
            Log.d("BebasLogger", "MASUK IF 1 $tag")
            fragmentManager.beginTransaction().hide(activeFragment!!).show(fragment).commit()
        } else if (activeFragment != null) {
            Log.d("BebasLogger", "MASUK IF 2 $tag")
            fragmentManager.beginTransaction().hide(activeFragment!!).add(R.id.fl, fragment, tag)
                .commit()
        } else {
            Log.d("BebasLogger", "MASUK IF 3 $tag")
            fragmentManager.beginTransaction().add(R.id.fl, fragment, tag).commit()
        }

        activeFragment = fragment
        activeIndexFragment = position
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun setStatusBarTextColor(isLight: Boolean) {
        val windowInsetController = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetController?.isAppearanceLightStatusBars = isLight
    }

    fun setStatusBarColor() {
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        window.statusBarColor = resources.getColor(R.color.background_home_color)
    }
}