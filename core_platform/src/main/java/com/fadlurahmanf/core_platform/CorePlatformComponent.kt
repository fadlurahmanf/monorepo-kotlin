package com.fadlurahmanf.core_platform

import com.fadlurahmanf.core_platform.domain.BiometricRepository
import dagger.Component
import dagger.Subcomponent

@Component(modules = [CorePlatformModule::class])
interface CorePlatformComponent {
    fun biometricRepository(): BiometricRepository

    @Component.Factory
    interface Factory {
        fun create(): CorePlatformComponent
    }
}