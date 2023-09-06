package com.fadlurahmanf.mapp_config.presentation.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fadlurahmanf.mapp_config.R
import com.fadlurahmanf.mapp_shared.MappShared

class MappSplActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapp_spl)

        val flavor = intent.extras?.getString("FLAVOR")

        MappShared.flavor = flavor ?: "dev"
        when (flavor) {
            "dev" -> {
                MappShared.masGuestBaseUrl = "https://guest.bankmas.my.id/"
            }
        }

        val intent = Intent(this, Class.forName("com.fadlurahmanf.mapp_example.presentation.example.ExampleActivity"))
        startActivity(intent)
    }
}