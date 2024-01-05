package com.fadlurahmanf.bebas_api.data.api

import com.fadlurahmanf.bebas_api.data.dto.general.BaseResponse
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryCheckoutFlowRequest
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryCheckoutFlowResponse
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface FulfillmentApi {
    @POST("api/v1/inquiry")
    fun inquiry(
        @Body request: InquiryCheckoutFlowRequest
    ): Observable<BaseResponse<InquiryCheckoutFlowResponse>>
}