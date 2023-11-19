package com.fadlurahmanf.bebas_onboarding.domain.di

import android.content.Context
import com.fadlurahmanf.bebas_onboarding.presentation.email.EmailVerificationActivity
import com.fadlurahmanf.bebas_onboarding.presentation.form_user.InputPhoneEmailActivity
import com.fadlurahmanf.bebas_onboarding.presentation.otp.OtpVerificationActivity
import com.fadlurahmanf.bebas_onboarding.presentation.splash.BebasSplashActivity
import com.fadlurahmanf.bebas_onboarding.presentation.welcome.TncActivity
import com.fadlurahmanf.bebas_onboarding.presentation.welcome.WelcomeOnboardingActivity
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
interface BebasOnboardingComponent {

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance context: Context,
            cryptoComponent: CoreCryptoComponent,
            platformComponent: CorePlatformComponent,
            bebasStorageComponent: BebasStorageComponent
        ): BebasOnboardingComponent
    }

    fun inject(activity: BebasSplashActivity)
    fun inject(activity: WelcomeOnboardingActivity)
    fun inject(activity: TncActivity)
    fun inject(activity: OtpVerificationActivity)
    fun inject(activity: InputPhoneEmailActivity)
    fun inject(activity: EmailVerificationActivity)
}