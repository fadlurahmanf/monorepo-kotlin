package com.fadlurahmanf.core_platform.domain

import android.content.Context
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.fadlurahmanf.core_platform.external.helper.CoreBiometric

interface BiometricRepository {
    fun isSupportedBiometric(context: Context): Boolean

    fun authenticateX(
        fragment: Fragment,
        titleText: String,
        descriptionText: String,
        negativeText: String,
        callback: CoreBiometric.AuthenticateXCallback,
    )

    @RequiresApi(Build.VERSION_CODES.P)
    fun authenticateP(
        context: Context,
        titleText: String,
        descriptionText: String,
        negativeText: String,
        callback: CoreBiometric.AuthenticatePCallback
    )

    @RequiresApi(Build.VERSION_CODES.M)
    fun authenticateM2(
        context: Context,
        callback: CoreBiometric.AuthenticateM2Callback
    )

    @RequiresApi(Build.VERSION_CODES.M)
    fun authenticateM1(
        context: Context,
        callback: CoreBiometric.AuthenticateM1Callback
    )
}