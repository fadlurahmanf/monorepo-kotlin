package com.fadlurahmanf.bebas_onboarding.domain.di

import android.content.Context
import com.fadlurahmanf.bebas_onboarding.presentation.splash.BebasSplashActivity
import com.fadlurahmanf.core_crypto.CoreCryptoComponent
import com.fadlurahmanf.core_platform.CorePlatformComponent
import dagger.BindsInstance
import dagger.Component

@Component(
    dependencies = [
        CoreCryptoComponent::class,
        CorePlatformComponent::class,
    ]
)
interface BebasOnboardingComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            cryptoComponent: CoreCryptoComponent,
            platformComponent: CorePlatformComponent
        ): BebasOnboardingComponent
    }

    fun inject(activity: BebasSplashActivity)
}