package com.fadlurahmanf.bebas_main.data.dto.model.home

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransactionMenuModel(
    var menuId: String,
    @StringRes var menuLabel: Int,
    @DrawableRes var imageMenu: Int,
) : Parcelable
