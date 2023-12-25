package com.fadlurahmanf.bebas_main.data.dto.model.home

import com.fadlurahmanf.bebas_api.data.dto.bank_account.BankAccountResponse

data class HomeBankAccountModel(
    var savingTypeLabel: String = "MAS Saving",
    val accountBalance: Double,
    val accountNumber: String,
    var isPinned: Boolean = false,
    var isBalanceVisible: Boolean = false,

    var response: BankAccountResponse
)
