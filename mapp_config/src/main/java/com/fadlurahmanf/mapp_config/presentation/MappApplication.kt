package com.fadlurahmanf.mapp_config.presentation

import android.app.Application
import com.fadlurahmanf.core_platform.CorePlatformComponent
import com.fadlurahmanf.core_platform.DaggerCorePlatformComponent
import com.fadlurahmanf.mapp_api.DaggerMappApiComponent
import com.fadlurahmanf.mapp_api.MappApiComponent
import com.fadlurahmanf.mapp_config.DaggerMappConfigComponent
import com.fadlurahmanf.mapp_config.MappConfigComponent
import com.fadlurahmanf.mapp_config.domain.di.IMappComponentProvider
import com.fadlurahmanf.mapp_firebase_database.DaggerMappFirebaseDatabaseComponent
import com.fadlurahmanf.mapp_firebase_database.MappFirebaseDatabaseComponent

class MappApplication : Application(), IMappComponentProvider {
    private lateinit var corePlatformComponent: CorePlatformComponent
    private lateinit var mappComponent: MappConfigComponent
    private lateinit var mappApiComponent: MappApiComponent
    private lateinit var mappFirebaseDatabaseComponent: MappFirebaseDatabaseComponent

    override fun provideCorePlatformComponent(): CorePlatformComponent {
        return if (this::corePlatformComponent.isInitialized) {
            corePlatformComponent
        } else {
            corePlatformComponent = DaggerCorePlatformComponent.factory().create()
            corePlatformComponent
        }
    }

    override fun provideMappComponent(): MappConfigComponent {
        return if (this::mappComponent.isInitialized) {
            mappComponent
        } else {
            mappComponent = DaggerMappConfigComponent.factory().create(this)
            mappComponent.inject(this)
            mappComponent
        }
    }

//    override fun provideMappApiComponent(): MappApiComponent {
//        return if (this::mappApiComponent.isInitialized) {
//            mappApiComponent
//        } else {
//            mappApiComponent = DaggerMappApiComponent.factory().create(this)
//            mappApiComponent
//        }
//    }

    override fun provideMappFirebaseDatabaseComponent(): MappFirebaseDatabaseComponent {
        return if (this::mappFirebaseDatabaseComponent.isInitialized) {
            mappFirebaseDatabaseComponent
        } else {
            mappFirebaseDatabaseComponent = DaggerMappFirebaseDatabaseComponent.factory().create()
            mappFirebaseDatabaseComponent
        }
    }
}