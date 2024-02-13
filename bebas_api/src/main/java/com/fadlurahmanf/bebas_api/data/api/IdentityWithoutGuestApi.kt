package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.auth.CreateGuestTokenResponse
import com.fadlurahmanf.bebas_api.data.dto.auth.GenerateGuestTokenRequest
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface IdentityWithoutGuestApi {
    @POST("guest/session/create")
    fun createGuestToken(
        @Body body: GenerateGuestTokenRequest
    ): Observable<BaseResponse<CreateGuestTokenResponse>>
}