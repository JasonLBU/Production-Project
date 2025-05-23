package com.example.productionproject.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Acknowledgement:
 * Development of this room database file was aided from official documentation guide, and modified
 * accordingly to interact with the logic and files of this app
 *
 * https://developer.android.com/training/data-storage/room
 *
 * Entity data class representing a financial transaction in the app*
 */
@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "type")val type: String,
    @ColumnInfo(name = "date") val date: Long = System.currentTimeMillis()
)