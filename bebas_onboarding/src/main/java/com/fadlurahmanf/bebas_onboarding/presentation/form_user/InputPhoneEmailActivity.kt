package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityInputPhoneEmailBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_onboarding.presentation.otp.OtpVerificationActivity
import com.fadlurahmanf.bebas_shared.data.enum_class.OnboardingFlow
import com.fadlurahmanf.bebas_shared.data.enum_class.OnboardingFlow.*
import javax.inject.Inject

class InputPhoneEmailActivity : BaseOnboardingActivity<ActivityInputPhoneEmailBinding>(ActivityInputPhoneEmailBinding::inflate) {

    @Inject
    lateinit var viewModel: InputPhoneEmailViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        viewModel.state.observe(this){
            when(it){
                true -> {
                    goToOtp()
                }

                else -> {

                }
            }
        }

        binding.btnNext.setOnClickListener {
            viewModel.process(binding.etPhone.text, binding.etEmail.text)
        }
    }

    private fun otpLauncher() = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

    }

    private fun goToOtp(){
        val intent = Intent(this, OtpVerificationActivity::class.java)
        intent.apply {
            putExtra(OtpVerificationActivity.PHONE_NUMBER_ARG, binding.etPhone.text)
        }
        otpLauncher().launch(intent)
    }
}