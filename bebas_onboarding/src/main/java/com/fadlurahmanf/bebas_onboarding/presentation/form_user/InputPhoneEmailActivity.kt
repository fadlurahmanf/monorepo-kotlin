package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import android.content.Intent
import android.text.Editable
import androidx.activity.result.contract.ActivityResultContracts
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityInputPhoneEmailBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_onboarding.presentation.otp.OtpVerificationActivity
import com.fadlurahmanf.bebas_ui.edittext.BebasPhoneNumberEdittext
import com.fadlurahmanf.bebas_shared.state.EditTextFormState
import com.fadlurahmanf.bebas_ui.edittext.BebasEdittext
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
                viewModel.processFormThroughButton = false
                viewModel.setPhone(unformattedText ?: "")
            }

        })

        binding.etEmail.addTextChangedListener(object : BebasEdittext.BebasEdittextTextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                viewModel.processFormThroughButton = false
                viewModel.setEmail(s?.toString() ?: "")
            }

        })

        viewModel.phoneState.observe(this) {
            when (it) {
                is EditTextFormState.FAILED -> {
                    binding.etPhone.setError(getString(it.idRawStringRes, getString(R.string.phone_number)), viewModel.processFormThroughButton)
                }

                is EditTextFormState.EMPTY -> {
                    binding.etPhone.setError(getString(R.string.error_general_message_form_empty, getString(R.string.phone_number)), viewModel.processFormThroughButton)
                }

                is EditTextFormState.SUCCESS -> {
                    binding.etPhone.removeError()
                }
            }
        }

        viewModel.emailState.observe(this) {
            when (it) {
                is EditTextFormState.FAILED -> {
                    binding.etEmail.setError(getString(it.idRawStringRes, getString(R.string.email)), viewModel.processFormThroughButton)
                }

                is EditTextFormState.EMPTY -> {
                    binding.etEmail.setError(getString(R.string.error_general_message_form_empty, getString(R.string.email)), viewModel.processFormThroughButton)
                }

                is EditTextFormState.SUCCESS -> {
                    binding.etEmail.removeError()
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
            currentFocus?.clearFocus()
            viewModel.processFormThroughButton = true
            viewModel.process(
                binding.etPhone.text.replace("\\D".toRegex(), ""),
                binding.etEmail.text
            )
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