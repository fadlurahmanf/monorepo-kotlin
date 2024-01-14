package com.fadlurahmanf.bebas_transaction.data.dto.result

import android.os.Parcelable
import com.fadlurahmanf.bebas_api.data.dto.transaction.checkout.CheckoutTransactionDataRequest
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionConfirmationResult(
    val selectedAccountNumber: String,
    val selectedAccountName: String,
    val additionalPLNPrePaidCheckout: PLNPrePaidCheckout? = null
) : Parcelable {
    @Parcelize
    data class PLNPrePaidCheckout(
        val orderId: String,
        val configGroupId: String,
        val paymentSourceSchema: List<CheckoutTransactionDataRequest.Schema> = listOf(),
        val paymentTypeCode: String,
    ) : Parcelable
}
