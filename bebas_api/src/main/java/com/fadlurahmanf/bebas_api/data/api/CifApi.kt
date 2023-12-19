package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoritePLNPrePaidResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoriteTransferResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.LatestTransactionResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.PinFavoriteRequest
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface CifApi {
    @GET("favorite")
    fun getFavoriteTransfer(): Observable<BaseResponse<List<FavoriteTransferResponse>>>

    @GET("favorite-prepaid/get-favorite")
    fun getFavoritePLNPrePaid(): Observable<BaseResponse<List<FavoritePLNPrePaidResponse>>>

    @PUT("favorite/pin")
    fun pinFavorite(
        @Body request: PinFavoriteRequest
    ): Observable<BaseResponse<Nothing>>

    @GET("transaction-history/last-three-transactions")
    fun getLatestTransactionTransfer(): Observable<BaseResponse<List<LatestTransactionResponse>>>

    @GET("transaction-history/prepaid/last-three-transactions")
    fun getLatestTransactionPLNPrePaid(
        @Query("type") type: String = "Listrik"
    ): Observable<BaseResponse<List<LatestTransactionResponse>>>
}