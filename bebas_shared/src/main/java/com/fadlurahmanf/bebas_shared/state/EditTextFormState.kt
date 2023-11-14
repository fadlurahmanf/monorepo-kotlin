package com.fadlurahmanf.bebas_shared.state

import androidx.annotation.StringRes

sealed class EditTextFormState<out T : Any>() {
    object EMPTY : EditTextFormState<Nothing>()
    data class SUCCESS<out T : Any>(val text: T) : EditTextFormState<T>()
    data class FAILED<out T : Any>(val text: T, @StringRes val idRawStringRes: Int) :
        EditTextFormState<T>()
}
