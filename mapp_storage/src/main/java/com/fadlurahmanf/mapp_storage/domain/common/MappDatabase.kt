package com.fadlurahmanf.mapp_storage.domain.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fadlurahmanf.mapp_storage.data.constant.MappDbConstant
import com.fadlurahmanf.mapp_storage.data.dao.MappDao
import com.fadlurahmanf.mapp_storage.data.entity.MappEntity

@Database(
    entities = [
        MappEntity::class
    ], version = MappDatabase.VERSION,
    exportSchema = false
)
abstract class MappDatabase : RoomDatabase() {
    abstract fun mappDao(): MappDao

    companion object {
        const val VERSION = 2

        @Volatile
        private var INSTANCE: MappDatabase? = null
        fun getDatabase(context: Context): MappDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MappDatabase::class.java,
                    MappDbConstant.dbName
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}