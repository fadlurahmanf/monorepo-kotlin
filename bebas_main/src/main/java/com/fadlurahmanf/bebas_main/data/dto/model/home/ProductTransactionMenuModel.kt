package com.fadlurahmanf.bebas_main.data.dto.model.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ProductTransactionMenuModel(
    var productMenuId: String,
    @StringRes var productMenuLabel: Int,
    @DrawableRes var productImageMenu: Int,
    var transactionMenu: TransactionMenuModel
)
