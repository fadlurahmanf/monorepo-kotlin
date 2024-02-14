package com.fadlurahmanf.core_logger.domain.datasources

import android.content.Context
import com.fadlurahmanf.core_logger.data.api.LogBetterStackApi
import com.fadlurahmanf.core_logger.data.request.LogBetterStackRequest
import com.fadlurahmanf.core_network.domain.network.CoreLogBetterStackNetwork

abstract class LogBetterStackRemoteDatasource(context: Context, tag: String) :
    CoreLogBetterStackNetwork<LogBetterStackApi>(context, tag = tag) {
    override fun getApi(): Class<LogBetterStackApi> = LogBetterStackApi::class.java

    fun log(message: String) = networkService().log(
        LogBetterStackRequest(
            message = message
        )
    )
}