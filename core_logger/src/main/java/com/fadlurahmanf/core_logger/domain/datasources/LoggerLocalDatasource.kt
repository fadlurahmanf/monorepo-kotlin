package com.fadlurahmanf.core_logger.domain.datasources

import android.content.Context
import com.fadlurahmanf.core_logger.data.entity.LoggerEntity
import com.fadlurahmanf.core_logger.domain.common.LoggerDatabase

class LoggerLocalDatasource(
    context: Context
) {
    private var dao = LoggerDatabase.getDatabase(context).loggerDao()

    fun insert(value: LoggerEntity) = dao.insert(value)
    fun update(value: LoggerEntity) = dao.update(value)
    fun getAll() = dao.getAll()
    fun getTyped(type: String) = dao.getTyped(type)
    fun delete() = dao.delete()
}