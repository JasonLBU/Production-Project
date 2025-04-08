package com.example.productionproject.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "purchases")
data class Purchase(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "description") val title: String,
    @ColumnInfo(name = "amount") val price: Double,
    @ColumnInfo(name = "type")val type: String
)