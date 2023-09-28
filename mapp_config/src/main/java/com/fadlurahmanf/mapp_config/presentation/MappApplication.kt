package com.fadlurahmanf.mapp_config.presentation

import android.app.Application
import com.fadlurahmanf.core_crypto.CoreCryptoComponent
import com.fadlurahmanf.core_crypto.DaggerCoreCryptoComponent
import com.fadlurahmanf.core_platform.CorePlatformComponent
import com.fadlurahmanf.core_platform.DaggerCorePlatformComponent
import com.fadlurahmanf.mapp_config.DaggerMappConfigComponent
import com.fadlurahmanf.mapp_config.MappConfigComponent
import com.fadlurahmanf.mapp_config.domain.di.IMappComponentProvider
import com.fadlurahmanf.mapp_fcm.DaggerMappFcmComponent
import com.fadlurahmanf.mapp_fcm.MappFcmComponent
import com.fadlurahmanf.mapp_firebase_database.DaggerMappFirebaseDatabaseComponent
import com.fadlurahmanf.mapp_firebase_database.MappFirebaseDatabaseComponent

class MappApplication : Application(), IMappComponentProvider {
    private lateinit var coreCryptoComponent: CoreCryptoComponent
    private lateinit var corePlatformComponent: CorePlatformComponent
    private lateinit var mappConfigComponent: MappConfigComponent
    private lateinit var mappFcmComponent: MappFcmComponent
    private lateinit var mappFirebaseDatabaseComponent: MappFirebaseDatabaseComponent

    override fun provideCoreCryptoComponent(): CoreCryptoComponent {
        return if (this::coreCryptoComponent.isInitialized) {
            coreCryptoComponent
        } else {
            coreCryptoComponent = DaggerCoreCryptoComponent.factory().create()
            coreCryptoComponent
        }
    }

    override fun provideCorePlatformComponent(): CorePlatformComponent {
        return if (this::corePlatformComponent.isInitialized) {
            corePlatformComponent
        } else {
            corePlatformComponent = DaggerCorePlatformComponent.factory().create()
            corePlatformComponent
        }
    }

    override fun provideMappConfigComponent(): MappConfigComponent {
        return if (this::mappConfigComponent.isInitialized) {
            mappConfigComponent
        } else {
            mappConfigComponent = DaggerMappConfigComponent.factory().create(this)
            mappConfigComponent.inject(this)
            mappConfigComponent
        }
    }

    override fun provideMappFcmComponent(): MappFcmComponent {
        return if (this::mappFcmComponent.isInitialized) {
            mappFcmComponent
        } else {
            mappFcmComponent = DaggerMappFcmComponent.factory().create()
            mappFcmComponent
        }
    }

    override fun provideMappFirebaseDatabaseComponent(): MappFirebaseDatabaseComponent {
        return if (this::mappFirebaseDatabaseComponent.isInitialized) {
            mappFirebaseDatabaseComponent
        } else {
            mappFirebaseDatabaseComponent = DaggerMappFirebaseDatabaseComponent.factory().create()
            mappFirebaseDatabaseComponent
        }
    }
}