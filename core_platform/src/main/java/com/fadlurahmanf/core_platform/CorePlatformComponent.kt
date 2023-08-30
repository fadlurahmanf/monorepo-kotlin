package com.fadlurahmanf.core_platform

import com.fadlurahmanf.core_platform.domain.BiometricRepository
import dagger.Component

@Component(modules = [CorePlatformModule::class])
interface CorePlatformComponent {
    fun provideBiometric(): BiometricRepository

    @Component.Factory
    interface Factory {
        fun create(): CorePlatformComponent
    }
}