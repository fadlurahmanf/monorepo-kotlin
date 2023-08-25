package com.fadlurahmanf.core_platform.domain

import android.content.Context
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricManager.BIOMETRIC_SUCCESS
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import javax.inject.Inject

class BiometricRepositoryImpl: BiometricRepository {
    override fun isSupportedBiometric(
        context: Context
    ): Boolean {
        return if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
            val biometricManager = context.getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager
            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) == BIOMETRIC_SUCCESS
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            val biometricManager =
                context.getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager
            biometricManager.canAuthenticate() == BIOMETRIC_SUCCESS
        } else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M){
            val manager = context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager
            manager.hasEnrolledFingerprints()
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            val manager = FingerprintManagerCompat.from(context)
            manager.hasEnrolledFingerprints()
        } else {
            TODO("NOT IMPLEMENTED")
        }
    }
}