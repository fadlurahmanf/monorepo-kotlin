package com.fadlurahmanf.core_platform.external.helper

import android.hardware.biometrics.BiometricPrompt
import android.hardware.fingerprint.FingerprintManager
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.biometric.BiometricPrompt as XBiometricPrompt

class CoreBiometric {
    interface AuthenticateCallback {
        fun onCancelClicked() {}
        fun onAuthenticationFailed()
        fun onAuthenticationError(errorCode: Int, errString: CharSequence?)
    }

    interface AuthenticateGeneralCallback : AuthenticateCallback {
        fun onAuthenticationSuccess(result: XBiometricPrompt.AuthenticationResult)
    }

    interface AuthenticatePCallback : AuthenticateCallback {
        fun onNegativeClick() {}
        fun onAuthenticationSuccess(result: BiometricPrompt.AuthenticationResult?)
    }

    interface AuthenticateM2Callback : AuthenticateCallback {
        fun onAuthenticationSuccess(result: FingerprintManager.AuthenticationResult?)
    }

    interface AuthenticateM1Callback : AuthenticateCallback {
        fun onAuthenticationSuccess(result: FingerprintManagerCompat.AuthenticationResult?)
    }
}