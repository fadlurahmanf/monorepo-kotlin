package com.fadlurahmanf.bebas_main.presentation.home

import androidx.fragment.app.Fragment
import com.fadlurahmanf.bebas_main.R
import com.fadlurahmanf.bebas_main.databinding.ActivityHomeBinding
import com.fadlurahmanf.bebas_main.presentation.BaseMainActivity

class HomeActivity : BaseMainActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        loadFragment(HomeFragment.newInstance("", ""))
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment.newInstance("", ""))
                    true
                }

                else -> {
                    loadFragment(HomeFragment.newInstance("", ""))
                    true
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fl, fragment)
        transaction.commit()
    }
}