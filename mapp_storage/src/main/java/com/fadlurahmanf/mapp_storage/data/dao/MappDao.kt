package com.fadlurahmanf.mapp_storage.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fadlurahmanf.mapp_storage.data.constant.MappDbConstant
import com.fadlurahmanf.mapp_storage.data.entity.MappEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface MappDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: MappEntity)

    @Update
    fun update(value: MappEntity)

    @Query("SELECT * FROM ${MappDbConstant.tMapp}")
    fun getAll(): Single<List<MappEntity>>

    @Query("DELETE FROM ${MappDbConstant.tMapp}")
    fun delete()
}