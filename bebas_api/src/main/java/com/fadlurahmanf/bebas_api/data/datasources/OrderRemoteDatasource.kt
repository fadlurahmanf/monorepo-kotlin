package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.OrderApi
import com.fadlurahmanf.bebas_api.data.dto.transaction.OrderPaymentSchemaRequest
import com.fadlurahmanf.bebas_api.data.dto.transaction.checkout.CheckoutTransactionPostingRequest
import com.fadlurahmanf.bebas_api.domain.network.OrderNetwork
import javax.inject.Inject

class OrderRemoteDatasource @Inject constructor(
    context: Context
) : OrderNetwork<OrderApi>(context) {
    override fun getApi(): Class<OrderApi> = OrderApi::class.java

    fun orderTransactionSchema(request: OrderPaymentSchemaRequest) =
        networkService().getOrderConfirmation(request)

    fun reorderTransactionSchema(orderId: String, request: OrderPaymentSchemaRequest) =
        networkService().reOrderConfirmation(
            orderId = orderId,
            request = request
        )

    fun postingTransaction(request: CheckoutTransactionPostingRequest) =
        networkService().postingTransaction(request)
}