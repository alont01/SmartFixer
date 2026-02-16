package com.example.smartfixer.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE past_fixes ADD COLUMN videoLinksJson TEXT NOT NULL DEFAULT '[]'")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            """CREATE TABLE IF NOT EXISTS experts (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                category TEXT NOT NULL,
                phone TEXT NOT NULL,
                email TEXT NOT NULL,
                hourlyRate REAL NOT NULL,
                description TEXT NOT NULL,
                availability TEXT NOT NULL,
                rating REAL NOT NULL,
                yearsExperience INTEGER NOT NULL,
                certifications TEXT NOT NULL,
                serviceArea TEXT NOT NULL,
                createdAt INTEGER NOT NULL
            )"""
        )
    }
}

@Database(entities = [PastFixEntity::class, ExpertEntity::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pastFixDao(): PastFixDao
    abstract fun expertDao(): ExpertDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smartfixer_db"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
