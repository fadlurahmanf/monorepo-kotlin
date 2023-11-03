package com.fadlurahmanf.core_platform

import com.fadlurahmanf.core_platform.domain.repositories.DeviceRepository
import com.fadlurahmanf.core_platform.domain.repositories.DeviceRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class CorePlatformModule {
    @Provides
    fun provideBiometricRepository(): DeviceRepository {
        return DeviceRepositoryImpl()
    }
}

