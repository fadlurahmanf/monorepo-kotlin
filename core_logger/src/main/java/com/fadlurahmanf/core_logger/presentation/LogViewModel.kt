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
                Log.d("MappLogger", "SUCCESS ${it.size}")
                _logs.value = CustomState.SUCCESS(ArrayList(it))
            },
            {
                Log.e("MappLogger", "ERROR: ${it.message}")
                _logs.value = CustomState.FAILED(Exception(it.message))
            },
            {}
        ))
    }
}