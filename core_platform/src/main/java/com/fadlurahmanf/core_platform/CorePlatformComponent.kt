package com.fadlurahmanf.core_platform

import dagger.Component
import dagger.Subcomponent

@Component(modules = [CorePlatformModule::class])
interface CorePlatformComponent {
    @Component.Factory
    interface Factory {
        fun create(): CorePlatformComponent
    }
}