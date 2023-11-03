package com.fadlurahmanf.bebas_storage.domain.datasource

import android.content.Context
import com.fadlurahmanf.bebas_storage.data.entity.BebasEntity
import com.fadlurahmanf.bebas_storage.domain.common.BebasDatabase
import com.fadlurahmanf.core_crypto.domain.repositories.CryptoRSARepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class BebasLocalDatasource @Inject constructor(
    context: Context,
    coreRSARepository: CryptoRSARepository
) {
    private var dao = BebasDatabase.getDatabase(context).bebasDao()

    fun insert(value: BebasEntity) {
        dao.insert(value)
    }

    fun insertOrReplaceWithExisting(value: BebasEntity) {
        dao.insert(value)
    }

    fun update(value: BebasEntity) = dao.update(value)
    fun getAll() = dao.getAll()
    fun isDataExist(): Single<Boolean> {
        return dao.getAll().map {
            it.isNotEmpty()
        }
    }

    fun delete() = dao.delete()
}