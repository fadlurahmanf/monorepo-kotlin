package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.cif.EStatementResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoritePLNResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoritePulsaPrePaidResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoriteTelkomIndihomeResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoriteTransferResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.LatestTransactionResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.LatestTransactionPostPaidResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.LatestTransactionPulsaPrePaidResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.PinFavoriteRequest
import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.loyalty.CifBebasPoinResponse
import com.google.gson.JsonObject
import io.reactivex.rxjava3.core.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface CifApi {

    @POST("statement/period")
    fun getEStatements(
        @Body request:JsonObject
    ): Observable<BaseResponse<EStatementResponse>>

    @POST("statement/download")
    fun downloadEStatement(
        @Body request:JsonObject
    ): Call<ResponseBody>

    @GET("favorite")
    fun getFavoriteTransfer(): Observable<BaseResponse<List<FavoriteTransferResponse>>>

    @GET("favorite-prepaid/get-favorite")
    fun getFavoritePLNPrePaid(): Observable<BaseResponse<List<FavoritePLNResponse>>>

    @GET("favorite-bill-payment/get-favorite")
    fun getFavoritePLNPostPaid(): Observable<BaseResponse<List<FavoritePLNResponse>>>

    @GET("favorite-pulsa/get-favorite")
    fun getFavoritePulsaPrePaid(
        @Query("prepaidCategory") category: String = "prepaidCategory"
    ): Observable<BaseResponse<List<FavoritePulsaPrePaidResponse>>>

    @GET("favorite-telkom-bill/get-favorite")
    fun getFavoriteTelkomIndihome(): Observable<BaseResponse<List<FavoriteTelkomIndihomeResponse>>>

    @GET("favorite-ewallet/get-favorite")
    fun getFavoriteTopUpEWallet(): Observable<BaseResponse<List<FavoriteTelkomIndihomeResponse>>>

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

    @GET("transaction-history/prepaid/last-three-transactions")
    fun getLatestPulsaPrePaidTransaction(
        @Query("type") type: String = "Pulsa"
    ): Observable<BaseResponse<List<LatestTransactionPulsaPrePaidResponse>>>

    @GET("transaction-history/postpaid/last-three-transactions")
    fun getLatestPostPaidTransaction(
        @Query("type") type: String
    ): Observable<BaseResponse<List<LatestTransactionPostPaidResponse>>>

    @GET("customer-info/points")
    fun getCifBebasPoin(): Observable<BaseResponse<CifBebasPoinResponse>>
}