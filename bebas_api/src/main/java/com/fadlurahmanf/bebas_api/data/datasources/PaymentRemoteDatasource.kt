package com.fadlurahmanf.bebas_api.data.datasources

import android.content.Context
import com.fadlurahmanf.bebas_api.data.api.PaymentApi
import com.fadlurahmanf.bebas_api.data.dto.payment_service.PaymentSourceConfigRequest
import com.fadlurahmanf.bebas_api.domain.network.PaymentNetwork
import javax.inject.Inject

class PaymentRemoteDatasource @Inject constructor(
    context: Context
) : PaymentNetwork<PaymentApi>(context) {
    override fun getApi(): Class<PaymentApi> = PaymentApi::class.java

    fun getPaymentSourceConfig(request: PaymentSourceConfigRequest) =
        networkService().getPaymentSourceConfig(request)
}