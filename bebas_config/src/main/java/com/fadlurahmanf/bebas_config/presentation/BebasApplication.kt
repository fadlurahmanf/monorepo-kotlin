package com.fadlurahmanf.bebas_config.presentation

import android.app.Application
import com.fadlurahmanf.core_logger.domain.datasources.LoggerLocalDatasource
import com.fadlurahmanf.core_logger.presentation.LogConsole

class BebasApplication : Application() {
    lateinit var logConsole: LogConsole

    override fun onCreate() {
        super.onCreate()
        initLogConsole()
    }

    private fun initLogConsole() {
        val loggerLocalDatasource = LoggerLocalDatasource(applicationContext)
        logConsole = LogConsole(loggerLocalDatasource, "BebasLogger")
    }
}