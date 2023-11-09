package com.fadlurahmanf.bebas_storage.domain.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fadlurahmanf.bebas_storage.data.constant.BebasDbConstant
import com.fadlurahmanf.bebas_storage.data.dao.BebasDao
import com.fadlurahmanf.bebas_storage.data.entity.BebasEntity

@Database(
    entities = [
        BebasEntity::class
    ], version = BebasDatabase.VERSION,
    exportSchema = false
)
abstract class BebasDatabase : RoomDatabase() {
    abstract fun bebasDao(): BebasDao

    companion object {
        const val VERSION = 4

        @Volatile
        private var INSTANCE: BebasDatabase? = null
        fun getDatabase(context: Context): BebasDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BebasDatabase::class.java,
                    BebasDbConstant.dbName
                ).fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}