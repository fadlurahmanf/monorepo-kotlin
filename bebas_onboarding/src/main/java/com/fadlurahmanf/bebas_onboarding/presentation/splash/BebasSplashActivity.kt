package com.fadlurahmanf.bebas_onboarding.presentation.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityBebasSplashBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_ui.presentation.activity.BaseBebasActivity
import javax.inject.Inject

class BebasSplashActivity :
    BaseOnboardingActivity<ActivityBebasSplashBinding>(ActivityBebasSplashBinding::inflate) {

    @Inject
    lateinit var viewModel: BebasSplashViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
    }
}