package com.fadlurahmanf.mapp_config

import android.content.Context
import com.fadlurahmanf.mapp_config.presentation.MappApplication
import dagger.BindsInstance
import dagger.Component

@Component
interface MappComponent {
    fun inject(app: MappApplication)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): MappComponent
    }
}