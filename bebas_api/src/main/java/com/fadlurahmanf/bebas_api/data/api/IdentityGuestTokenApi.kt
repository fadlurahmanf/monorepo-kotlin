package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.identity.LoginRequest
import com.fadlurahmanf.bebas_api.data.dto.identity.AuthResponse
import com.fadlurahmanf.bebas_api.data.dto.identity.RefreshUserTokenRequest
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface IdentityGuestTokenApi {
    @POST("auth/login")
    fun login(
        @Body body: LoginRequest
    ): Observable<BaseResponse<AuthResponse>>

    @POST("auth/refresh")
    fun refreshUserToken(
        @Body body: RefreshUserTokenRequest
    ): Observable<BaseResponse<AuthResponse>>
}