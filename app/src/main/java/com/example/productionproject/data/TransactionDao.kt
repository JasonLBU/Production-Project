package com.example.productionproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

/**
 * Acknowledgement:
 * Development of this Room database file was aided from official documentation guide, and modified
 * accordingly to interact with the logic and files of this app
 *
 * https://developer.android.com/training/data-storage/room
 *
 * Data Access Object (DAO) for performing CRUD operations on the transactions table.
 */
@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: Transaction): Long

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAllTransaction(): Flow<List<Transaction>>

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()

}