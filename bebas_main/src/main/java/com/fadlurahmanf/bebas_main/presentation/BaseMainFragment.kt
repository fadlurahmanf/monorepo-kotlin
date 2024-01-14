package com.fadlurahmanf.bebas_main.presentation

import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.bebas_config.presentation.BebasApplication
import com.fadlurahmanf.bebas_main.domain.di.BebasMainComponent
import com.fadlurahmanf.bebas_main.domain.di.DaggerBebasMainComponent
import com.fadlurahmanf.bebas_storage.DaggerBebasStorageComponent
import com.fadlurahmanf.bebas_ui.fragment.BaseBebasFragment
import com.fadlurahmanf.bebas_ui.fragment.BebasInflateFragment
import com.fadlurahmanf.core_crypto.DaggerCoreCryptoComponent
import com.fadlurahmanf.core_platform.DaggerCorePlatformComponent

abstract class BaseMainFragment<VB : ViewBinding>(inflate: BebasInflateFragment<VB>) :
    BaseBebasFragment<VB>(inflate) {

    lateinit var component: BebasMainComponent

    override fun initComponent() {
        val cryptoComponent = DaggerCoreCryptoComponent.factory().create()
        component = DaggerBebasMainComponent.factory()
            .create(
                requireActivity().applicationContext,
                cryptoComponent,
                DaggerCorePlatformComponent.factory().create(),
                DaggerBebasStorageComponent.factory()
                    .create(requireActivity().applicationContext, cryptoComponent)
            )
    }

    fun logConsole() = (requireActivity().applicationContext as BebasApplication).logConsole
}