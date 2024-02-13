package com.fadlurahmanf.core_logger.presentation

import android.util.Log
import com.fadlurahmanf.core_logger.data.entity.LoggerEntity
import com.fadlurahmanf.core_logger.domain.datasources.LoggerLocalDatasource

class LogConsole(
    private val loggerLocalDatasource: LoggerLocalDatasource,
    private var defaultTag: String = "CoreLogger"
) {

    private fun insert(entity: LoggerEntity) {
        try {
            loggerLocalDatasource.insert(entity)
        } catch (e: Throwable) {
            Log.e(defaultTag, "insert: ${e.message}")
        }
    }

    fun d(tag: String = defaultTag, message: String) {
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
            Log.e(defaultTag, "d: $e")
        }
    }

    fun d(message: String) {
        try {
            val entity = LoggerEntity(
                tag = defaultTag,
                type = "DEBUG",
                text = message,
                date = System.currentTimeMillis()
            )
            insert(entity)
            Log.d(defaultTag, message)
        } catch (e: Exception) {
            Log.e(defaultTag, "d: $e")
        }
    }

    fun i(tag: String = defaultTag, message: String) {
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
            Log.e(defaultTag, "i: $e")
        }
    }

    fun i(message: String) {
        try {
            val entity = LoggerEntity(
                tag = defaultTag,
                type = "INFO",
                text = message,
                date = System.currentTimeMillis()
            )
            insert(entity)
            Log.i(defaultTag, message)
        } catch (e: Exception) {
            Log.e(defaultTag, "i: $e")
        }
    }

    fun e(tag: String = defaultTag, message: String) {
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
            Log.e(defaultTag, "e: $e")
        }
    }

    fun e(message: String) {
        try {
            val entity = LoggerEntity(
                tag = defaultTag,
                type = "ERROR",
                text = message,
                date = System.currentTimeMillis()
            )
            insert(entity)
            Log.e(defaultTag, message)
        } catch (e: Exception) {
            Log.e(defaultTag, "e: $e")
        }
    }
}