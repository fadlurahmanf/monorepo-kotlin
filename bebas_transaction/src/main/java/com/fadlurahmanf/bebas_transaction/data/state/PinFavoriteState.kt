package com.fadlurahmanf.bebas_transaction.data.state

import com.fadlurahmanf.bebas_shared.data.exception.BebasException

sealed class PinFavoriteState {
    object IDLE : PinFavoriteState()
    object LOADING : PinFavoriteState()
    data class SuccessPinned(
        val isPinned: Boolean
    ) : PinFavoriteState()

    data class FAILED(
        val exception: BebasException,
        val favoriteId: String,
        val previousStatePinned: Boolean
    ) :
        PinFavoriteState()
}
