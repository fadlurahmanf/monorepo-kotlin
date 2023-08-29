package com.fadlurahmanf.mapp_config.domain.di

import com.fadlurahmanf.core_platform.CorePlatformComponent
import com.fadlurahmanf.mapp_config.MappConfigComponent

interface IMappComponentProvider {
    fun provideMappComponent(): MappConfigComponent
    fun provideCorePlatformComponent(): CorePlatformComponent
}