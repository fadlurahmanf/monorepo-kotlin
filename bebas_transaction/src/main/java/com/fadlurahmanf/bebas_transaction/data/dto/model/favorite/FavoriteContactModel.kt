package com.fadlurahmanf.bebas_transaction.data.dto.model.favorite

import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoritePLNPrePaidResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoriteTransferResponse

data class FavoriteContactModel(
    var id: String,
    var nameInFavoriteContact: String,
    var labelTypeOfFavorite: String,
    var accountNumber: String,
    var isPinned: Boolean = false,

    var additionalTransferData: FavoriteTransferResponse? = null,
    var additionalPlnPrePaidData: FavoritePLNPrePaidResponse? = null,
)
