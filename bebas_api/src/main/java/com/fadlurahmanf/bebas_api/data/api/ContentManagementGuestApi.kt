package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.banner.WelcomeBannerResponse
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.tnc.TncResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Header

interface ContentManagementGuestApi {
    @GET("welcomepage/product")
    fun getWelcomeBanner(
        @Header("Accept-Language") lang: String
    ): Observable<BaseResponse<List<WelcomeBannerResponse>>>

    @GET("welcomepage/term-and-condition")
    fun getTNC(
        @Header("Accept-Language") lang: String
    ): Observable<BaseResponse<TncResponse>>
}