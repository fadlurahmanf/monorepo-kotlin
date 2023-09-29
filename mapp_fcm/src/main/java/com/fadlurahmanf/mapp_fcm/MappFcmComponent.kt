package com.fadlurahmanf.mapp_fcm

import com.fadlurahmanf.mapp_fcm.domain.repositories.MappFcmRepository
import dagger.Component

@Component(modules = [MappFcmModule::class])
interface MappFcmComponent {

    fun provideFcmRepository(): MappFcmRepository

    @Component.Factory
    interface Factory {
        fun create(): MappFcmComponent
    }
}