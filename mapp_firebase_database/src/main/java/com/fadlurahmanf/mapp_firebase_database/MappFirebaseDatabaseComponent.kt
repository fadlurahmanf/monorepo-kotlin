package com.fadlurahmanf.mapp_firebase_database

import com.fadlurahmanf.mapp_firebase_database.domain.MappFirebaseDatabaseRepositoryImpl
import dagger.Component

@Component
interface MappFirebaseDatabaseComponent {
    fun provideMappFirebaseDatabaseRepository(): MappFirebaseDatabaseRepositoryImpl

    @Component.Factory
    interface Factory {
        fun create(): MappFirebaseDatabaseComponent
    }
}