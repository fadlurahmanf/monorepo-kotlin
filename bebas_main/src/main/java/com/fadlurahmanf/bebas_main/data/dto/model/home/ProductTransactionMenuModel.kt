package com.fadlurahmanf.bebas_main.data.dto.model.home

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductTransactionMenuModel(
    var productMenuId: String,
    @StringRes var productMenuLabel: Int,
    @DrawableRes var productImageMenu: Int,
    var isSelected: Boolean = false
) : Parcelable
