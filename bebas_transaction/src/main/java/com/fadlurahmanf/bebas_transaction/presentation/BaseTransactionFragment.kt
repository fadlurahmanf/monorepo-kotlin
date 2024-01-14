package com.fadlurahmanf.bebas_transaction.presentation

import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.bebas_config.presentation.BebasApplication
import com.fadlurahmanf.bebas_storage.DaggerBebasStorageComponent
import com.fadlurahmanf.bebas_transaction.domain.di.BebasTransactionComponent
import com.fadlurahmanf.bebas_transaction.domain.di.DaggerBebasTransactionComponent
import com.fadlurahmanf.bebas_ui.fragment.BaseBebasFragment
import com.fadlurahmanf.bebas_ui.fragment.BebasInflateFragment
import com.fadlurahmanf.core_crypto.DaggerCoreCryptoComponent
import com.fadlurahmanf.core_platform.DaggerCorePlatformComponent

abstract class BaseTransactionFragment<VB : ViewBinding>(inflate: BebasInflateFragment<VB>) :
    BaseBebasFragment<VB>(inflate) {

    lateinit var component: BebasTransactionComponent

    override fun initComponent() {
        val cryptoComponent = DaggerCoreCryptoComponent.factory().create()
        component = DaggerBebasTransactionComponent.factory()
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