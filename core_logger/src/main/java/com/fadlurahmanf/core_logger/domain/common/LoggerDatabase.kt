package com.fadlurahmanf.core_logger.domain.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fadlurahmanf.core_logger.data.constant.LoggerDbConstant
import com.fadlurahmanf.core_logger.data.dao.LoggerDao
import com.fadlurahmanf.core_logger.data.entity.LoggerEntity

@Database(
    entities = [
        LoggerEntity::class
    ], version = LoggerDatabase.VERSION,
    exportSchema = false
)
abstract class LoggerDatabase : RoomDatabase() {
    abstract fun loggerDao(): LoggerDao

    companion object {
        const val VERSION = 1

        @Volatile
        private var INSTANCE: LoggerDatabase? = null
        fun getDatabase(context: Context): LoggerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LoggerDatabase::class.java,
                    LoggerDbConstant.dbName
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}