package com.fadlurahmanf.bebas_transaction.data.dto.model

import android.os.Parcelable
import com.fadlurahmanf.bebas_api.data.dto.bank_account.BankAccountResponse
import com.fadlurahmanf.bebas_api.data.dto.transaction.PaymentSourceConfigResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaymentSourceModel(
    val accountName: String,
    val accountNumber: String,
    val subLabel: String,
    val balance: Double,

    val bankAccountResponse: BankAccountResponse? = null,
    val paymentSourceConfig: PaymentSourceConfigResponse? = null,
    val paymentSource: PaymentSourceConfigResponse.Source? = null,
) : Parcelable
