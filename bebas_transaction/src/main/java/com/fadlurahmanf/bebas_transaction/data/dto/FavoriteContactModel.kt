package com.fadlurahmanf.bebas_transaction.data.dto

data class FavoriteContactModel(
    val id:String,
    val nameInFavoriteContact:String,
    val labelTypeOfFavorite:String,
    val accountNumber:String,
    var isPinned:Boolean = false,
)
