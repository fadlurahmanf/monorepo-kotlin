package com.fadlurahmanf.bebas_transaction.presentation.ppob

import com.fadlurahmanf.bebas_transaction.domain.repositories.TransactionRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import javax.inject.Inject

class TransactionConfirmationFlowCheckoutViewModel @Inject constructor(
    private val transactionRepositoryImpl: TransactionRepositoryImpl
):BaseViewModel() {
}