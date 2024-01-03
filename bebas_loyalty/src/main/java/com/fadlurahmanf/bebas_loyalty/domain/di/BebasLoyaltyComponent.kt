package com.fadlurahmanf.bebas_loyalty.domain.di

import android.content.Context
import com.fadlurahmanf.bebas_loyalty.presentation.history.HistoryLoyaltyActivity
import com.fadlurahmanf.bebas_loyalty.presentation.history.HistoryLoyaltyFragment
import com.fadlurahmanf.core_platform.CorePlatformComponent
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [
        CorePlatformComponent::class,
    ]
)
interface BebasLoyaltyComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            platformComponent: CorePlatformComponent,
        ): BebasLoyaltyComponent
    }

    fun inject(activity: HistoryLoyaltyActivity)
    fun inject(fragment: HistoryLoyaltyFragment)
}