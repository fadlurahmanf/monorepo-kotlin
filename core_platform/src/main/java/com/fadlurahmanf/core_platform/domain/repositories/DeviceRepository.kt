package com.fadlurahmanf.core_platform.domain.repositories

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.fadlurahmanf.core_platform.data.dto.model.BebasContactModel
import com.fadlurahmanf.core_platform.external.helper.CoreBiometric
import io.reactivex.rxjava3.core.Observable

interface DeviceRepository {
    fun randomUUID(): String
    fun deviceID(context: Context): String
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

    fun getContacts(
        context: Context
    ): Observable<List<BebasContactModel>>

    fun getContactsWithIndicator(
        context: Context
    ): Observable<List<BebasContactModel>>
}