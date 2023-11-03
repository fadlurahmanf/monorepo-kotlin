package com.fadlurahmanf.bebas_storage

import android.content.Context
import com.fadlurahmanf.core_crypto.CoreCryptoComponent
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [
        CoreCryptoComponent::class
    ]
)
interface BebasStorageComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            cryptoComponent: CoreCryptoComponent
        ): BebasStorageComponent
    }
}