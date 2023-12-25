package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.pin.PinAttemptResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface IdentityApi {
    @GET("user/attempt-failed")
    fun getTotalPinAttempt(): Observable<BaseResponse<PinAttemptResponse>>
}