package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.OrderApi
import com.fadlurahmanf.bebas_api.data.dto.order_service.OrderPaymentSchemaRequest
import com.fadlurahmanf.bebas_api.data.dto.transfer.CheckoutTransactionPostingRequest
import com.fadlurahmanf.bebas_api.domain.network.OrderNetwork
import javax.inject.Inject

class OrderRemoteDatasource @Inject constructor(
    context: Context
) : OrderNetwork<OrderApi>(context) {
    override fun getApi(): Class<OrderApi> = OrderApi::class.java

    fun orderTransactionSchema(request: OrderPaymentSchemaRequest) =
        networkService().getOrderConfirmation(request)

    fun postingTransaction(request: CheckoutTransactionPostingRequest) =
        networkService().postingTransaction(request)
}