package com.example.productionproject

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.math.BigDecimal

class FinanceViewModel : ViewModel() {
    val purchaseList = mutableStateListOf<PurchaseEntry>()

    val totalBalance: BigDecimal
        get() = purchaseList.fold(BigDecimal.ZERO) { acc, entry ->
            when (entry.type) {
                TransactionType.Income -> acc + entry.price
                TransactionType.Expense -> acc - entry.price
            }
        }


    fun addPurchase(
        title: String,
        amount: BigDecimal,
        type: TransactionType
        ) {
        purchaseList.add(0, PurchaseEntry(title, amount, type))
    }
}