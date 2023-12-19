package com.fadlurahmanf.bebas_transaction.data.state

import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_transaction.data.dto.FavoriteContactModel
import com.fadlurahmanf.bebas_transaction.data.dto.LatestTransactionModel

sealed class InquiryBankState {
    object IDLE : InquiryBankState()
    object LOADING : InquiryBankState()
    data class SUCCESS(
        val result: InquiryBankResponse,
        val isFromFavorite: Boolean = false,
        val favoriteModel: FavoriteContactModel? = null,
        val isFromLatest: Boolean = false,
        val latestModel: LatestTransactionModel? = null,
    ) : InquiryBankState()

    data class FAILED(
        val exception: BebasException,
    ) : InquiryBankState()
}
