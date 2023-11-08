package com.fadlurahmanf.mapp_example.presentation.otp

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.fadlurahmanf.mapp_example.databinding.ActivityOtpBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import com.fadlurahmanf.mapp_shared.extension.dismissKeyboard

class OtpActivity : BaseExampleActivity<ActivityOtpBinding>(ActivityOtpBinding::inflate) {
    override fun injectActivity() {

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
                    binding.otp4.clearFocus()
                    binding.otp4.dismissKeyboard()
                } else if (before == 1 && count == 0) {
                    binding.otp3.requestFocus()
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

        binding.otp1.setOnFocusChangeListener { v, hasFocus ->
            Log.d("MappOtpLogger", "OTP1, HAS_FOCUS: $hasFocus")
            if (hasFocus && getOTPText().isNotEmpty()) {
                binding.otp2.requestFocus()
            }
        }

        binding.otp2.setOnFocusChangeListener { v, hasFocus ->
            Log.d("MappOtpLogger", "OTP2, HAS_FOCUS: $hasFocus")
            if (hasFocus && getOTPText().isEmpty()) {
                binding.otp1.requestFocus()
            } else if (hasFocus && getOTPText().length >= 2) {
                binding.otp3.requestFocus()
            }
        }

        binding.otp3.setOnFocusChangeListener { v, hasFocus ->
            Log.d("MappOtpLogger", "OTP3, HAS_FOCUS: $hasFocus")
            if (hasFocus && getOTPText().length < 2) {
                binding.otp2.requestFocus()
            } else if (hasFocus && getOTPText().length >= 3) {
                binding.otp4.requestFocus()
            }
        }

        binding.otp4.setOnFocusChangeListener { v, hasFocus ->
            Log.d("MappOtpLogger", "OTP4, HAS_FOCUS: $hasFocus")
            if (hasFocus && getOTPText().length < 3) {
                binding.otp3.requestFocus()
            }
        }

    }

    private fun isKeyCodeNumber(keyCode: Int): Boolean {
        return keyCode == KeyEvent.KEYCODE_0 || keyCode == KeyEvent.KEYCODE_1 || keyCode == KeyEvent.KEYCODE_2 || keyCode == KeyEvent.KEYCODE_3 || keyCode == KeyEvent.KEYCODE_4 || keyCode == KeyEvent.KEYCODE_5 || keyCode == KeyEvent.KEYCODE_6 || keyCode == KeyEvent.KEYCODE_7 || keyCode == KeyEvent.KEYCODE_8 || keyCode == KeyEvent.KEYCODE_9
    }

    private fun getOTPText(): String {
        return "${binding.otp1.text}${binding.otp2.text}${binding.otp3.text}${binding.otp4.text}"
    }

}