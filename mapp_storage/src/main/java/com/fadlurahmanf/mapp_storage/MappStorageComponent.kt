package com.fadlurahmanf.mapp_storage

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@Component
interface MappStorageComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): MappStorageComponent
    }
}