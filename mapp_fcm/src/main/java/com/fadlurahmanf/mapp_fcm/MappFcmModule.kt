package com.fadlurahmanf.mapp_fcm

import com.fadlurahmanf.mapp_fcm.domain.repositories.MappFcmRepository
import com.fadlurahmanf.mapp_fcm.domain.repositories.MappFcmRepositoryImpl
import dagger.Module
import dagger.Provides

@Module
class MappFcmModule {

    @Provides
    fun provideFcmRepository(): MappFcmRepository {
        return MappFcmRepositoryImpl()
    }
}