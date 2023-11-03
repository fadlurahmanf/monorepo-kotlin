package com.fadlurahmanf.bebas_storage.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.fadlurahmanf.bebas_storage.data.constant.BebasDbConstant
import com.fadlurahmanf.bebas_storage.data.entity.BebasEntity
import io.reactivex.rxjava3.core.Single

@Dao
interface BebasDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(value: BebasEntity)

    @Update
    fun update(value: BebasEntity)

    @Query("SELECT * FROM ${BebasDbConstant.tMapp}")
    fun getAll(): Single<List<BebasEntity>>

    @Query("DELETE FROM ${BebasDbConstant.tMapp}")
    fun delete()
}