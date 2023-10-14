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
                type = "DEBUG",
                text = message,
                date = System.currentTimeMillis()
            )
            insert(entity)
            Log.d(tag, message)
        } catch (e: Exception) {
            Log.e(DEFAULT_TAG, "d: $e")
        }
    }

    fun d(message: String) {
        try {
            val entity = LoggerEntity(
                tag = DEFAULT_TAG,
                type = "DEBUG",
                text = message,
                date = System.currentTimeMillis()
            )
            insert(entity)
            Log.d(DEFAULT_TAG, message)
        } catch (e: Exception) {
            Log.e(DEFAULT_TAG, "d: $e")
        }
    }

    fun i(tag: String = DEFAULT_TAG, message: String) {
        try {
            val entity = LoggerEntity(
                tag = tag,
                type = "INFO",
                text = message,
                date = System.currentTimeMillis()
            )
            insert(entity)
            Log.i(tag, message)
        } catch (e: Exception) {
            Log.e(DEFAULT_TAG, "i: $e")
        }
    }

    fun i(message: String) {
        try {
            val entity = LoggerEntity(
                tag = DEFAULT_TAG,
                type = "INFO",
                text = message,
                date = System.currentTimeMillis()
            )
            insert(entity)
            Log.i(DEFAULT_TAG, message)
        } catch (e: Exception) {
            Log.e(DEFAULT_TAG, "i: $e")
        }
    }

    fun e(tag: String = DEFAULT_TAG, message: String) {
        try {
            val entity = LoggerEntity(
                tag = tag,
                type = "ERROR",
                text = message,
                date = System.currentTimeMillis()
            )
            insert(entity)
            Log.e(tag, message)
        } catch (e: Exception) {
            Log.e(DEFAULT_TAG, "e: $e")
        }
    }

    fun e(message: String) {
        try {
            val entity = LoggerEntity(
                tag = DEFAULT_TAG,
                type = "ERROR",
                text = message,
                date = System.currentTimeMillis()
            )
            insert(entity)
            Log.e(DEFAULT_TAG, message)
        } catch (e: Exception) {
            Log.e(DEFAULT_TAG, "e: $e")
        }
    }
}