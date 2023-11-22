package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.email.CheckEmailIsVerifyRequest
import com.fadlurahmanf.bebas_api.data.dto.email.CheckEmailIsVerifyResponse
import com.fadlurahmanf.bebas_api.data.dto.email.RequestEmailVerificationReponse
import com.fadlurahmanf.bebas_api.data.dto.email.RequestEmailVerificationRequest
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.ocr.OcrRequest
import com.fadlurahmanf.bebas_api.data.dto.ocr.OcrResponse
import com.fadlurahmanf.bebas_api.data.dto.otp.OtpVerificationRequest
import com.fadlurahmanf.bebas_api.data.dto.otp.OtpResponse
import com.fadlurahmanf.bebas_api.data.dto.otp.VerifyOtpRequest
import com.fadlurahmanf.bebas_api.data.dto.otp.VerifyOtpResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface OobApi {
    @POST("verification/request-otp")
    fun requestOtp(
        @Body body: OtpVerificationRequest
    ): Observable<BaseResponse<OtpResponse>>

    @POST("verification/verify-otp")
    fun verifyOtp(
        @Body body: VerifyOtpRequest
    ): Observable<BaseResponse<VerifyOtpResponse>>

    @POST("verification/request-email")
    fun requestEmail(
        @Body body: RequestEmailVerificationRequest
    ): Observable<BaseResponse<RequestEmailVerificationReponse>>

    @POST("verification/check-email")
    fun checkIsEmailVerify(
        @Body body: CheckEmailIsVerifyRequest
    ): Observable<BaseResponse<CheckEmailIsVerifyResponse>>

    @POST("identity-card/ocr")
    fun getOCRv2(
        @Body body: OcrRequest
    ): Observable<BaseResponse<OcrResponse>>
}