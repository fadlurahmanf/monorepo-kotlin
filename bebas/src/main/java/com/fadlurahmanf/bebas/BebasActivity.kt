package com.fadlurahmanf.bebas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

class BebasActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bebas)

        val intent = Intent(
            this,
            Class.forName("com.fadlurahmanf.bebas_config.presentation.BebasConfigActivity")
        )
        intent.apply {
            putExtra("FLAVOR", BuildConfig.FLAVOR)
            putExtra("VERSION_CODE", BuildConfig.VERSION_CODE)
            putExtra("VERSION_NAME", BuildConfig.VERSION_NAME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}