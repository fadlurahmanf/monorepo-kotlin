package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityPrepareOnboardingBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_onboarding.presentation.camera_verification.EktpVerificationActivity
import javax.inject.Inject

class PrepareOnboardingActivity :
    BaseOnboardingActivity<ActivityPrepareOnboardingBinding>(ActivityPrepareOnboardingBinding::inflate) {

    @Inject
    lateinit var viewModel: PrepareOnboardingViewModel
    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        binding.btnNext.setOnClickListener {
            viewModel.updateIsFinishedPreparedOnBoarding(true)
            val intent = Intent(this, EktpVerificationActivity::class.java)
            startActivity(intent)
        }
    }
}