package com.fadlurahmanf.bebas_storage.domain.common

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.fadlurahmanf.bebas_storage.data.constant.BebasDbConstant
import com.fadlurahmanf.bebas_storage.data.dao.BebasDao
import com.fadlurahmanf.bebas_storage.data.entity.BebasEntity
import com.fadlurahmanf.bebas_storage.domain.converter.BebasConverters

@Database(
    entities = [
        BebasEntity::class
    ], version = BebasDatabase.VERSION,
//    autoMigrations = [
//        AutoMigration(
//            from = 11,
//            to = 12,
//            spec = BebasDatabase.AUTO_MIGRATION_SPEC_11_12::class
//        )
//    ],
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
        const val VERSION = 13

        val MANUAL_MIGRATION_11_12 = object : Migration(11, 12) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE t_bebas ADD COLUMN encryptedOnboardingId TEXT")
            }
        }

        val MANUAL_MIGRATION_12_13 = object : Migration(12, 13) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE t_bebas ADD COLUMN base64ImageEktp TEXT")
            }
        }

        @Volatile
        private var INSTANCE: BebasDatabase? = null
        fun getDatabase(context: Context): BebasDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BebasDatabase::class.java,
                    BebasDbConstant.dbName
                )
                    .addMigrations(MANUAL_MIGRATION_11_12, MANUAL_MIGRATION_12_13)
//                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    class AUTO_MIGRATION_SPEC_11_12 : AutoMigrationSpec {}
}