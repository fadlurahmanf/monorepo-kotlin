package com.fadlurahmanf.mapp_config.presentation

import android.app.Application
import com.fadlurahmanf.core_platform.CorePlatformComponent
import com.fadlurahmanf.core_platform.DaggerCorePlatformComponent
import com.fadlurahmanf.mapp_config.DaggerMappConfigComponent
import com.fadlurahmanf.mapp_config.MappConfigComponent
import com.fadlurahmanf.mapp_config.domain.di.IMappComponentProvider

class MappApplication : Application(), IMappComponentProvider {
    private lateinit var mappComponent: MappConfigComponent
    private lateinit var corePlatformComponent: CorePlatformComponent

    override fun provideMappComponent(): MappConfigComponent {
        return if (this::mappComponent.isInitialized) {
            mappComponent
        } else {
            mappComponent = DaggerMappConfigComponent.factory().create(this)
            mappComponent.inject(this)
            mappComponent
        }
    }

    override fun provideCorePlatformComponent(): CorePlatformComponent {
        return if (this::corePlatformComponent.isInitialized) {
            corePlatformComponent
        } else {
            corePlatformComponent = DaggerCorePlatformComponent.factory().create()
            corePlatformComponent
        }
    }
}