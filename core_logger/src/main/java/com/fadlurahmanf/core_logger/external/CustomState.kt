package com.fadlurahmanf.core_logger.external

sealed class CustomState<out T : Any> {
    object IDLE : CustomState<Nothing>()
    object LOADING : CustomState<Nothing>()
    data class SUCCESS<out T : Any>(val data: T) : CustomState<T>()
    data class FAILED(val exception: Exception) : CustomState<Nothing>()
}
