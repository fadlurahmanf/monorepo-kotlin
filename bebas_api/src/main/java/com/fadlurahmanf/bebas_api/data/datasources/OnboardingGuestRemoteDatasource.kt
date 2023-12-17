package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.OobApi
import com.fadlurahmanf.bebas_api.data.dto.ektp.EktpDataV2Request
import com.fadlurahmanf.bebas_api.data.dto.email.CheckEmailIsVerifyRequest
import com.fadlurahmanf.bebas_api.data.dto.email.RequestEmailVerificationRequest
import com.fadlurahmanf.bebas_api.data.dto.ektp.OcrRequest
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
        networkService().requestOtp(
            OtpVerificationRequest(
                phoneNumber, deviceId, false
            )
        )

    fun sendOtpVerification(phoneNumber: String, deviceId: String) =
        networkService().requestOtp(
            OtpVerificationRequest(
                phoneNumber, deviceId, true
            )
        )

    fun verifyOtp(request: VerifyOtpRequest) =
        networkService().verifyOtp(request)

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
        networkService().requestEmail(
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
        networkService().requestEmail(
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
        networkService().checkIsEmailVerify(request)

    fun getOcrV2(bodyRequest: OcrRequest) = networkService().getOCRv2(bodyRequest)
    fun saveEktpDataV2(ektpData: EktpDataV2Request) = networkService().saveEktpDataV2(ektpData)

    fun getProvinces() = networkService().getProvinces()

    fun getCities(provinceId: String) = networkService().getCities(provinceId)
    fun getSubDistricts(cityId: String) = networkService().getSubDistricts(cityId)
    fun getWards(subDistrictId: String) = networkService().getWards(subDistrictId)
}