package com.fadlurahmanf.core_firebase_database

import com.fadlurahmanf.core_firebase_database.domain.FirebaseDatabaseRepository
import com.fadlurahmanf.core_firebase_database.domain.FirebaseDatabaseRepositoryImpl
import dagger.Module

@Module
class CoreFirebaseDatabaseModule {
    fun provideCoreFirebaseDatabaseRepository(): FirebaseDatabaseRepository {
        return FirebaseDatabaseRepositoryImpl()
    }
}