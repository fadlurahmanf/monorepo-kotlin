package com.fadlurahmanf.mapp_example.presentation

import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.mapp_config.helper.di.CoreInjectHelper
import com.fadlurahmanf.mapp_config.presentation.MappApplication
import com.fadlurahmanf.mapp_example.DaggerMappExampleComponent
import com.fadlurahmanf.mapp_example.MappExampleComponent
import com.fadlurahmanf.mapp_ui.presentation.activity.BaseMappActivity
import com.fadlurahmanf.mapp_ui.presentation.activity.MappInflateActivity

abstract class BaseExampleActivity<VB : ViewBinding>(
    inflater: MappInflateActivity<VB>
) : BaseMappActivity<VB>(inflater) {
    lateinit var component: MappExampleComponent
    override fun initComponent() {
        component = DaggerMappExampleComponent.factory()
            .create(
                applicationContext,
                CoreInjectHelper.provideCoreCryptoComponent(applicationContext),
                CoreInjectHelper.provideCorePlatformComponent(applicationContext),
                CoreInjectHelper.provideMappConfigComponent(applicationContext),
                CoreInjectHelper.provideMappFcmComponent(applicationContext),
                CoreInjectHelper.provideMappFirebaseDatabaseComponent(applicationContext),
            )
    }

    fun logConsole() = (applicationContext as MappApplication).logConsole
}

