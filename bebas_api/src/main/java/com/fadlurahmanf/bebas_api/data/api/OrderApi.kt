package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.order_service.OrderPaymentSchemaRequest
import com.fadlurahmanf.bebas_api.data.dto.order_service.OrderPaymentSchemaResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface OrderApi {
    @POST("apps/order-confirmation")
    fun getOrderConfirmation(
        @Body request: OrderPaymentSchemaRequest
    ): Observable<BaseResponse<OrderPaymentSchemaResponse>>
}