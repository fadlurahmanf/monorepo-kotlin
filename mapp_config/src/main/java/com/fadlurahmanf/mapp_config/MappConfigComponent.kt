package com.fadlurahmanf.mapp_config

import android.content.Context
import com.fadlurahmanf.mapp_config.presentation.MappApplication
import dagger.BindsInstance
import dagger.Component


@Component
interface MappConfigComponent {
    fun inject(app:MappApplication)
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): MappConfigComponent
    }
}