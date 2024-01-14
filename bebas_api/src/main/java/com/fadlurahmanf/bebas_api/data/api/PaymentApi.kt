package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.PaymentSourceConfigRequest
import com.fadlurahmanf.bebas_api.data.dto.transaction.PaymentSourceConfigResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentApi {
    @POST("apps/payment-source-config")
    fun getPaymentSourceConfig(
        @Body paymentSourceConfigRequest: PaymentSourceConfigRequest
    ): Observable<BaseResponse<List<PaymentSourceConfigResponse>>>
}