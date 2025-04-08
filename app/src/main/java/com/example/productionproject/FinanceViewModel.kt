package com.example.productionproject

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productionproject.data.Purchase
import com.example.productionproject.data.PurchaseDao
import com.example.productionproject.data.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.math.BigDecimal

class FinanceViewModel(private val dao: PurchaseDao) : ViewModel() {

    private val _purchaseList = MutableStateFlow<List<Purchase>>(emptyList())
    val purchaseList: StateFlow<List<Purchase>> = _purchaseList.asStateFlow()

    val totalBalance: BigDecimal
        get() = _purchaseList.value.fold(BigDecimal.ZERO) { acc, purchase ->
            when (purchase.type) {
                TransactionType.Income -> acc + purchase.price
                TransactionType.Expense -> acc - purchase.price
            }
        }

    init {
        loadPurchases()
    }

    fun addPurchase(title: String, amount: BigDecimal, type: TransactionType) {
        viewModelScope.launch {
            val newPurchase = Purchase(title = title, price = amount, type = type)
            dao.insertPurchase(newPurchase)
            loadPurchases()
        }
    }

    private fun loadPurchases() {
        viewModelScope.launch {
            _purchaseList.value = dao.getAllPurchases()
        }
    }
}