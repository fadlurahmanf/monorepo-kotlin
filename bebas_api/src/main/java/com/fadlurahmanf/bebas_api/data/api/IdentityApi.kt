package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.pin.PinAttemptResponse
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface IdentityApi {
    @GET("user/attempt-failed")
    fun getTotalPinAttempt(): Observable<BaseResponse<PinAttemptResponse>>

    @POST("user/challenge")
    fun generateChallengeCode(
        @Body json: JsonObject
    ): Observable<BaseResponse<String>>
}