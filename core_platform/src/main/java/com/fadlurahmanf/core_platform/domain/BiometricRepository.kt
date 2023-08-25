package com.fadlurahmanf.core_platform.domain

import android.content.Context
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal

interface BiometricRepository {
    fun isSupportedBiometric(context: Context):Boolean
}