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
        val versionCode = intent.extras?.getString("VERSION_CODE")
        val versionName = intent.extras?.getString("VERSION_NAME")
        val packageId = intent.extras?.getString("PACKAGE_ID")

        BebasShared.appVersionName = versionName ?: "-"
        BebasShared.appVersionCode = versionCode ?: "-"
        BebasShared.packageId = packageId ?: "-"


        when (flavor) {
            "retail" -> {
                BebasShared.setBebasUrl("https://api.bankmas.my.id/")
                BebasShared.setOpenviduBaseUrl("https://vc.bankmas.my.id/", "vc.bankmas.my.id")
            }

            "dev" -> {
                BebasShared.setBebasUrl("https://api.bankmas.my.id/")
                BebasShared.setOpenviduBaseUrl("https://vc.bankmas.my.id/", "vc.bankmas.my.id")
                BebasShared.encodedPrivateKeyTransaction = "j8U51ILRqW/L+hHF4WBjaHA6lKwAbT5crNZTmBb/vqh7DQaLNIgc9ovWRBmsBNVZ8c0X5Y4imBudLzrO5NZIMQ=="
                BebasShared.encodedPublicKeyTransaction = "ew0GizSIHPaL1kQZrATVWfHNF+WOIpgbnS86zuTWSDE="
                BebasShared.saltPassword = "ew0GizSIHPaL1kQZrATVWfHNF+WOIpgb"
                BebasShared.saltPin = "48666158643620530283315810"
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