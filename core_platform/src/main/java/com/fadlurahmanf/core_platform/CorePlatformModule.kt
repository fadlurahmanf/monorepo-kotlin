package com.fadlurahmanf.core_platform

import com.fadlurahmanf.core_platform.domain.BiometricFakeRepositoryImpl
import com.fadlurahmanf.core_platform.domain.BiometricRepository
import com.fadlurahmanf.core_platform.domain.BiometricRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module()
class CorePlatformModule {
    @Provides
    fun provideBiometricRepository(): BiometricRepository {
        return BiometricRepositoryImpl()
    }
}

@Module()
class CorePlatformFakeModule {
    @Provides
    fun provideBiometricRepository(): BiometricRepository {
        return BiometricFakeRepositoryImpl()
    }
}
