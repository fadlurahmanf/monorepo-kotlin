package com.fadlurahmanf.core_logger.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fadlurahmanf.core_logger.data.constant.LoggerDbConstant
import com.fadlurahmanf.core_logger.data.entity.LoggerEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface LoggerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: LoggerEntity)

    @Update
    fun update(value: LoggerEntity)

    @Query("SELECT * FROM ${LoggerDbConstant.tLogger}")
    fun getAll(): Single<List<LoggerEntity>>

    @Query("DELETE FROM ${LoggerDbConstant.tLogger}")
    fun delete()
}