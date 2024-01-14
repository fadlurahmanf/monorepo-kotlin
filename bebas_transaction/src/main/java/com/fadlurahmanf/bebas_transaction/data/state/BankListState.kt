package com.fadlurahmanf.bebas_transaction.data.state

import com.fadlurahmanf.bebas_api.data.dto.others.ItemBankResponse
import com.fadlurahmanf.bebas_shared.data.exception.BebasException

sealed class BankListState {
    object IDLE : BankListState()
    object LOADING : BankListState()
    data class SUCCESS(
        val otherBanks:List<ItemBankResponse>,
        val topBanks:List<ItemBankResponse>,
    ) : BankListState()

    data class FAILED(
        val exception: BebasException,
    ) : BankListState()
}
