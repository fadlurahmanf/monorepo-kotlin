package com.fadlurahmanf.bebas_transaction.data.dto.model.favorite

import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoritePLNResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoritePulsaPrePaidResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoriteTelkomIndihomeResponse
import com.fadlurahmanf.bebas_api.data.dto.favorite.FavoriteTransferResponse

data class FavoriteContactModel(
    var id: String,
    /** OR ALIAS NAME */
    var favoriteLabel: String,
    var favoriteSubLabel: String,
    var identifierNumber: String,
    var isPinned: Boolean = false,

    var additionalTransferData: FavoriteTransferResponse? = null,
    var additionalPlnPrePaidData: FavoritePLNResponse? = null,
    var additionalPlnPostPaidData: FavoritePLNResponse? = null,
    var additionalPulsaPrePaidData: FavoritePulsaPrePaidResponse? = null,
    var additionalTelkomIndihome: FavoriteTelkomIndihomeResponse? = null,
)
