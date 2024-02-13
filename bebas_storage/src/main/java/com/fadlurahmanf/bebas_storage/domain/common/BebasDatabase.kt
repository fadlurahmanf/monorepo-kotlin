package com.fadlurahmanf.bebas_storage.domain.common

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fadlurahmanf.bebas_storage.data.constant.BebasDbConstant
import com.fadlurahmanf.bebas_storage.data.dao.BebasDao
import com.fadlurahmanf.bebas_storage.data.entity.BebasEntity
import com.fadlurahmanf.bebas_storage.domain.converter.BebasConverters
import com.fadlurahmanf.bebas_storage.domain.migrations.BebasDbMigrations

@Database(
    entities = [
        BebasEntity::class
    ], version = BebasDatabase.VERSION,
    exportSchema = true
)
@TypeConverters(
    value = [
        BebasConverters::class
    ]
)
abstract class BebasDatabase : RoomDatabase() {
    abstract fun bebasDao(): BebasDao

    companion object {
        const val VERSION = 16

        @Volatile
        private var INSTANCE: BebasDatabase? = null
        fun getDatabase(context: Context): BebasDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BebasDatabase::class.java,
                    BebasDbConstant.dbName
                )
                    .addMigrations(
                        BebasDbMigrations.MANUAL_MIGRATION_11_12,
                        BebasDbMigrations.MANUAL_MIGRATION_12_13,
                        BebasDbMigrations.MANUAL_MIGRATION_13_14,
                        BebasDbMigrations.MANUAL_MIGRATION_14_15,
                        BebasDbMigrations.MANUAL_MIGRATION_15_16,
                    )
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}