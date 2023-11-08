package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.identity.CreateGuestTokenResponse
import com.fadlurahmanf.bebas_api.data.dto.identity.GenerateGuestTokenRequest
import com.fadlurahmanf.bebas_api.data.dto.otp.OtpRequest
import com.fadlurahmanf.bebas_api.data.dto.otp.OtpResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface OobApi {
    @POST("verification/request-otp")
    fun requestOtp(
        @Body body: OtpRequest
    ): Observable<BaseResponse<OtpResponse>>
}