package com.fadlurahmanf.bebas_main.data.dto.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class TransactionMenuModel(
    var menuId: String,
    @StringRes var menuLabel: Int,
    @DrawableRes var imageMenu: Int,
)
