package com.fadlurahmanf.bebas_api.network_state

import com.fadlurahmanf.bebas_shared.data.exception.BebasException

sealed class NetworkState<out T : Any> {
    object IDLE : NetworkState<Nothing>()
    object LOADING : NetworkState<Nothing>()
    data class SUCCESS<out T : Any>(val data: T) : NetworkState<T>()
    data class FAILED(val exception: BebasException) : NetworkState<Nothing>()
}