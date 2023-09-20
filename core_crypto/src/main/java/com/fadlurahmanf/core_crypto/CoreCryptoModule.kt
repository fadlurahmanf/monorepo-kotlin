package com.fadlurahmanf.core_crypto

import com.fadlurahmanf.core_crypto.domain.repositories.CryptoRSARSARepositoryImpl
import com.fadlurahmanf.core_crypto.domain.repositories.CryptoRSARepository
import dagger.Module
import dagger.Provides

@Module
class CoreCryptoModule {

    @Provides
    fun provideCryptoRSARepository(): CryptoRSARepository {
        return CryptoRSARSARepositoryImpl()
    }
}