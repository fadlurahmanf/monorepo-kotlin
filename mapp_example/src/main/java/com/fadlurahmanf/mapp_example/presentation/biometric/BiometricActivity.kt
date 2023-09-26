package com.fadlurahmanf.mapp_example.presentation.biometric

import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import com.fadlurahmanf.core_platform.domain.BiometricRepository
import com.fadlurahmanf.core_platform.external.helper.CoreBiometric
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
        binding.tvIsSupportedBiometric.text =
            "IS SUPPORTED BIOMETRIC: ${biometricRepository.isSupportedBiometric(this)}"

        binding.btnAuthenticateBiometric.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                biometricRepository.authenticateP(
                    this,
                    "TITLE BIOMETRIC",
                    "DESC BIOMETRIC",
                    "NEGATIVE",
                    object : CoreBiometric.AuthenticatePCallback {
                        override fun onAuthenticationSuccess(result: BiometricPrompt.AuthenticationResult?) {
                            showInfoBottomsheet(
                                title = "Biometric",
                                desc = "Biometric Success",
                                buttonText = "Okey"
                            )
                        }

                        override fun onAuthenticationFailed() {

                        }

                        override fun onAuthenticationError(
                            errorCode: Int,
                            errString: CharSequence?
                        ) {

                        }
                    })
            }
        }
    }

    @Inject
    lateinit var biometricRepository: BiometricRepository
}