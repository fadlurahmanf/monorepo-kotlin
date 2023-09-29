package com.fadlurahmanf.mapp_splash.presentation

import androidx.viewbinding.ViewBinding
import com.fadlurahmanf.mapp_api.data.exception.MappException
import com.fadlurahmanf.mapp_splash.DaggerMappSplashComponent
import com.fadlurahmanf.mapp_splash.MappSplashComponent
import com.fadlurahmanf.mapp_ui.presentation.activity.BaseMappActivity
import com.fadlurahmanf.mapp_ui.presentation.activity.MappInflateActivity

abstract class BaseMappSplashActivity<VB : ViewBinding>(
    inflater: MappInflateActivity<VB>
) : BaseMappActivity<VB>(inflater) {
    lateinit var component: MappSplashComponent
    override fun initComponent() {
        component = DaggerMappSplashComponent.factory().create(applicationContext)
    }

    fun showBaseSplashFailedBottomSheet(exception: MappException) {
        showFailedBottomsheet(
            title = exception.toProperTitle(this),
            desc = exception.toProperMessage(this),
            buttonText = exception.toProperButtonText(this)
        )
    }

}

