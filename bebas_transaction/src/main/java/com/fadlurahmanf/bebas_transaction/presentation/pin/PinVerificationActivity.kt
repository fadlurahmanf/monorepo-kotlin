package com.fadlurahmanf.bebas_transaction.presentation.pin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fadlurahmanf.bebas_transaction.R
import com.fadlurahmanf.bebas_transaction.databinding.ActivityPinVerificationBinding
import com.fadlurahmanf.bebas_transaction.presentation.BaseTransactionActivity
import com.fadlurahmanf.bebas_ui.pin.BebasPinKeyboard

class PinVerificationActivity : BaseTransactionActivity<ActivityPinVerificationBinding>(ActivityPinVerificationBinding::inflate),
    BebasPinKeyboard.Callback {
    override fun injectActivity() {

    }

    override fun onBebasCreate(savedInstanceState: Bundle?) {
        binding.ivPinKeyboard.setCallback(this)
    }

    override fun onPinClicked(pin: String) {
        Log.d("BebasLogger", "ON PIN CLICKED: $pin")
    }

    override fun onForgotPinClicked() {
        Log.d("BebasLogger", "ON FORGOT PIN CLICKED")
    }

}