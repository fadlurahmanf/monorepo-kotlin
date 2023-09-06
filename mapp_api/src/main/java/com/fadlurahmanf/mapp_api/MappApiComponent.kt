package com.fadlurahmanf.mapp_api

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@Component
interface MappApiComponent {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): MappApiComponent
    }
}