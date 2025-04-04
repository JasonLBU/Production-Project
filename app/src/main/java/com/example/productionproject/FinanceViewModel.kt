package com.example.productionproject

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import java.math.BigDecimal

class FinanceViewModel : ViewModel() {
    val purchaseList = mutableStateListOf<PurchaseEntry>()

    fun addPurchase(title: String, amount: BigDecimal) {
        purchaseList.add(0, PurchaseEntry(title, amount))
    }
}