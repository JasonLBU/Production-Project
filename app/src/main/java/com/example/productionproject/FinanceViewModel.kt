package com.example.productionproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productionproject.data.Transaction
import com.example.productionproject.data.TransactionDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FinanceViewModel(private val dao: TransactionDao) : ViewModel() {

    private val _transactionList = MutableStateFlow<List<Transaction>>(emptyList())
    val transactionList: StateFlow<List<Transaction>> = _transactionList.asStateFlow()

    val totalBalance: Double
        get() = _transactionList.value.sumOf { transaction ->
            if (transaction.type == "Income") transaction.price
            else -transaction.price
        }

    init {
        viewModelScope.launch {
            dao.getAllTransaction().collect { transactions ->
                _transactionList.value = transactions
            }
        }
    }

    fun addTransaction(title: String, amount: Double, type: String) {
        viewModelScope.launch {
            val transaction = Transaction(title = title, price = amount, type = type)
            dao.insertTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            dao.updateTransaction(transaction)
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            dao.deleteTransaction(transaction)
        }
    }
}