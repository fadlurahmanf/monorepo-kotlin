package com.fadlurahmanf.mapp_api.external.helper.network_state

sealed class NetworkState<out T : Any> {
    object IDLE : NetworkState<Nothing>()
    object LOADING : NetworkState<Nothing>()
    data class SUCCESS<out T : Any>(val data: T) : NetworkState<T>()
    data class FAILED(val exception: Exception) : NetworkState<Nothing>()
}