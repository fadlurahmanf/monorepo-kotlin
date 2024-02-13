package com.fadlurahmanf.bebas_api.data.dto.transaction

data class OrderPaymentSchemaResponse(
    val orderId: String? = null,
    val paymentSchema: PaymentSchema? = null,
) {
    data class PaymentSchema(
        val total: Double? = null,
        val currencyLabel: String? = null,
        val currencyCode: String? = null,
        val details: List<Detail>? = null
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
