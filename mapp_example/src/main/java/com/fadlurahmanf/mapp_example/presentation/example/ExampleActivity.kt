package com.fadlurahmanf.mapp_example.presentation.example

import android.content.Intent
import com.fadlurahmanf.mapp_example.data.dto.model.MenuModel
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import com.fadlurahmanf.mapp_example.databinding.ActivityExampleBinding
import com.fadlurahmanf.mapp_example.presentation.biometric.BiometricActivity

class ExampleActivity : BaseExampleActivity<ActivityExampleBinding>(
    ActivityExampleBinding::inflate
), MenuRecycleView.CallBack {

    companion object {
        val menus = arrayListOf<MenuModel>(
            MenuModel(
                menuId = "BIOMETRIC",
                menuTitle = "Biometric"
            )
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
        }
    }
}