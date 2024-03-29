package com.fadlurahmanf.bebas_onboarding.domain.di

import android.content.Context
import com.fadlurahmanf.bebas_fcm.domain.di.BebasFcmComponent
import com.fadlurahmanf.bebas_onboarding.presentation.camera_verification.EktpVerificationCameraActivity
import com.fadlurahmanf.bebas_onboarding.presentation.camera_verification.EktpVerificationCameraResultActivity
import com.fadlurahmanf.bebas_onboarding.presentation.camera_verification.FaceVerificationActivity
import com.fadlurahmanf.bebas_onboarding.presentation.email.EmailVerificationActivity
import com.fadlurahmanf.bebas_onboarding.presentation.form_user.EktpVerificationFormActivity
import com.fadlurahmanf.bebas_onboarding.presentation.form_user.InputPhoneEmailActivity
import com.fadlurahmanf.bebas_onboarding.presentation.form_user.PrepareOnboardingActivity
import com.fadlurahmanf.bebas_onboarding.presentation.login.LoginActivity
import com.fadlurahmanf.bebas_onboarding.presentation.otp.OtpVerificationActivity
import com.fadlurahmanf.bebas_onboarding.presentation.splash.BebasSplashActivity
import com.fadlurahmanf.bebas_onboarding.presentation.vc.DebugVideoCallActivity
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
        BebasFcmComponent::class,
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
            bebasFcmComponent: BebasFcmComponent,
            bebasStorageComponent: BebasStorageComponent
        ): BebasOnboardingComponent
    }

    fun inject(activity: BebasSplashActivity)
    fun inject(activity: WelcomeOnboardingActivity)
    fun inject(activity: TncActivity)
    fun inject(activity: OtpVerificationActivity)
    fun inject(activity: InputPhoneEmailActivity)
    fun inject(activity: EmailVerificationActivity)
    fun inject(activity: PrepareOnboardingActivity)
    fun inject(activity: EktpVerificationCameraActivity)
    fun inject(activity: FaceVerificationActivity)
    fun inject(activity: EktpVerificationCameraResultActivity)
    fun inject(activity: EktpVerificationFormActivity)
    fun inject(activity: DebugVideoCallActivity)
    fun inject(activity: LoginActivity)
}