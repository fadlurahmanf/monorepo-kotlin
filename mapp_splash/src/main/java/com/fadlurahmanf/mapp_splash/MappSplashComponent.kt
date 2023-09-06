package com.fadlurahmanf.mapp_splash

import com.fadlurahmanf.mapp_config.MappConfigComponent
import com.fadlurahmanf.mapp_splash.presentation.SplashActivity
import dagger.Component

@Component(
    dependencies = [MappConfigComponent::class]
)
interface MappSplashComponent {
    @Component.Factory
    interface Factory {
        fun create(mapp: MappConfigComponent): MappSplashComponent
    }

    fun inject(splashActivity: SplashActivity)
}