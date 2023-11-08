package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.IdentityGuestTokenApi
import com.fadlurahmanf.bebas_api.data.api.OobApi
import com.fadlurahmanf.bebas_api.data.dto.identity.LoginRequest
import com.fadlurahmanf.bebas_api.data.dto.identity.RefreshUserTokenRequest
import com.fadlurahmanf.bebas_api.data.dto.otp.OtpRequest
import com.fadlurahmanf.bebas_api.domain.network.IdentityGuestNetwork
import javax.inject.Inject

class OnboardingGuestRemoteDatasource @Inject constructor(
    context: Context
) :
    IdentityGuestNetwork<OobApi>(context) {
    override fun getApi(): Class<OobApi> = OobApi::class.java

    fun requestOtpAvailability(phoneNumber: String, deviceId: String) =
        networkService(30).requestOtp(
            OtpRequest(
                phoneNumber, deviceId, false
            )
        )

    fun sendOtp(phoneNumber: String, deviceId: String) =
        networkService(30).requestOtp(
            OtpRequest(
                phoneNumber, deviceId, true
            )
        )
}