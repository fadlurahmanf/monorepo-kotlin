package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.FulfillmentApi
import com.fadlurahmanf.bebas_api.data.dto.ppob.InquiryCheckoutFlowRequest
import com.fadlurahmanf.bebas_api.domain.network.FulfillmentNetwork
import javax.inject.Inject

class FulfillmentRemoteDatasource @Inject constructor(
    context: Context
) : FulfillmentNetwork<FulfillmentApi>(context) {
    override fun getApi(): Class<FulfillmentApi> = FulfillmentApi::class.java

    fun inquiryCheckoutFlow(request:InquiryCheckoutFlowRequest) = networkService().inquiry(request)
}