package com.fadlurahmanf.core_platform

import dagger.Component
import dagger.Subcomponent

@Subcomponent()
interface CorePlatformComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): CorePlatformComponent
    }
}