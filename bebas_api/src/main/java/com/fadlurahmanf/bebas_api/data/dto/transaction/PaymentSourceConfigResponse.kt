package com.fadlurahmanf.bebas_api.data.dto.transaction

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentSourceConfigResponse(
    val id: String? = null,
    val currencyLabel: String? = null,
    val currencyCode: String? = null,
    val paymentSources: List<Source>? = null,
) : Parcelable {

    @Parcelize
    data class Source(
        val code: String? = null,
        val name: String? = null,
        val type: String? = null,
        val accountHolderName: String? = null,
        val mainAccount: Boolean? = null,
        val accountNumber: String? = null,
        val accountBalance: Double? = null,
        val minimumBalanceEligible: Double? = null,
        val holdPointExecutionAt: String? = null,
        val usagePriority: Int? = null,
        val eligible: Boolean? = null,
    ) : Parcelable
}
