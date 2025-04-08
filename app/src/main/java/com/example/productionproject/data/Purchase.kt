package com.example.productionproject.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.productionproject.TransactionType
import java.math.BigDecimal

@Entity(tableName = "purchases")
data class Purchase(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val price: BigDecimal,
    val type: TransactionType
)