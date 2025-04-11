package com.example.productionproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productionproject.data.Purchase
import com.example.productionproject.data.PurchaseDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FinanceViewModel(private val dao: PurchaseDao) : ViewModel() {

    private val _purchaseList = MutableStateFlow<List<Purchase>>(emptyList())
    val purchaseList: StateFlow<List<Purchase>> = _purchaseList.asStateFlow()

    val totalBalance: Double
        get() = _purchaseList.value.sumOf { purchase ->
            if (purchase.type == "Income") purchase.price
            else -purchase.price
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

    fun updatePurchase(purchase: Purchase) {
        viewModelScope.launch {
            dao.updatePurchase(purchase)
        }
    }

    fun deletePurchase(purchase: Purchase) {
        viewModelScope.launch {
            dao.deletePurchase(purchase)
        }
    }
}