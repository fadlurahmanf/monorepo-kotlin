package com.fadlurahmanf.mapp_example.data.dto.model

import androidx.annotation.DrawableRes

data class MenuModel(
    var menuId: String,
    var menuTitle: String,
    var menuSubTitle: String,
    @DrawableRes val icon:Int,
)
