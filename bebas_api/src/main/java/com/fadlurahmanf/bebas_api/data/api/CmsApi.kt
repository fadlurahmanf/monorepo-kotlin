package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.home.HomePageBannerInfoResponse
import com.fadlurahmanf.bebas_api.data.dto.home.ProductTransactionMenuResponse
import com.fadlurahmanf.bebas_api.data.dto.loyalty.ProgramCategoryResponse
import com.fadlurahmanf.bebas_api.data.dto.promo.ItemPromoResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryPulsaDataRequest
import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryPulsaDataResponse
import com.fadlurahmanf.bebas_api.data.dto.others.ItemBankResponse
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

    @GET("homepage/banner/info")
    fun getBannerInfo(): Observable<BaseResponse<List<HomePageBannerInfoResponse>>>

    @GET("banner/promo/category")
    fun getProgramCategory(): Observable<BaseResponse<List<ProgramCategoryResponse>>>
}