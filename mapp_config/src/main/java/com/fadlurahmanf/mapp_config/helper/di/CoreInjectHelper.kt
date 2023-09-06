package com.fadlurahmanf.mapp_config.helper.di

import android.content.Context
import com.fadlurahmanf.core_platform.CorePlatformComponent
import com.fadlurahmanf.mapp_config.MappConfigComponent
import com.fadlurahmanf.mapp_config.domain.di.IMappComponentProvider
import com.fadlurahmanf.mapp_firebase_database.MappFirebaseDatabaseComponent

object CoreInjectHelper {

    fun provideCorePlatformComponent(applicationContext: Context): CorePlatformComponent {
        if (applicationContext is IMappComponentProvider) {
            return (applicationContext as IMappComponentProvider).provideCorePlatformComponent()
        } else {
            throw IllegalStateException("application context should be IMappComponentProvider")
        }
    }
    fun provideMappComponent(applicationContext: Context): MappConfigComponent {
        if (applicationContext is IMappComponentProvider) {
            return (applicationContext as IMappComponentProvider).provideMappComponent()
        } else {
            throw IllegalStateException("application context not contain IMappComponentProvider")
        }
    }

    fun provideMappFirebaseDatabaseComponent(applicationContext: Context):MappFirebaseDatabaseComponent {
        if (applicationContext is IMappComponentProvider) {
            return (applicationContext as IMappComponentProvider).provideMappFirebaseDatabaseComponent()
        } else {
            throw IllegalStateException("application context should be IMappComponentProvider")
        }
    }
}