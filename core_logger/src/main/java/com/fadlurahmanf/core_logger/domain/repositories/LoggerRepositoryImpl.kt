package com.fadlurahmanf.core_logger.domain.repositories

import android.util.Log
import com.fadlurahmanf.core_logger.data.entity.LoggerEntity
import com.fadlurahmanf.core_logger.domain.datasources.LoggerLocalDatasource
import javax.inject.Inject

class LoggerRepositoryImpl @Inject constructor(
    private val loggerLocalDatasource: LoggerLocalDatasource
) {

    companion object {
        const val DEFAULT_TAG = "CoreLogger"
    }

    private fun insert(entity: LoggerEntity) {
        try {
            loggerLocalDatasource.insert(entity)
        } catch (e: Throwable) {
            Log.e(DEFAULT_TAG, "insert: ${e.message}")
        }
    }

    fun d(tag: String = DEFAULT_TAG, message: String) {
        try {
            val entity = LoggerEntity(
                tag = tag,
                text = message,
                date = System.currentTimeMillis()
            )
            insert(entity)
            Log.d(tag, message)
        } catch (e: Exception) {
            Log.e(DEFAULT_TAG, "d: $e")
        }
    }
}