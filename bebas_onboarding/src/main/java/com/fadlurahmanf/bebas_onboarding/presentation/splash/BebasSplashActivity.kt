package com.fadlurahmanf.bebas_onboarding.presentation.splash

import android.content.Intent
import android.os.Bundle
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityBebasSplashBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_onboarding.presentation.login.LoginActivity
import javax.inject.Inject

class BebasSplashActivity :
    BaseOnboardingActivity<ActivityBebasSplashBinding>(ActivityBebasSplashBinding::inflate) {

    @Inject
    lateinit var viewModel: BebasSplashViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        viewModel.state.observe(this) {
            when (it) {
                is SplashState.SUCCESS -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                }

                is SplashState.FAILED -> {
                    showFailedBebasBottomsheet(it.exception)
                }

                else -> {

                }
            }
        }

        viewModel.generateGuestToken()
    }
}