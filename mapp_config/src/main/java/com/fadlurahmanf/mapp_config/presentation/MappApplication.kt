package com.fadlurahmanf.mapp_config.presentation

import android.app.Application
import com.fadlurahmanf.mapp_config.DaggerMappComponent
import com.fadlurahmanf.mapp_config.MappComponent
import com.fadlurahmanf.mapp_config.domain.di.IMappComponentProvider

class MappApplication : Application(), IMappComponentProvider {
    private lateinit var mappComponent: MappComponent
    override fun onCreate() {
        super.onCreate()
        initInjection()
    }

    private fun initInjection() {
        mappComponent = DaggerMappComponent.factory().create(this)
        mappComponent.inject(this)
    }

    override fun provideMappComponent(): MappComponent {
        return if (this::mappComponent.isInitialized) {
            mappComponent
        } else {
            mappComponent = DaggerMappComponent.factory().create(this)
            mappComponent.inject(this)
            mappComponent
        }
    }
}