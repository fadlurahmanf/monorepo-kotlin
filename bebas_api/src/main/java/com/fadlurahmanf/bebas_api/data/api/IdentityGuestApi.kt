package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.auth.LoginRequest
import com.fadlurahmanf.bebas_api.data.dto.auth.AuthResponse
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.auth.RefreshUserTokenRequest
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface IdentityGuestApi {
    @POST("auth/login")
    fun login(
        @Body body: LoginRequest
    ): Observable<BaseResponse<AuthResponse>>

    @POST("auth/refresh")
    fun refreshUserToken(
        @Body body: RefreshUserTokenRequest
    ): Call<BaseResponse<AuthResponse>>
}