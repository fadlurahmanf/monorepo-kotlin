package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import com.fadlurahmanf.bebas_transaction.data.dto.model.PaymentSourceModel
import com.fadlurahmanf.bebas_transaction.data.flow.SelectPaymentSourceFlow
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectPaymentSourceArgument(
    val flow: SelectPaymentSourceFlow
) : Parcelable