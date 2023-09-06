package com.fadlurahmanf.mapp_ui.presentation.view_model

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewModel:ViewModel() {
    fun compositeDisposable() = CompositeDisposable()
}