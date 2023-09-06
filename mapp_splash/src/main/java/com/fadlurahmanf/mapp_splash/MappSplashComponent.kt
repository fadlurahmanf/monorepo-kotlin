package com.fadlurahmanf.mapp_splash

import android.content.Context
import com.fadlurahmanf.mapp_config.MappConfigComponent
import com.fadlurahmanf.mapp_splash.presentation.SplashActivity
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [MappConfigComponent::class]
)
interface MappSplashComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context, mapp: MappConfigComponent): MappSplashComponent
    }

    fun inject(splashActivity: SplashActivity)
}