package com.fadlurahmanf.mapp_splash.presentation

import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.mapp_config.helper.di.CoreInjectHelper
import com.fadlurahmanf.mapp_splash.DaggerMappSplashComponent
import com.fadlurahmanf.mapp_splash.MappSplashComponent
import com.fadlurahmanf.mapp_ui.presentation.activity.BaseMappActivity
import com.fadlurahmanf.mapp_ui.presentation.activity.MappInflateActivity

abstract class BaseMappSplashActivity<VB : ViewBinding>(
    inflater: MappInflateActivity<VB>
) : BaseMappActivity<VB>(inflater) {
    lateinit var component: MappSplashComponent
    override fun initComponent() {
        component = DaggerMappSplashComponent.factory()
            .create(
                applicationContext,
                CoreInjectHelper.provideMappComponent(applicationContext),
            )
    }

}

