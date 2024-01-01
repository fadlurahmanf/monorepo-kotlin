package com.fadlurahmanf.bebas_fcm.domain.di

import android.content.Context
import com.fadlurahmanf.bebas_fcm.domain.repositories.BebasFcmRepository
import dagger.BindsInstance
import dagger.Component

@Component(modules = [BebasFcmModule::class])
interface BebasFcmComponent {

    fun provideBebasFcmRepository(): BebasFcmRepository

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
        ): BebasFcmComponent
    }
}