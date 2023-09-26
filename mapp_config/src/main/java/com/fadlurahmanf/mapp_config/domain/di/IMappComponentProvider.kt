package com.fadlurahmanf.mapp_config.domain.di

import com.fadlurahmanf.core_crypto.CoreCryptoComponent
import com.fadlurahmanf.core_platform.CorePlatformComponent
import com.fadlurahmanf.mapp_config.MappConfigComponent
import com.fadlurahmanf.mapp_firebase_database.MappFirebaseDatabaseComponent

interface IMappComponentProvider {
    fun provideCoreCryptoComponent(): CoreCryptoComponent
    fun provideCorePlatformComponent(): CorePlatformComponent
    fun provideMappComponent(): MappConfigComponent
    fun provideMappFirebaseDatabaseComponent(): MappFirebaseDatabaseComponent
}