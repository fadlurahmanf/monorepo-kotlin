package com.fadlurahmanf.bebas_loyalty.presentation

import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.bebas_config.presentation.BebasApplication
import com.fadlurahmanf.bebas_loyalty.domain.di.BebasLoyaltyComponent
import com.fadlurahmanf.bebas_loyalty.domain.di.DaggerBebasLoyaltyComponent
import com.fadlurahmanf.bebas_ui.activity.BaseBebasActivity
import com.fadlurahmanf.bebas_ui.activity.BebasInflateActivity
import com.fadlurahmanf.core_platform.DaggerCorePlatformComponent

abstract class BaseLoyaltyActivity<VB : ViewBinding>(inflate: BebasInflateActivity<VB>) :
    BaseBebasActivity<VB>(inflate) {

    lateinit var component: BebasLoyaltyComponent
    override fun initComponent() {
        component = DaggerBebasLoyaltyComponent.factory()
            .create(
                applicationContext,
                DaggerCorePlatformComponent.factory().create()
            )
    }

    fun logConsole() = (applicationContext as BebasApplication).logConsole
}