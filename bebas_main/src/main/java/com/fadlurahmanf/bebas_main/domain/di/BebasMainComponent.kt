package com.fadlurahmanf.bebas_main.domain.di

import android.content.Context
import com.fadlurahmanf.bebas_main.presentation.home.history.HistoryFragment
import com.fadlurahmanf.bebas_main.presentation.home.HomeActivity
import com.fadlurahmanf.bebas_main.presentation.home.history.EStatementFragment
import com.fadlurahmanf.bebas_main.presentation.home.history.MutationFragment
import com.fadlurahmanf.bebas_main.presentation.home.home.HomeFragment
import com.fadlurahmanf.bebas_main.presentation.notification.NotificationActivity
import com.fadlurahmanf.bebas_main.presentation.notification.NotificationInformationFragment
import com.fadlurahmanf.bebas_main.presentation.notification.NotificationTransactionFragment
import com.fadlurahmanf.bebas_storage.BebasStorageComponent
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
interface BebasMainComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            cryptoComponent: CoreCryptoComponent,
            platformComponent: CorePlatformComponent,
            bebasStorageComponent: BebasStorageComponent
        ): BebasMainComponent
    }

    fun inject(fragment: HomeFragment)
    fun inject(fragment: HistoryFragment)
    fun inject(fragment: NotificationTransactionFragment)
    fun inject(fragment: NotificationInformationFragment)
    fun inject(fragment: MutationFragment)
    fun inject(fragment: EStatementFragment)

    fun inject(activity: HomeActivity)
    fun inject(activity: NotificationActivity)
}