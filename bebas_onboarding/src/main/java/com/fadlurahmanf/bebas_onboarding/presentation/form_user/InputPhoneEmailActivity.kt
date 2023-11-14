package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import android.content.Intent
import android.text.Editable
import androidx.activity.result.contract.ActivityResultContracts
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityInputPhoneEmailBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_onboarding.presentation.otp.OtpVerificationActivity
import com.fadlurahmanf.bebas_ui.edittext.BebasPhoneNumberEdittext
import com.fadlurahmanf.bebas_shared.state.EditTextFormState
import javax.inject.Inject

class InputPhoneEmailActivity :
    BaseOnboardingActivity<ActivityInputPhoneEmailBinding>(ActivityInputPhoneEmailBinding::inflate) {

    @Inject
    lateinit var viewModel: InputPhoneEmailViewModel

    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        binding.etPhone.addTextChangedListener(object :
                                                   BebasPhoneNumberEdittext.BebasPhoneNumberEdittextTextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(
                s: Editable?,
                formattedText: String?,
                unformattedText: String?
            ) {
                viewModel.setPhone(unformattedText ?: "")
            }

        })

        viewModel.phoneState.observe(this) {
            when (it) {
                is EditTextFormState.SUCCESS -> {
                    binding.etPhone.setError(null)
                }

                is EditTextFormState.FAILED -> {
                    binding.etPhone.setError(it.errorMessage)
                }

                EditTextFormState.EMPTY -> {
                    binding.etPhone.setError(null)
                }
            }
        }

        viewModel.state.observe(this) {
            when (it) {
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

    private fun otpLauncher() =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }

    private fun goToOtp() {
        val intent = Intent(this, OtpVerificationActivity::class.java)
        intent.apply {
            putExtra(OtpVerificationActivity.PHONE_NUMBER_ARG, binding.etPhone.text)
        }
        otpLauncher().launch(intent)
    }
}