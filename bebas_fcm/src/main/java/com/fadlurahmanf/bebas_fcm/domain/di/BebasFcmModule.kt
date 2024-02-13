package com.fadlurahmanf.bebas_fcm.domain.di

import com.fadlurahmanf.bebas_fcm.domain.repositories.BebasFcmRepository
import com.fadlurahmanf.bebas_fcm.domain.repositories.BebasFcmRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class BebasFcmModule {
    @Provides
    fun provideBebasFcmRepository(): BebasFcmRepository {
        return BebasFcmRepositoryImpl()
    }
}