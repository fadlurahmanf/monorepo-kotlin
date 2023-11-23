package com.fadlurahmanf.bebas_storage.domain.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object BebasDbMigrations {
    val MANUAL_MIGRATION_11_12 = object : Migration(11, 12) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE t_bebas ADD COLUMN encryptedOnboardingId TEXT NULL")
        }
    }

    val MANUAL_MIGRATION_12_13 = object : Migration(12, 13) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE t_bebas ADD COLUMN base64ImageEktp TEXT NULL")
        }
    }

    val MANUAL_MIGRATION_13_14 = object : Migration(13, 14) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE t_bebas ADD COLUMN isFinishedEktpCameraVerification INTEGER NULL")
        }
    }

    val MANUAL_MIGRATION_14_15 = object : Migration(14, 15) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE t_bebas ADD COLUMN encryptedIdCardNumber TEXT NULL")
        }
    }
}