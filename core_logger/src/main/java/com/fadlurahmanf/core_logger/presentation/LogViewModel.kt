package com.fadlurahmanf.core_logger.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fadlurahmanf.core_logger.data.entity.LoggerEntity
import com.fadlurahmanf.core_logger.domain.datasources.LoggerLocalDatasource
import com.fadlurahmanf.core_logger.external.CustomState
import io.reactivex.rxjava3.disposables.CompositeDisposable

class LogViewModel(private val loggerLocalDatasource: LoggerLocalDatasource) : ViewModel() {

    private fun compositeDisposable() = CompositeDisposable()

    private val _logs = MutableLiveData<CustomState<ArrayList<LoggerEntity>>>()
    val logs: LiveData<CustomState<ArrayList<LoggerEntity>>> = _logs

    fun getAllLogger() {
        _logs.value = CustomState.LOADING
        compositeDisposable().add(loggerLocalDatasource.getAll().toObservable().subscribe(
            {
                _logs.value = CustomState.SUCCESS(ArrayList(it))
            },
            {
                _logs.value = CustomState.FAILED(Exception(it.message))
            },
            {}
        ))
    }

    fun getTypedLogger(type: String) {
        _logs.value = CustomState.LOADING
        compositeDisposable().add(loggerLocalDatasource.getTyped(type).toObservable().subscribe(
            {
                _logs.value = CustomState.SUCCESS(ArrayList(it))
            },
            {
                _logs.value = CustomState.FAILED(Exception(it.message))
            },
            {}
        ))
    }
}