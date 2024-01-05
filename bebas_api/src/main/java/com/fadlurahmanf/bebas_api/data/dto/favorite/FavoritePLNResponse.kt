package com.fadlurahmanf.bebas_api.data.dto.favorite

data class FavoritePLNResponse(
    val id: String? = null,
    val aliasName: String? = null,
    val providerName: String? = null,
    val accountNumber: String? = null,
    val categoryName: String? = null,
    val pinnedDate: String? = null,
    val isPinned: Boolean? = null,
)
