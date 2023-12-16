package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.home.TransactionMenuResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface CmsApi {
    @GET("homepage/menu")
    fun getTransactionMenu(): Observable<BaseResponse<List<TransactionMenuResponse>>>
}