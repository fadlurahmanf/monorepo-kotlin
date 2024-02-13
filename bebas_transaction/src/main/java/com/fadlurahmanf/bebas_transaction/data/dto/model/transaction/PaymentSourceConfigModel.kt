package com.fadlurahmanf.bebas_transaction.data.dto.model.transaction

import com.fadlurahmanf.bebas_transaction.data.dto.model.PaymentSourceModel

data class PaymentSourceConfigModel(
    val mainPaymentSource: PaymentSourceModel,
    val loyaltyPointPaymentSource: PaymentSourceModel? = null,
    val paymentSourcesAvailable: List<PaymentSourceModel>,
    val paymentSourcesWithoutLoyaltyPoint: List<PaymentSourceModel>,
)
