package com.fadlurahmanf.core_platform

import com.fadlurahmanf.core_platform.domain.BiometricRepository
import com.fadlurahmanf.core_platform.domain.BiometricRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class CorePlatformModule {
    @Provides
    fun provideBiometricRepository(): BiometricRepository {
        return BiometricRepositoryImpl()
    }
}

