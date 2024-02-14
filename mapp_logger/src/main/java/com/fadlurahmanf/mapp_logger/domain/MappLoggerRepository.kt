package com.fadlurahmanf.mapp_logger.domain

import android.util.Log
import com.fadlurahmanf.core_logger.data.entity.LoggerEntity
import com.fadlurahmanf.mapp_logger.domain.datasources.MappLogBetterStackRemoteDatasource
import com.fadlurahmanf.mapp_logger.domain.datasources.MappLoggerLocalDatasource
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject

class MappLoggerRepository @Inject constructor(
    private val mappLoggerLocalDatasource: MappLoggerLocalDatasource,
    private val mappLogBetterStackRemoteDatasource: MappLogBetterStackRemoteDatasource
) {
    fun logRemotely(message: String) {
        try {
            mappLogBetterStackRemoteDatasource.log(message).execute()
        } catch (e: Exception) {
            Log.e("MappLoggerRepository", "failed to log remotely")
        }
    }

    fun saveLogLocally(entity: LoggerEntity) {
        try {
            mappLoggerLocalDatasource.insert(entity)
        } catch (e: Exception) {
            Log.e("MappLoggerRepository", "failed save log locally")
        }
    }
}