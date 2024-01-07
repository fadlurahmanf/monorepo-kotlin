package com.fadlurahmanf.bebas_api.data.dto.order_service

data class OrderPaymentSchemaResponse(
    val orderId: String? = null,
) {
    data class PaymentSchema(
        val total: Double? = null,
        val currencyLabel: String? = null,
        val currencyCode: String? = null,
    ) {
        data class Detail(
            val label: String? = null,
            val amount: Double? = null,
        )

        data class PaymentSource(
            val accountNumber: String? = null,
            val amount: Double? = null,
            val code: String? = null,
            val status: Boolean? = null,
            val type: String? = null,
        )
    }
}
