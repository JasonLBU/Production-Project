package com.example.productionproject

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productionproject.data.Purchase
import com.example.productionproject.data.PurchaseDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class FinanceViewModel(private val dao: PurchaseDao) : ViewModel() {

    private val _purchaseList = MutableStateFlow<List<Purchase>>(emptyList())
    val purchaseList: StateFlow<List<Purchase>> = _purchaseList.asStateFlow()

    val totalBalance: Double
        get() = _purchaseList.value.fold(0.0) { acc, purchase ->
            when (purchase.type) {
                "Income" -> acc + purchase.price
                "Expense" -> acc - purchase.price
                else -> acc
            }
        }

    init {
        viewModelScope.launch {
            dao.getAllPurchases().collect { purchases ->
                _purchaseList.value = purchases
            }
        }
    }

    fun addPurchase(title: String, amount: Double, type: String) {
        viewModelScope.launch {
            val purchase = Purchase(title = title, price = amount, type = type)
            dao.insertPurchase(purchase)
        }
    }
}