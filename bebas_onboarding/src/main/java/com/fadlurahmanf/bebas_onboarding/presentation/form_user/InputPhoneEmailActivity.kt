package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import android.app.Activity
import android.content.Intent
import android.text.Editable
import androidx.activity.result.contract.ActivityResultContracts
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.data.state.InitInputPhoneAndEmailState
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityInputPhoneEmailBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_onboarding.presentation.otp.OtpVerificationActivity
import com.fadlurahmanf.bebas_ui.edittext.BebasPhoneNumberEdittext
import com.fadlurahmanf.bebas_shared.state.EditTextFormState
import com.fadlurahmanf.bebas_ui.edittext.BebasEdittext
import com.fadlurahmanf.bebas_ui.extension.clearFocusAndDismissKeyboard
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

        initObserver()

        binding.btnNext.setOnClickListener {
            currentFocus?.clearFocusAndDismissKeyboard()
            viewModel.processFormThroughButton = true
            viewModel.process(
                binding.etPhone.text.replace("\\D".toRegex(), ""),
                binding.etEmail.text
            )
        }

        viewModel.initLastStorage()
    }

    private fun initObserver() {
        viewModel.phoneState.observe(this) {
            when (it) {
                is EditTextFormState.FAILED -> {
                    binding.etPhone.setError(
                        getString(
                            it.idRawStringRes,
                            getString(R.string.phone_number)
                        ), viewModel.processFormThroughButton
                    )
                }

                is EditTextFormState.EMPTY -> {
                    binding.etPhone.setError(
                        getString(
                            R.string.error_general_message_form_empty,
                            getString(R.string.phone_number)
                        ), viewModel.processFormThroughButton
                    )
                }

                is EditTextFormState.SUCCESS -> {
                    binding.etPhone.removeError()
                }
            }
        }

        viewModel.emailState.observe(this) {
            when (it) {
                is EditTextFormState.FAILED -> {
                    binding.etEmail.setError(
                        getString(
                            it.idRawStringRes,
                            getString(R.string.email)
                        ), viewModel.processFormThroughButton
                    )
                }

                is EditTextFormState.EMPTY -> {
                    binding.etEmail.setError(
                        getString(
                            R.string.error_general_message_form_empty,
                            getString(R.string.email)
                        ), viewModel.processFormThroughButton
                    )
                }

                is EditTextFormState.SUCCESS -> {
                    binding.etEmail.removeError()
                }
            }
        }

        viewModel.initState.observe(this) {
            when (it) {
                is InitInputPhoneAndEmailState.SuccessLoadData -> {
                    binding.etPhone.text = it.phone ?: ""
                    binding.etEmail.text = it.email ?: ""
                }

                else -> {

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
    }

    private val otpLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {

            }
        }

    private fun goToOtp() {
        val intent = Intent(this, OtpVerificationActivity::class.java)
        intent.apply {
            putExtra(
                OtpVerificationActivity.PHONE_NUMBER_ARG,
                binding.etPhone.text.replace("\\D".toRegex(), "")
            )
        }
        otpLauncher.launch(intent)
    }
}