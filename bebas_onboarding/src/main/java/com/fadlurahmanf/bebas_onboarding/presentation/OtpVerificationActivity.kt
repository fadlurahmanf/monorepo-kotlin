package com.fadlurahmanf.bebas_onboarding.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import com.fadlurahmanf.bebas_onboarding.R
import com.fadlurahmanf.bebas_onboarding.databinding.ActivityOtpVerificationBinding
import com.fadlurahmanf.bebas_ui.extension.dismissKeyboard

class OtpVerificationActivity :
    BaseOnboardingActivity<ActivityOtpVerificationBinding>(ActivityOtpVerificationBinding::inflate) {
    override fun injectActivity() {
        component.inject(this)
    }

    override fun setup() {
        listenOtpField()
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

        binding.otp1.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_UP) {
                if (binding.otp2.text.isEmpty()) {
                    binding.otp1.text.clear()
                    binding.otp1.requestFocus()
                } else {
                    binding.otp2.text.clear()
                }
            }
            return@setOnKeyListener true
        }

        binding.otp2.setOnKeyListener { v, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_UP) {
                if (binding.otp2.text.isEmpty()) {
                    binding.otp1.text.clear()
                    binding.otp1.requestFocus()
                } else {
                    binding.otp2.text.clear()
                }
            }
            return@setOnKeyListener true
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
            return@setOnKeyListener true
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
            return@setOnKeyListener true
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
            return@setOnKeyListener true
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
            return@setOnKeyListener true
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