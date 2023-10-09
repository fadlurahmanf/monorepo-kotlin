package com.fadlurahmanf.core_logger.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fadlurahmanf.core_logger.data.constant.LoggerDbConstant
import java.util.Date

@Entity(tableName = LoggerDbConstant.tLogger)
data class LoggerEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var tag: String,
    var date: Long,
    var text: String,
)
