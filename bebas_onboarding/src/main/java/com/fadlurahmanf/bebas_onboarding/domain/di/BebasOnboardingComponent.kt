package com.fadlurahmanf.bebas_onboarding.domain.di

import android.content.Context
import com.fadlurahmanf.bebas_onboarding.presentation.splash.BebasSplashActivity
import dagger.BindsInstance
import dagger.Component

@Component
interface BebasOnboardingComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): BebasOnboardingComponent
    }

    fun inject(activity: BebasSplashActivity)
}