package com.fadlurahmanf.bebas_onboarding.presentation.email

import com.fadlurahmanf.bebas_api.data.exception.BebasException
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityEmailVerificationBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity

class EmailVerificationActivity :
    BaseOnboardingActivity<ActivityEmailVerificationBinding>(ActivityEmailVerificationBinding::inflate) {
    companion object {
        const val EMAIL_ARG = "EMAIL_ARG"
    }

    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var email: String

    override fun setup() {
        val emailArg = intent.extras?.getString(EMAIL_ARG)

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        if (emailArg == null) {
            showFailedBottomsheet(BebasException.generalRC("EMAIL_MISSING"))
            return
        }

        email = emailArg

        binding.etEmail.text = email
        binding.etEmail.setIsEnabled(false)
    }

}