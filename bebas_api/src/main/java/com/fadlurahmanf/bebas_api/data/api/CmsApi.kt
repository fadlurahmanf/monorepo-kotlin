package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.home.ProductTransactionMenuResponse
import com.fadlurahmanf.bebas_api.data.dto.promo.ItemPromoResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryPulsaDataRequest
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryPulsaDataResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.ItemBankResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CmsApi {
    @GET("homepage/menu")
    fun getTransactionMenu(): Observable<BaseResponse<List<ProductTransactionMenuResponse>>>

    @GET("transfer-bank/list-bank")
    fun getBankList(): Observable<BaseResponse<List<ItemBankResponse>>>

    @GET("homepage/apps/banner/promo")
    fun getHomePagePromo(): Observable<BaseResponse<List<ItemPromoResponse>>>

    @POST("mobile-provider/check-provider")
    fun inquiryPulsaData(
        @Body request: InquiryPulsaDataRequest
    ): Observable<BaseResponse<InquiryPulsaDataResponse>>
}