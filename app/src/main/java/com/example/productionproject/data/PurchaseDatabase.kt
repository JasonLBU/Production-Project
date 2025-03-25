package com.example.productionproject.data

import androidx.room.Database

@Database(entities = [Purchase::class], version = 1)
abstract class PurchaseDatabase {
    abstract fun purchaseDao(): PurchaseDao
}