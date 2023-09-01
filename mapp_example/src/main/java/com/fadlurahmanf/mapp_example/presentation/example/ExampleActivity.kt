package com.fadlurahmanf.mapp_example.presentation.example

import android.content.Intent
import com.fadlurahmanf.mapp_example.R
import com.fadlurahmanf.mapp_example.data.dto.model.MenuModel
import com.fadlurahmanf.mapp_example.databinding.ActivityExampleBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import com.fadlurahmanf.mapp_example.presentation.biometric.BiometricActivity
import com.fadlurahmanf.mapp_example.presentation.notification.NotificationActivity

class ExampleActivity : BaseExampleActivity<ActivityExampleBinding>(
    ActivityExampleBinding::inflate
), MenuRecycleView.CallBack {

    companion object {
        val menus = arrayListOf<MenuModel>(
            MenuModel(
                menuId = "BIOMETRIC",
                menuTitle = "Biometric",
                menuSubTitle = "Go To Biometric",
                icon = R.drawable.outline_featured_play_list_24
            ),
            MenuModel(
                menuId = "NOTIFICATION",
                menuTitle = "Notification",
                menuSubTitle = "Go To Notification",
                icon = R.drawable.outline_featured_play_list_24
            ),
        )
    }

    override fun setup() {
        initAdapter()
    }

    private lateinit var adapter: MenuRecycleView
    private fun initAdapter() {
        adapter = MenuRecycleView()
        adapter.setList(menus)
        adapter.setCallBack(this)
        binding.rv.adapter = adapter
    }

    override fun injectActivity() {
        component.inject(this)
    }

    override fun onMenuClicked(menu: MenuModel) {
        if (menu.menuId == "BIOMETRIC") {
            val intent = Intent(this, BiometricActivity::class.java)
            startActivity(intent)
        } else if (menu.menuId == "NOTIFICATION") {
            val intent = Intent(this, NotificationActivity::class.java)
            startActivity(intent)
        }
    }
}