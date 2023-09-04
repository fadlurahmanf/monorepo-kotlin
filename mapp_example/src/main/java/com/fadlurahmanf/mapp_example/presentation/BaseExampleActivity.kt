package com.fadlurahmanf.mapp_example.presentation

import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.mapp_config.helper.di.CoreInjectHelper
import com.fadlurahmanf.mapp_config.presentation.BaseMappActivity
import com.fadlurahmanf.mapp_config.presentation.MappInflateActivity
import com.fadlurahmanf.mapp_example.DaggerMappExampleComponent
import com.fadlurahmanf.mapp_example.MappExampleComponent

abstract class BaseExampleActivity<VB : ViewBinding>(
    inflater: MappInflateActivity<VB>
) : BaseMappActivity<VB>(inflater) {
    lateinit var component: MappExampleComponent
    override fun initComponent() {
        component = DaggerMappExampleComponent.factory()
            .create(
                CoreInjectHelper.provideCorePlatformComponent(applicationContext),
                CoreInjectHelper.provideMappComponent(applicationContext),
                CoreInjectHelper.provideMappFirebaseDatabaseComponent(applicationContext),
            )
    }

}

