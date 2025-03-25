package com.example.productionproject.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface PurchaseDao {
    @Insert
    fun insertPurchase(purchase: Purchase)

    @Update
    fun updatePurchase(purchase: Purchase)

    @Delete
    fun deletePurchase(purchase: Purchase)

    @Query("SELECT * FROM purchases")
    fun getAllPurchases(): List<Purchase>

    @Query("SELECT * FROM purchases WHERE id = :id")
    fun getPurchaseById(id: Int): Purchase
}