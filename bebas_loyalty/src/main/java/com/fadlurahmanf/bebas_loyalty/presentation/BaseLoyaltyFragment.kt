package com.fadlurahmanf.bebas_loyalty.presentation

import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.bebas_config.presentation.BebasApplication
import com.fadlurahmanf.bebas_loyalty.domain.di.BebasLoyaltyComponent
import com.fadlurahmanf.bebas_loyalty.domain.di.DaggerBebasLoyaltyComponent
import com.fadlurahmanf.bebas_ui.fragment.BaseBebasFragment
import com.fadlurahmanf.bebas_ui.fragment.BebasInflateFragment
import com.fadlurahmanf.core_platform.DaggerCorePlatformComponent

abstract class BaseLoyaltyFragment<VB : ViewBinding>(inflate: BebasInflateFragment<VB>) :
    BaseBebasFragment<VB>(inflate) {

    lateinit var component: BebasLoyaltyComponent

    override fun initComponent() {
        component = DaggerBebasLoyaltyComponent.factory()
            .create(
                requireActivity().applicationContext,
                DaggerCorePlatformComponent.factory().create()
            )
    }

    fun logConsole() = (requireActivity().applicationContext as BebasApplication).logConsole
}