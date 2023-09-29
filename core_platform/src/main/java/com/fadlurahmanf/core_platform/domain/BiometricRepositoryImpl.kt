package com.fadlurahmanf.core_platform.domain

import android.content.Context
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricManager.BIOMETRIC_SUCCESS
import android.hardware.biometrics.BiometricPrompt
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.fragment.app.Fragment
import androidx.biometric.BiometricPrompt as XBiometricPrompt
import com.fadlurahmanf.core_platform.external.helper.CoreBiometric

class BiometricRepositoryImpl : BiometricRepository {

    override fun isSupportedBiometric(
        context: Context
    ): Boolean {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            val biometricManager =
                context.getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager
            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BIOMETRIC_SUCCESS
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            val biometricManager =
                context.getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager
            biometricManager.canAuthenticate() == BIOMETRIC_SUCCESS
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            val manager =
                context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            manager.hasEnrolledFingerprints()
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            val manager = FingerprintManagerCompat.from(context)
            manager.hasEnrolledFingerprints()
        } else {
            TODO("NOT IMPLEMENTED")
        }
    }

    override fun authenticateX(
        fragment: Fragment,
        titleText: String,
        descriptionText: String,
        negativeText: String,
        callback: CoreBiometric.AuthenticateXCallback,
    ) {
        val prompt = XBiometricPrompt(fragment, object :
            XBiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: XBiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                callback.onAuthenticationSuccess(result)
            }
        })
        val promptInfo = XBiometricPrompt.PromptInfo.Builder().setTitle(titleText)
            .setDescription(descriptionText)
            .setNegativeButtonText(negativeText)
            .build()
        return prompt.authenticate(promptInfo)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun authenticateP(
        context: Context,
        titleText: String,
        descriptionText: String,
        negativeText: String,
        callback: CoreBiometric.AuthenticatePCallback
    ) {
        val executor = ContextCompat.getMainExecutor(context)
        val cancellationSignal = CancellationSignal()
        cancellationSignal.setOnCancelListener {
            callback.onCancelClicked()
        }
        val prompt = BiometricPrompt.Builder(context).setTitle(titleText)
            .setDescription(descriptionText)
            .setNegativeButton(
                negativeText, executor
            ) { _, _ -> callback.onCancelClicked() }
            .build()
        return prompt.authenticate(
            cancellationSignal, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    callback.onAuthenticationSuccess(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    callback.onAuthenticationFailed()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    callback.onAuthenticationError(errorCode, errString)
                }
            }
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun authenticateM2(
        context: Context,
        callback: CoreBiometric.AuthenticateM2Callback
    ) {
        val manager =
            context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
        val cancellationSignal = CancellationSignal()
        cancellationSignal.setOnCancelListener {
            callback.onCancelClicked()
        }
        return manager.authenticate(null, cancellationSignal, 0, object :
            FingerprintManager.AuthenticationCallback() {
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                callback.onAuthenticationFailed()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                super.onAuthenticationError(errorCode, errString)
                callback.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationSucceeded(result: FingerprintManager.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                callback.onAuthenticationSuccess(result)
            }
        }, null)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun authenticateM1(
        context: Context,
        callback: CoreBiometric.AuthenticateM1Callback
    ) {
        val manager = FingerprintManagerCompat.from(context)
        val cancellationSignal = androidx.core.os.CancellationSignal()
        cancellationSignal.setOnCancelListener {
            callback.onCancelClicked()
        }
        return manager.authenticate(null, 0, cancellationSignal, object :
            FingerprintManagerCompat.AuthenticationCallback() {
            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                callback.onAuthenticationFailed()
            }

            override fun onAuthenticationError(errMsgId: Int, errString: CharSequence?) {
                super.onAuthenticationError(errMsgId, errString)
                callback.onAuthenticationError(errMsgId, errString)
            }

            override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult?) {
                super.onAuthenticationSucceeded(result)
                callback.onAuthenticationSuccess(result)
            }
        }, null)
    }
}