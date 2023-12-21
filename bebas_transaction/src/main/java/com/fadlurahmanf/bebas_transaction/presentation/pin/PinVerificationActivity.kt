package com.fadlurahmanf.bebas_transaction.presentation.pin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.databinding.ActivityPinVerificationBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_ui.pin.BebasPinKeyboard

class PinVerificationActivity :
    BaseTransactionActivity<ActivityPinVerificationBinding>(ActivityPinVerificationBinding::inflate),
    BebasPinKeyboard.Callback {
    override fun injectActivity() {
        component.inject(this)
    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        binding.ivPinKeyboard.setCallback(this)
    }

    private var pin: String = ""

    override fun onPinClicked(pin: String) {
        if (this.pin.length < 6) {
            this.pin += pin
            binding.bebasPinBox.pin = this.pin
        }
    }

    override fun onForgotPinClicked() {}

    override fun onDeletePinClicked() {
        if (pin.isNotEmpty()) {
            pin = pin.substring(0, pin.length - 1)
            binding.bebasPinBox.pin = pin
        }
    }

}