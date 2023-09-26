package com.fadlurahmanf.mapp_storage.domain.datasource

import android.content.Context
import com.fadlurahmanf.mapp_storage.data.entity.MappEntity
import com.fadlurahmanf.mapp_storage.domain.common.MappDatabase
import javax.inject.Inject

class MappLocalDatasource @Inject constructor(
    context: Context
) {
    private var dao = MappDatabase.getDatabase(context).mappDao()

    fun insert(value: MappEntity) = dao.insert(value)
    fun update(value: MappEntity) = dao.update(value)
    fun getAll() = dao.getAll()
    fun delete() = dao.delete()
}