package com.fadlurahmanf.bebas_transaction.data.state

import com.fadlurahmanf.bebas_api.data.dto.transfer.BankResponse
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.FavoriteContactModel
import com.fadlurahmanf.bebas_transaction.data.dto.LatestTransactionModel

sealed class BankListState {
    object IDLE : BankListState()
    object LOADING : BankListState()
    data class SUCCESS(
        val otherBanks:List<BankResponse>,
        val topBanks:List<BankResponse>,
    ) : BankListState()

    data class FAILED(
        val exception: BebasException,
    ) : BankListState()
}
