package com.fadlurahmanf.bebas_ui.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {

    var baseDisposable: CompositeDisposable = CompositeDisposable()
    fun compositeDisposable() = CompositeDisposable()

    fun dispose() {
        baseDisposable.clear()
    }
}