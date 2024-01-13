package com.fadlurahmanf.bebas_transaction.external

import com.fadlurahmanf.bebas_transaction.data.dto.model.PaymentSourceModel
import com.fadlurahmanf.bebas_transaction.data.dto.result.TransactionConfirmationResult

interface TransactionConfirmationCallback {
    fun onButtonTransactionConfirmationClicked(result: TransactionConfirmationResult)
    fun onChangePaymentSource(selectedPaymentSource: PaymentSourceModel)
}