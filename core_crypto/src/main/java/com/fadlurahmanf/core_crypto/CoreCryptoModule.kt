package com.fadlurahmanf.core_crypto

import com.fadlurahmanf.core_crypto.domain.repositories.CryptoAESRepository
import com.fadlurahmanf.core_crypto.domain.repositories.CryptoAESRepositoryImpl
import com.fadlurahmanf.core_crypto.domain.repositories.CryptoED25119Repository
import com.fadlurahmanf.core_crypto.domain.repositories.CryptoED25119RepositoryImpl
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

    @Provides
    fun provideCryptoAESRepository(): CryptoAESRepository {
        return CryptoAESRepositoryImpl()
    }

    @Provides
    fun provideCryptoED25119Repository(): CryptoED25119Repository {
        return CryptoED25119RepositoryImpl()
    }
}