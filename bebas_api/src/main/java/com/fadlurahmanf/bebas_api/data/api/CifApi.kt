package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoritePLNPrePaidResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoritePulsaPrePaidResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoriteTelkomIndihomeResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoriteTransferResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.LatestTransactionResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.LatestTransactionPostPaidResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.PinFavoriteRequest
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.loyalty.CifBebasPoinResponse
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

    @GET("favorite-pulsa/get-favorite")
    fun getFavoritePulsaPrePaid(
        @Query("prepaidCategory") category: String = "prepaidCategory"
    ): Observable<BaseResponse<List<FavoritePulsaPrePaidResponse>>>

    @GET("favorite-telkom-bill/get-favorite")
    fun getFavoriteTelkomIndihome(): Observable<BaseResponse<List<FavoriteTelkomIndihomeResponse>>>

    @PUT("favorite/pin")
    fun pinFavorite(
        @Body request: PinFavoriteRequest
    ): Observable<BaseResponse<Nothing>>

    @GET("transaction-history/last-three-transactions")
    fun getLatestTransactionTransfer(): Observable<BaseResponse<List<LatestTransactionResponse>>>

    @GET("transaction-history/prepaid/last-three-transactions")
    fun getLatestPrePaidTransaction(
        @Query("type") type: String
    ): Observable<BaseResponse<List<LatestTransactionResponse>>>

    @GET("transaction-history/postpaid/last-three-transactions")
    fun getLatestPostPaidTransaction(
        @Query("type") type: String
    ): Observable<BaseResponse<List<LatestTransactionPostPaidResponse>>>

    @GET("customer-info/points")
    fun getCifBebasPoin(): Observable<BaseResponse<CifBebasPoinResponse>>
}