package com.fadlurahmanf.mapp_logger.domain.datasources

import android.content.Context
import com.fadlurahmanf.core_logger.domain.datasources.LogBetterStackRemoteDatasource
import com.fadlurahmanf.mapp_shared.MappShared

class MappLogBetterStackRemoteDatasource(context: Context) : LogBetterStackRemoteDatasource(context, tag = "MappLoggerNetwork") {
    override val logBetterStackToken: String
        get() = MappShared.logBetterStackToken
}