package com.fadlurahmanf.bebas_ui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    fun compositeDisposable() = CompositeDisposable()
}