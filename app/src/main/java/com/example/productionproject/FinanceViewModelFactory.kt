package com.example.productionproject

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.productionproject.data.TransactionDao

/**
 * Factory class for creating instances of [FinanceViewModel].
 *
 * This factory ensures that the [FinanceViewModel] is provided with the
 * required [TransactionDao] dependency.
 *
 * @property dao An instance of [TransactionDao] used for database operations.
 */
class FinanceViewModelFactory(
    private val dao: TransactionDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FinanceViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FinanceViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
