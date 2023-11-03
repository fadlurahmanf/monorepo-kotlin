package com.fadlurahmanf.bebas_config.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.fadlurahmanf.bebas_config.R
import com.fadlurahmanf.bebas_shared.BebasShared

class BebasConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bebas_config)
        val flavor = intent.extras?.getString("FLAVOR")

        when (flavor) {
            "dev" -> {
                BebasShared.setBebasUrl("https://api.bankmas.my.id/")
            }

            "staging" -> {
                BebasShared.setBebasUrl("https://api.bankmas.link/")
            }

            "prod" -> {
                BebasShared.setBebasUrl("https://api.bankmas.net/")
            }
        }

        val intent = Intent(
            this,
            Class.forName("com.fadlurahmanf.bebas_onboarding.presentation.splash.BebasSplashActivity")
        )
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}