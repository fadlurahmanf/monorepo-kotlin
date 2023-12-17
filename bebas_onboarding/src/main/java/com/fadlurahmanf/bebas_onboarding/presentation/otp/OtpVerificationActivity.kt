package com.fadlurahmanf.bebas_onboarding.presentation.otp

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.ForegroundColorSpan
import android.view.KeyEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.buildSpannedString
import com.fadlurahmanf.bebas_api.network_state.NetworkState
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityOtpVerificationBinding
import com.fadlurahmanf.bebas_onboarding.presentation.BaseOnboardingActivity
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.extension.maskPhoneNumber
import com.fadlurahmanf.bebas_ui.extension.dismissKeyboard
import com.fadlurahmanf.bebas_ui.font.BebasFontTypeSpan
import javax.inject.Inject

class OtpVerificationActivity :
    BaseOnboardingActivity<ActivityOtpVerificationBinding>(ActivityOtpVerificationBinding::inflate) {

    companion object {
        const val PHONE_NUMBER_ARG = "PHONE_NUMBER_ARG"
    }

    override fun injectActivity() {
        component.inject(this)
    }

    @Inject
    lateinit var viewModel: OtpVerificationViewModel

    private lateinit var phoneNumber: String


    override fun onBebasCreate(savedInstanceState: Bundle?) {
        binding.btnVerifyOtp.setOnClickListener {
            if (getOTPText().length >= 6) {
                viewModel.verifyOtp("111111", phoneNumber)
            }
        }

        binding.btnCounterOtpRetry.setOnClickListener {
            viewModel.requestOtp(phoneNumber)
        }

        val phoneNumberArg = intent.getStringExtra(PHONE_NUMBER_ARG)
        if (phoneNumberArg == null) {
            showFailedBottomsheet(BebasException.generalRC("PHONE_NUMBER_MISSING"))
            return
        }
        this.phoneNumber = phoneNumberArg

        initSubHeader()
        listenOtpField()
        initObserver()

        viewModel.requestOtp(phoneNumber)
    }

    private fun initObserver() {
        viewModel.otpTick.observe(this) {
            val minutes = (it / 1000) / 60
            val seconds = (it / 1000) % 60
            if (minutes > 0) {
                binding.tvCountdownOtpRetry.text =
                    getString(R.string.resend_otp_in_minutes_seconds, minutes, seconds)
            } else {
                binding.tvCountdownOtpRetry.text =
                    getString(R.string.resend_otp_in_seconds, seconds)
            }
        }

        viewModel.requestOtpState.observe(this) {
            when (it) {

                is NetworkState.SUCCESS -> {
                    binding.btnCounterOtpRetry.text =
                        getString(R.string.otp_counter_retry, it.data.totalRequestOtpAttempt, 3)

                    setTimerOtp(it.data.remainingOtpInSecond)
                }

                is NetworkState.FAILED -> {
                    showFailedBottomsheet(it.exception)
                }


                else -> {

                }
            }
        }

        viewModel.verifyOtpState.observe(this) {
            when (it) {
                is NetworkState.SUCCESS -> {
                    dismissLoadingDialog()
                    setResult(RESULT_OK, intent.apply {
                        putExtra("OTP_TOKEN", it.data)
                    })
                    finish()
                }

                is NetworkState.FAILED -> {
                    dismissLoadingDialog()
                    showFailedBottomsheet(it.exception)
                }

                is NetworkState.LOADING -> {
                    showLoadingDialog()
                }

                else -> {

                }
            }
        }
    }

    private fun initSubHeader() {
        val prefixOtpDescSpan = SpannableString("Masukkan OTP yang sudah kami kirimkan ke ")
        prefixOtpDescSpan.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0,
            prefixOtpDescSpan.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        val phoneSpan = SpannableString(phoneNumber.maskPhoneNumber())
        phoneSpan.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
            0,
            phoneSpan.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        phoneSpan.setSpan(
            BebasFontTypeSpan("", ResourcesCompat.getFont(this, R.font.lexend_deca_bold)!!),
            0,
            phoneSpan.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        val spannedBuilder = buildSpannedString {
            append(prefixOtpDescSpan)
            append(phoneSpan)
        }

        binding.tvSubtitleHeader.text = spannedBuilder
    }

    private var timer: CountDownTimer? = null

    private fun setTimerOtp(totalTimerInSeconds: Long) {
        timer = object : CountDownTimer(totalTimerInSeconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.btnCounterOtpRetry.visibility = View.GONE
                binding.tvCountdownOtpRetry.visibility = View.VISIBLE
                viewModel.setOtpTick(millisUntilFinished)
            }

            override fun onFinish() {
                binding.btnCounterOtpRetry.visibility = View.VISIBLE
                binding.tvCountdownOtpRetry.visibility = View.GONE
                viewModel.setOtpTick(0)
            }
        }
        timer?.start()
    }

    private fun listenOtpField() {
        binding.otp1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (before == 0 && count == 1) {
                    binding.otp2.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.otp2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (before == 0 && count == 1) {
                    binding.otp3.requestFocus()
                } else if (before == 1 && count == 0) {
                    binding.otp1.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.otp3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (before == 0 && count == 1) {
                    binding.otp4.requestFocus()
                } else if (before == 1 && count == 0) {
                    binding.otp2.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.otp4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (before == 0 && count == 1) {
                    binding.otp5.requestFocus()
                } else if (before == 1 && count == 0) {
                    binding.otp3.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.otp5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (before == 0 && count == 1) {
                    binding.otp6.requestFocus()
                } else if (before == 1 && count == 0) {
                    binding.otp4.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.otp6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (before == 0 && count == 1) {
                    binding.otp6.clearFocus()
                    binding.otp6.dismissKeyboard()
                } else if (before == 1 && count == 0) {
                    binding.otp5.requestFocus()
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.otp2.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_UP) {
                if (binding.otp2.text.isEmpty()) {
                    binding.otp1.text.clear()
                    binding.otp1.requestFocus()
                } else {
                    binding.otp2.text.clear()
                }
            }
            return@setOnKeyListener false
        }

        binding.otp3.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_UP) {
                if (binding.otp3.text.isEmpty()) {
                    binding.otp2.text.clear()
                    binding.otp2.requestFocus()
                } else {
                    binding.otp3.text.clear()
                }
            }
            return@setOnKeyListener false
        }

        binding.otp4.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_UP) {
                if (binding.otp4.text.isEmpty()) {
                    binding.otp3.text.clear()
                    binding.otp3.requestFocus()
                } else {
                    binding.otp4.text.clear()
                }
            }
            return@setOnKeyListener false
        }

        binding.otp5.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_UP) {
                if (binding.otp5.text.isEmpty()) {
                    binding.otp4.text.clear()
                    binding.otp4.requestFocus()
                } else {
                    binding.otp5.text.clear()
                }
            }
            return@setOnKeyListener false
        }

        binding.otp6.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_UP) {
                if (binding.otp6.text.isEmpty()) {
                    binding.otp5.text.clear()
                    binding.otp5.requestFocus()
                } else {
                    binding.otp6.text.clear()
                }
            }
            return@setOnKeyListener false
        }

        binding.otp1.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && getOTPText().isNotEmpty()) {
                binding.otp2.requestFocus()
            }
        }

        binding.otp2.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus && getOTPText().isEmpty()) {
                binding.otp1.requestFocus()
            } else if (hasFocus && getOTPText().length >= 2) {
                binding.otp3.requestFocus()
            }
        }

        binding.otp3.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && getOTPText().length < 2) {
                binding.otp2.requestFocus()
            } else if (hasFocus && getOTPText().length >= 3) {
                binding.otp4.requestFocus()
            }
        }

        binding.otp4.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && getOTPText().length < 3) {
                binding.otp3.requestFocus()
            } else if (hasFocus && getOTPText().length >= 4) {
                binding.otp5.requestFocus()
            }
        }

        binding.otp5.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && getOTPText().length < 4) {
                binding.otp4.requestFocus()
            } else if (hasFocus && getOTPText().length >= 5) {
                binding.otp6.requestFocus()
            }
        }

        binding.otp6.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && getOTPText().length < 5) {
                binding.otp5.requestFocus()
            }
        }
    }

    private fun getOTPText(): String {
        return "${binding.otp1.text}${binding.otp2.text}${binding.otp3.text}${binding.otp4.text}${binding.otp5.text}${binding.otp6.text}"
    }
}