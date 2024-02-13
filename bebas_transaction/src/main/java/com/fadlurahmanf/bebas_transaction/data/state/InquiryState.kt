package com.fadlurahmanf.bebas_transaction.data.state

import com.fadlurahmanf.bebas_api.data.dto.transaction.inquiry.InquiryBankResponse
import com.fadlurahmanf.bebas_api.data.dto.others.ItemBankResponse
import com.fadlurahmanf.bebas_shared.data.exception.BebasException
import com.fadlurahmanf.bebas_shared.data.exception.FulfillmentException
import com.fadlurahmanf.bebas_transaction.data.dto.model.favorite.FavoriteContactModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.favorite.LatestTransactionModel
import com.fadlurahmanf.bebas_transaction.data.dto.model.transfer.InquiryResultModel

sealed class InquiryState {
    object IDLE : InquiryState()
    object LOADING : InquiryState()
    data class SuccessFromFavoriteActivity(
        val result: InquiryResultModel,
        val isFromFavorite: Boolean = false,
        val favoriteModel: FavoriteContactModel? = null,
        val isFromLatest: Boolean = false,
        val latestModel: LatestTransactionModel? = null,
    ) : InquiryState()

    data class SuccessFromListActivity(
        val result: InquiryBankResponse,
        val selectedBank: ItemBankResponse,
        val destinationAccount: String,
        val isInquiryBankMas: Boolean = false,
    ) : InquiryState()

    data class FailedBebas(
        val exception: BebasException,
    ) : InquiryState()

    data class FailedFulfillment(
        val exception: FulfillmentException,
    ) : InquiryState()
}
