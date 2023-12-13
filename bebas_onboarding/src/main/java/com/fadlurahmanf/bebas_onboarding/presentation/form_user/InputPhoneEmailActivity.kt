package com.fadlurahmanf.bebas_onboarding.presentation.form_user

import android.app.Activity
import android.content.Intent
import android.text.Editable
import androidx.activity.result.contract.ActivityResultContracts
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.data.state.InitInputPhoneAndEmailState
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityInputPhoneEmailBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_onboarding.presentation.email.EmailVerificationActivity
import com.fadlurahmanf.bebas_onboarding.presentation.otp.OtpVerificationActivity
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.data.flow.OnboardingFlow
import com.fadlurahmanf.bebas_ui.edittext.BebasMaskingEdittext
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
                                                   BebasMaskingEdittext.BebasMaskingEdittextTextWatcher {
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

        // fake
        binding.etPhone.text = "081555555555"
        binding.etEmail.text = "bmastest@mailnesia.com"

        viewModel.initLastStorage()
    }

    private fun initObserver() {
        viewModel.phoneState.observe(this) {
            when (it) {
                is EditTextFormState.FAILED -> {
                    binding.etPhone.setError(
                        getString(
                            it.idRawStringRes,
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
                            it.idRawStringRes
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
                is InitInputPhoneAndEmailState.SuccessToOtp -> {
                    binding.etPhone.text = it.phone
                    binding.etEmail.text = it.email

                    val intent = Intent(this, EmailVerificationActivity::class.java)
                    intent.putExtra(EmailVerificationActivity.EMAIL_ARG, it.email)
                    emailLauncher.launch(intent)
                }

                is InitInputPhoneAndEmailState.FAILED -> {

                }

                is InitInputPhoneAndEmailState.SuccessFlowOnboarding -> {
                    binding.etPhone.text = it.phone
                    binding.etEmail.text = it.email

                    val intent = Intent(this, PrepareOnboardingActivity::class.java)
                    startActivity(intent)
                }

                is InitInputPhoneAndEmailState.SuccessFlowSelfActivation -> {
                    binding.etPhone.text = it.phone
                    binding.etEmail.text = it.email

                    val intent = Intent(this, InputNikAndAccountNumberActivity::class.java)
                    startActivity(intent)
                }

                is InitInputPhoneAndEmailState.SuccessToEmail -> {
                    binding.etPhone.text = it.phone
                    binding.etEmail.text = it.email

                    val intent = Intent(this, EmailVerificationActivity::class.java)
                    intent.putExtra(EmailVerificationActivity.EMAIL_ARG, it.email)
                    emailLauncher.launch(intent)
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
                val otpToken = it.data?.getStringExtra("OTP_TOKEN")
                if (otpToken != null) {
                    viewModel.updateIsFinishedOtpVerification(true)
                    goToEmail()
                }
            }
        }

    private val emailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val emailToken = it.data?.getStringExtra("EMAIL_TOKEN")
                if (emailToken != null) {
                    viewModel.updateIsFinishedEmailVerification(true)
                    navigateAfterEmailVerification(emailToken)
                }
            }
        }

    private fun navigateAfterEmailVerification(emailToken: String) {
        when (viewModel.onboardingFlow) {
            OnboardingFlow.ONBOARDING -> {
                val intent = Intent(this, PrepareOnboardingActivity::class.java)
                startActivity(intent)
            }

            OnboardingFlow.SELF_ACTIVATION -> {
                val intent = Intent(this, InputNikAndAccountNumberActivity::class.java)
                startActivity(intent)
            }

            null -> {
                showFailedBottomsheet(BebasException.generalRC("ONBOARDING_FLOW_MISSING"))
            }
        }
    }

    private fun goToOtp() {
        viewModel.updateIsFinishedOtpVerification(false)
        val intent = Intent(this, OtpVerificationActivity::class.java)
        intent.apply {
            putExtra(
                OtpVerificationActivity.PHONE_NUMBER_ARG,
                binding.etPhone.text.replace("\\D".toRegex(), "")
            )
        }
        otpLauncher.launch(intent)
    }

    private fun goToEmail() {
        viewModel.updateIsFinishedEmailVerification(false)
        val intent = Intent(this, EmailVerificationActivity::class.java)
        intent.apply {
            putExtra(
                EmailVerificationActivity.EMAIL_ARG,
                binding.etEmail.text
            )
        }
        emailLauncher.launch(intent)
    }

    override fun onDestroy() {
        viewModel.deleteDataInputPhoneAndEmail()
        super.onDestroy()
    }

}