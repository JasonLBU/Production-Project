package com.example.productionproject.data

import kotlin.random.Random

enum class TransactionType {
    Income,
    Expense
}

class TransactionFactory {
    // Defined sample titles for income and expenses.
    private val incomeTitles = listOf("Salary", "Bonus", "Cash", "Investment", "commisi")
    private val expenseTitles = listOf("Groceries", "Utilities", "Rent", "Entertainment", "Transportation")

    /**
     * Generates a random [Transaction] instance.
     */
    fun generateRandomTransaction(): Transaction {
        val random = Random(System.currentTimeMillis())

        // Randomly choose a TransactionType from the enum.
        val type = TransactionType.values().random()

        // Use the type to decide which title to select.
        val title = if (type == TransactionType.Income) {
            incomeTitles.random()
        } else {
            expenseTitles.random()
        }

        // Generate a random amount
        val amount = random.nextDouble(10.0, 1000.0)
        return Transaction(
            title = title,
            price = amount,
            type = type.name,
            date = System.currentTimeMillis()
        )
    }
}