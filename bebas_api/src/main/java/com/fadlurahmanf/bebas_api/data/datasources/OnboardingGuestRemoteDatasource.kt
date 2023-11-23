package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.OobApi
import com.fadlurahmanf.bebas_api.data.dto.email.CheckEmailIsVerifyRequest
import com.fadlurahmanf.bebas_api.data.dto.email.RequestEmailVerificationRequest
import com.fadlurahmanf.bebas_api.data.dto.ocr.OcrRequest
import com.fadlurahmanf.bebas_api.data.dto.otp.OtpVerificationRequest
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
            OtpVerificationRequest(
                phoneNumber, deviceId, false
            )
        )

    fun sendOtpVerification(phoneNumber: String, deviceId: String) =
        networkService(30).requestOtp(
            OtpVerificationRequest(
                phoneNumber, deviceId, true
            )
        )

    fun verifyOtp(request: VerifyOtpRequest) =
        networkService(30).verifyOtp(request)

    fun requestEmailAvailability(
        email: String,
        phoneNumber: String,
        otpToken: String,
        deviceId: String,
        /**
         * POSSIBLE VALUE: onboarding, selfActivation
         * */
        flowType: String
    ) =
        networkService(30).requestEmail(
            RequestEmailVerificationRequest(
                email = email,
                phoneNumber = phoneNumber,
                otpToken = otpToken,
                deviceId = deviceId,
                flowType = flowType,
                shouldSendEmail = false
            )
        )

    fun sendEmailVerification(
        email: String,
        phoneNumber: String,
        otpToken: String,
        deviceId: String,
        /**
         * POSSIBLE VALUE: onboarding, selfActivation
         * */
        flowType: String
    ) =
        networkService(30).requestEmail(
            RequestEmailVerificationRequest(
                email = email,
                phoneNumber = phoneNumber,
                otpToken = otpToken,
                deviceId = deviceId,
                flowType = flowType,
                shouldSendEmail = true
            )
        )

    fun checkEmailIsVerify(request: CheckEmailIsVerifyRequest) =
        networkService(30).checkIsEmailVerify(request)

    fun getOcrV2(bodyRequest: OcrRequest) = networkService(30).getOCRv2(bodyRequest)

    fun getProvinces() = networkService(30).getProvinces()
}