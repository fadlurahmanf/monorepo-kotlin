package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoriteTransferResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.PinFavoriteRequest
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.home.TransactionMenuResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface CifApi {
    @GET("favorite")
    fun getFavoriteTransfer(): Observable<BaseResponse<List<FavoriteTransferResponse>>>

    @PUT("favorite/pin")
    fun pinFavorite(
        @Body request: PinFavoriteRequest
    ): Observable<BaseResponse<Nothing>>
}