package com.fadlurahmanf.bebas_main.presentation.home

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import androidx.fragment.app.Fragment
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.databinding.ActivityHomeBinding
import com.fadlurahmanf.bebas_main.presentation.BaseMainActivity
import com.fadlurahmanf.bebas_main.presentation.home.home.HomeFragment
import com.fadlurahmanf.bebas_main.presentation.notification.NotificationActivity
import com.fadlurahmanf.bebas_shared.data.argument.transaction.FavoriteArgument
import com.fadlurahmanf.bebas_shared.data.flow.transaction.FavoriteFlow

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

        Handler(Looper.getMainLooper()).postDelayed({
                                                        val intent = Intent(
                                                            this,
                                                            NotificationActivity::class.java
                                                        )
                                                        startActivity(intent)
                                                    }, 1500)
    }

    private fun loadFragment(fragment: Fragment, tag: String, position: Int) {
        if (activeIndexFragment == position) return
        val existingFragment = fragmentManager.findFragmentByTag(tag)

        if (existingFragment?.isAdded == true && activeFragment != null) {
            fragmentManager.beginTransaction().hide(activeFragment!!).show(fragment).commit()
        } else if (activeFragment != null) {
            fragmentManager.beginTransaction().hide(activeFragment!!).add(R.id.fl, fragment, tag)
                .commit()
        } else {
            fragmentManager.beginTransaction().add(R.id.fl, fragment, tag).commit()
        }

        activeFragment = fragment
        activeIndexFragment = position
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
}