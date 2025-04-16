package com.example.productionproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Acknowledgement:
 * Development of this Room database file was aided from official documentation guide, and modified
 * accordingly to interact with the logic and files of this app
 *
 * https://developer.android.com/training/data-storage/room
 *
 * The Room database for the finance tracker app.
 *
 * Contains a single table [Transaction] and serves as the main access point
 * to the app's persisted data via [TransactionDao].
 *
 * Use [TransactionDatabase.getDatabase] to obtain an instance of this database.
 */
@Database(entities = [Transaction::class], version = 1)
abstract class TransactionDatabase: RoomDatabase() {
    abstract fun transactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: TransactionDatabase? = null

        fun getDatabase(context: Context): TransactionDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TransactionDatabase::class.java,
                    "Log-transactions"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}