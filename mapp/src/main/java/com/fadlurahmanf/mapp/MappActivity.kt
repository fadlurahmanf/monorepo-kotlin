package com.fadlurahmanf.mapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fadlurahmanf.mapp_example.presentation.example.ExampleActivity

class MappActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapp)
        val flavor = BuildConfig.FLAVOR
        val intent = Intent("com.fadlurahmanf.mapp_config.MappSplActivity")
        intent.apply {
            putExtra("FLAVOR", flavor)
        }
        startActivity(intent)
    }
}