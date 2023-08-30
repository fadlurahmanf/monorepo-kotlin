package com.fadlurahmanf.mapp_example.presentation.biometric

import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CancellationSignal
import com.fadlurahmanf.core_platform.domain.BiometricRepository
import com.fadlurahmanf.mapp_example.R
import com.fadlurahmanf.mapp_example.databinding.ActivityBiometricBinding
import com.fadlurahmanf.mapp_example.presentation.BaseExampleActivity
import javax.inject.Inject

class BiometricActivity : BaseExampleActivity<ActivityBiometricBinding>(
    ActivityBiometricBinding::inflate
) {

    override fun injectActivity() {
        component.inject(this)
    }

    private lateinit var cancellationSignal: CancellationSignal

    override fun setup() {
        cancellationSignal = CancellationSignal()
        cancellationSignal.setOnCancelListener {
            println("MASUK CANCEL")
        }
        binding.tvIsSupportedBiometric.text = "IS SUPPORTED BIOMETRIC: ${biometricRepository.isSupportedBiometric(this)}"

        binding.btnAuthenticateBiometric.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//                biometricRepository.authenticate(this, cancellationSignal, object :
//                    BiometricPrompt.AuthenticationCallback() {
//                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
//                        super.onAuthenticationSucceeded(result)
//                        println("MASUK ON SUCCESS")
//                    }
//
//                    override fun onAuthenticationFailed() {
//                        super.onAuthenticationFailed()
//                        println("MASUK ON AUTH FAILED")
//                    }
//                })
            }
        }
    }

    @Inject
    lateinit var biometricRepository: BiometricRepository
}