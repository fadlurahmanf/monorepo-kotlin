package com.fadlurahmanf.bebas_config.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.bebas_config.R

class BebasConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bebas_config)

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