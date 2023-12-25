package com.fadlurahmanf.bebas_main.data.dto.argument

import android.os.Parcelable
import com.fadlurahmanf.bebas_main.data.dto.model.home.TransactionMenuModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductTransactionMenuArgument(
    var transactionMenus: List<TransactionMenuModel>
) : Parcelable
