package com.fadlurahmanf.bebas_transaction.data.dto.argument

import android.os.Parcelable
import com.fadlurahmanf.bebas_api.data.dto.transfer.InquiryBankResponse
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransferDetailArgument(
    var isFavorite: Boolean,
    var isFavoriteEnabled: Boolean = false,
    var accountName: String,
    var realAccountName: String,
    var accountNumber: String,
    var bankImageUrl: String? = null,
    var bankName: String,

    val inquiryBank: InquiryBankResponse
) : Parcelable
