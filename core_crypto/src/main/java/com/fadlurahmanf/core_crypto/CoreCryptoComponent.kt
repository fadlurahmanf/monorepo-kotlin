package com.fadlurahmanf.core_crypto

import com.fadlurahmanf.core_crypto.domain.repositories.CryptoAESRepository
import com.fadlurahmanf.core_crypto.domain.repositories.CryptoRSARepository
import dagger.Component

@Component(modules = [CoreCryptoModule::class])
interface CoreCryptoComponent {

    fun provideCryptoRSARepository():CryptoRSARepository
    fun provideCryptoAESRepository():CryptoAESRepository

    @Component.Factory
    interface Factory {
        fun create(): CoreCryptoComponent
    }
}