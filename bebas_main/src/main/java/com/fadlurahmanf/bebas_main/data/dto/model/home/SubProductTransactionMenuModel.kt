package com.fadlurahmanf.bebas_main.data.dto.model.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class SubProductTransactionMenuModel(
    var subProductMenuId: String,
    @StringRes var subProductMenuLabel: Int,
    @DrawableRes var subProductImageMenu: Int,
    var productTransactionMenu: ProductTransactionMenuModel
)
