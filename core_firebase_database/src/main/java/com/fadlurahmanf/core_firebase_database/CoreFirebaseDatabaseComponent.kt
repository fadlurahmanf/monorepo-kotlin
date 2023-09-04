package com.fadlurahmanf.core_firebase_database

import com.fadlurahmanf.core_firebase_database.domain.FirebaseDatabaseRepository
import dagger.Component

@Component(
    modules = [
        CoreFirebaseDatabaseModule::class
    ]
)
interface CoreFirebaseDatabaseComponent {
    fun provideFirebaseDatabaseRepository(): FirebaseDatabaseRepository

    @Component.Factory
    interface Factory {
        fun create(): CoreFirebaseDatabaseComponent
    }
}