package com.fadlurahmanf.bebas_ui.extension

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

fun View.clearFocusAndDismissKeyboard() {
    clearFocus()
    dismissKeyboard()
}

fun View.dismissKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}