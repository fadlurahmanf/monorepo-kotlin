package com.fadlurahmanf.bebas_transaction.domain.di

import android.content.Context
import com.fadlurahmanf.bebas_storage.BebasStorageComponent
import com.fadlurahmanf.bebas_transaction.presentation.others.BankListActivity
import com.fadlurahmanf.bebas_transaction.presentation.favorite.FavoriteListActivity
import com.fadlurahmanf.bebas_transaction.presentation.transfer.TransferConfirmationBottomsheet
import com.fadlurahmanf.bebas_transaction.presentation.transfer.TransferDetailActivity
import com.fadlurahmanf.core_crypto.CoreCryptoComponent
import com.fadlurahmanf.core_platform.CorePlatformComponent
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [
        CoreCryptoComponent::class,
        CorePlatformComponent::class,
        BebasStorageComponent::class
    ]
)
interface BebasTransactionComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            cryptoComponent: CoreCryptoComponent,
            platformComponent: CorePlatformComponent,
            bebasStorageComponent: BebasStorageComponent
        ): BebasTransactionComponent
    }

    fun inject(activity: FavoriteListActivity)
    fun inject(activity: BankListActivity)
    fun inject(activity: TransferDetailActivity)
    fun inject(botttomsheet: TransferConfirmationBottomsheet)
}