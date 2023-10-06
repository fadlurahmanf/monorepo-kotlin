package com.fadlurahmanf.mapp_api.data.api

import com.fadlurahmanf.mapp_api.data.dto.general.BaseResponse
import com.fadlurahmanf.mapp_api.data.dto.identity.LoginRequest
import com.fadlurahmanf.mapp_api.data.dto.identity.LoginResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface MasIdentityGuestTokenApi {
    @POST("sales/auth/login")
    fun login(
        @Body body: LoginRequest
    ): Observable<BaseResponse<LoginResponse>>
}