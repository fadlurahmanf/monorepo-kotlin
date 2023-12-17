package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoriteTransferResponse
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.home.TransactionMenuResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET

interface CifApi {
    @GET("favorite")
    fun getFavoriteTransfer(): Observable<BaseResponse<List<FavoriteTransferResponse>>>
}