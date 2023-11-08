package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.OobApi
import com.fadlurahmanf.bebas_api.data.dto.otp.OtpRequest
import com.fadlurahmanf.bebas_api.data.dto.otp.VerifyOtpRequest
import com.fadlurahmanf.bebas_api.domain.network.OobGuestNetwork
import javax.inject.Inject

class OnboardingGuestRemoteDatasource @Inject constructor(
    context: Context
) :
    OobGuestNetwork<OobApi>(context) {
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

    fun verifyOtp(request: VerifyOtpRequest) =
        networkService(30).verifyOtp(request)
}