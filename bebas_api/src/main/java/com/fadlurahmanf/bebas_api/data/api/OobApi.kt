package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.demography.DemographyItemResponse
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
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @GET("region/provinces")
    fun getProvinces(): Observable<BaseResponse<List<DemographyItemResponse>>>

    @GET("region/city/{provinceId}")
    fun getCities(
        @Path("provinceId") provinceId: String
    ): Observable<BaseResponse<List<DemographyItemResponse>>>
    @GET("region/sub-district/{cityId}")
    fun getSubDistricts(
        @Path("cityId") cityId: String
    ): Observable<BaseResponse<List<DemographyItemResponse>>>

    @GET("region/village/{subDistrictId}")
    fun getWards(
        @Path("subDistrictId") subDistrictId: String
    ): Observable<BaseResponse<List<DemographyItemResponse>>>
}