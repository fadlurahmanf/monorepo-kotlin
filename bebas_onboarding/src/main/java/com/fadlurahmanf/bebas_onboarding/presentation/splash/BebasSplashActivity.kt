package com.fadlurahmanf.bebas_onboarding.presentation.splash

import android.content.Intent
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityBebasSplashBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_onboarding.presentation.welcome.WelcomeOnboardingActivity
import javax.inject.Inject

class BebasSplashActivity :
    BaseOnboardingActivity<ActivityBebasSplashBinding>(ActivityBebasSplashBinding::inflate) {

    @Inject
    lateinit var viewModel: BebasSplashViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        viewModel.state.observe(this) {
            when (it) {
                is SplashState.SUCCESS -> {
                    val intent = Intent(this, WelcomeOnboardingActivity::class.java)
                    startActivity(intent)
                }

                else -> {

                }
            }
        }

        viewModel.generateGuestToken()
    }
}