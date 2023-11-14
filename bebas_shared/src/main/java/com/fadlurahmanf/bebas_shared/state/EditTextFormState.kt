package com.fadlurahmanf.bebas_shared.state

sealed class EditTextFormState<out T : Any>() {
    object EMPTY : EditTextFormState<Nothing>()
    data class SUCCESS<out T : Any>(val text: T) : EditTextFormState<T>()
    data class FAILED<out T : Any>(val text: T, val errorMessage: String) : EditTextFormState<T>()
}
