package com.fadlurahmanf.mapp_api.data.api

import com.fadlurahmanf.mapp_api.data.dto.general.BaseResponse
import com.fadlurahmanf.mapp_api.data.dto.identity.CreateGuestTokenResponse
import com.fadlurahmanf.starterappmvvm.core.network.data.dto.request.CreateGuestTokenRequest
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface MasIdentityApi {
    @POST("guest/session/create")
    fun createGuestToken(
        @Body body: CreateGuestTokenRequest
    ): Observable<BaseResponse<CreateGuestTokenResponse>>
}