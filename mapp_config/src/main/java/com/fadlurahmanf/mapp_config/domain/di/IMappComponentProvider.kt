package com.fadlurahmanf.mapp_config.domain.di

import com.fadlurahmanf.core_platform.CorePlatformComponent
import com.fadlurahmanf.mapp_api.MappApiComponent
import com.fadlurahmanf.mapp_config.MappConfigComponent
import com.fadlurahmanf.mapp_firebase_database.MappFirebaseDatabaseComponent

interface IMappComponentProvider {
    fun provideCorePlatformComponent(): CorePlatformComponent
    fun provideMappComponent(): MappConfigComponent
    fun provideMappFirebaseDatabaseComponent(): MappFirebaseDatabaseComponent
}