package com.fadlurahmanf.mapp_config.helper.di

import android.content.Context
import com.fadlurahmanf.core_crypto.CoreCryptoComponent
import com.fadlurahmanf.core_platform.CorePlatformComponent
import com.fadlurahmanf.mapp_config.MappConfigComponent
import com.fadlurahmanf.mapp_config.domain.di.IMappComponentProvider
import com.fadlurahmanf.mapp_fcm.MappFcmComponent
import com.fadlurahmanf.mapp_firebase_database.MappFirebaseDatabaseComponent

object CoreInjectHelper {

    fun provideCoreCryptoComponent(applicationContext: Context): CoreCryptoComponent {
        if (applicationContext is IMappComponentProvider) {
            return (applicationContext as IMappComponentProvider).provideCoreCryptoComponent()
        } else {
            throw IllegalStateException("application context should be IMappComponentProvider")
        }
    }

    fun provideCorePlatformComponent(applicationContext: Context): CorePlatformComponent {
        if (applicationContext is IMappComponentProvider) {
            return (applicationContext as IMappComponentProvider).provideCorePlatformComponent()
        } else {
            throw IllegalStateException("application context should be IMappComponentProvider")
        }
    }

    fun provideMappConfigComponent(applicationContext: Context): MappConfigComponent {
        if (applicationContext is IMappComponentProvider) {
            return (applicationContext as IMappComponentProvider).provideMappConfigComponent()
        } else {
            throw IllegalStateException("application context not contain IMappComponentProvider")
        }
    }

    fun provideMappFcmComponent(applicationContext: Context): MappFcmComponent {
        if (applicationContext is IMappComponentProvider) {
            return (applicationContext as IMappComponentProvider).provideMappFcmComponent()
        } else {
            throw IllegalStateException("application context not contain IMappComponentProvider")
        }
    }

    fun provideMappFirebaseDatabaseComponent(applicationContext: Context): MappFirebaseDatabaseComponent {
        if (applicationContext is IMappComponentProvider) {
            return (applicationContext as IMappComponentProvider).provideMappFirebaseDatabaseComponent()
        } else {
            throw IllegalStateException("application context should be IMappComponentProvider")
        }
    }
}