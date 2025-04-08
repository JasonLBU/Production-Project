package com.example.productionproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseDao {
    @Insert
    suspend fun insertPurchase(purchase: Purchase)

    @Query("SELECT * FROM purchases")
    fun getAllPurchases(): Flow<List<Purchase>>
}