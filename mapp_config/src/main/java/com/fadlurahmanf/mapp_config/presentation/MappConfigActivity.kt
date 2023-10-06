package com.fadlurahmanf.mapp_config.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.mapp_config.R
import com.fadlurahmanf.mapp_shared.MappShared

class MappConfigActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapp_config)

        val flavor = intent.extras?.getString("FLAVOR")

        MappShared.flavor = flavor ?: "dev"
        when (flavor) {
            "dev" -> {
                MappShared.merchantGuestBaseUrl = "https://merchant.bankmas.my.id/"
                MappShared.retailGuestBaseUrl = "https://api.bankmas.my.id/"
            }
        }

        val intent = Intent(
                this,
        Class.forName("com.fadlurahmanf.mapp_splash.presentation.SplashActivity")
        )
        startActivity(intent)
    }
}