package com.fadlurahmanf.core_logger

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@Component
interface CoreLoggerComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): CoreLoggerComponent
    }
}