package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.OrderPaymentSchemaRequest
import com.fadlurahmanf.bebas_api.data.dto.transaction.OrderPaymentSchemaResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.posting.PostingCheckoutResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.checkout.CheckoutTransactionPostingRequest
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface OrderApi {
    @POST("apps/order-confirmation")
    fun getOrderConfirmation(
        @Body request: OrderPaymentSchemaRequest
    ): Observable<BaseResponse<OrderPaymentSchemaResponse>>

    @PUT("apps/order-confirmation/{orderId}")
    fun reOrderConfirmation(
        @Path("orderId") orderId: String,
        @Body request: OrderPaymentSchemaRequest
    ): Observable<BaseResponse<OrderPaymentSchemaResponse>>

    @POST("apps/transaction/posting")
    fun postingTransaction(
        @Body request: CheckoutTransactionPostingRequest
    ): Observable<BaseResponse<PostingCheckoutResponse>>
}