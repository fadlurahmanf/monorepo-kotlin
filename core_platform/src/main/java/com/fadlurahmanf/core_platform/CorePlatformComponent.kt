package com.fadlurahmanf.core_platform

import com.fadlurahmanf.core_platform.domain.repositories.DeviceRepository
import dagger.Component

@Component(modules = [CorePlatformModule::class])
interface CorePlatformComponent {
    fun provideBiometric(): DeviceRepository

    @Component.Factory
    interface Factory {
        fun create(): CorePlatformComponent
    }
}