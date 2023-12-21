package com.fadlurahmanf.bebas_transaction.presentation.pin

import com.fadlurahmanf.bebas_transaction.domain.repositories.TransactionRepositoryImpl
import com.fadlurahmanf.bebas_ui.viewmodel.BaseViewModel
import javax.inject.Inject

class PinVerificationViewModel @Inject constructor(
    private val transactionRepositoryImpl: TransactionRepositoryImpl
) : BaseViewModel() {

}