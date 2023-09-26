package com.fadlurahmanf.mapp_api.external.helper.network_state

import com.fadlurahmanf.mapp_api.data.exception.MappException

sealed class NetworkState<out T : Any> {
    object IDLE : NetworkState<Nothing>()
    object LOADING : NetworkState<Nothing>()
    data class SUCCESS<out T : Any>(val data: T) : NetworkState<T>()
    data class FAILED(val exception: MappException) : NetworkState<Nothing>()
}