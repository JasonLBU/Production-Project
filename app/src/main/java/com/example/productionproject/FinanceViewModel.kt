package com.example.productionproject

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.productionproject.data.Transaction
import com.example.productionproject.data.TransactionDao
import com.example.productionproject.data.TransactionFactory
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel class responsible for handling transaction data.
 *
 * This ViewModel interacts with the [TransactionDao] to observe
 * and modify the list of transactions, and it updates the total balance.
 */
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

    /**
     * Adds a new transaction.
     *
     * @param title The title of the transaction.
     * @param amount The monetary amount.
     * @param type The type of transaction ("Income" or "Expense").
     */
    fun addTransaction(title: String, amount: Double, type: String) {
        viewModelScope.launch {
            var transaction = Transaction(title = title, price = amount, type = type)
            val generatedId = dao.insertTransaction(transaction)

            transaction = transaction.copy(id = generatedId.toInt())

            val database = Firebase.database
            // Used the generatedId as the key so that the node's key is "1", "2", etc., not a random push ID.
            val transactionRef = database.getReference("transactions").child(generatedId.toString())

            transactionRef.setValue(transaction)
                .addOnSuccessListener {
                    Log.d("FirebaseDB", "Transaction added to Firebase with id $generatedId successfully!")
                }
                .addOnFailureListener { e ->
                    Log.w("FirebaseDB", "Error adding transaction to Firebase", e)
                }
        }
    }

    /**
     * Updates an existing transaction.
     *
     * @param transaction The transaction object with updated fields.
     */
    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // Update locally in Room
            dao.updateTransaction(transaction)

            // Update the transaction in the Firebase Realtime Database.
            val database = Firebase.database
            val transactionRef = database.getReference("transactions")
                .child(transaction.id.toString())

            transactionRef.setValue(transaction)
                .addOnSuccessListener {
                    Log.d("FirebaseDB", "Transaction updated successfully in Firebase.")
                }
                .addOnFailureListener { e ->
                    Log.w("FirebaseDB", "Error updating transaction in Firebase", e)
                }
        }
    }

    /**
     * Deletes a transaction.
     *
     * @param transaction The transaction to delete.
     */
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // Delete locally in Room
            dao.deleteTransaction(transaction)

            // Remove the transaction child node from Firebase.
            val database = Firebase.database
            val transactionRef = database.getReference("transactions")
                .child(transaction.id.toString())

            transactionRef.removeValue()
                .addOnSuccessListener {
                    Log.d("FirebaseDB", "Transaction deleted successfully from Firebase.")
                }
                .addOnFailureListener { e ->
                    Log.w("FirebaseDB", "Error deleting transaction from Firebase", e)
                }
        }
    }

    /**
     * Deletes all transactions from the local database and
     * clears the 'transactions' node in the Firebase database.
     */
    fun deleteAllTransactions() {
        viewModelScope.launch {
            // Delete from Room
            dao.deleteAllTransactions()

            // Delete from Firebase
            val database = Firebase.database
            val transactionsRef = database.getReference("transactions")
            transactionsRef.removeValue()
                .addOnSuccessListener {
                    Log.d("FirebaseDB", "All transactions deleted from Firebase successfully!")
                }
                .addOnFailureListener { e ->
                    Log.w("FirebaseDB", "Error deleting all transactions from Firebase", e)
                }
        }
    }
}