package com.example.productionproject

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.productionproject.components.ExpenseCardColor
import com.example.productionproject.components.ExpenseLabelColor
import com.example.productionproject.components.IncomeCardColor
import com.example.productionproject.components.IncomeLabelColor
import com.example.productionproject.components.PriceLabel
import com.example.productionproject.components.TitleLabel
import com.example.productionproject.data.Transaction
import com.example.productionproject.data.TransactionFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

/**
 * Displays the history screen which shows the total balance and a list of transactions.
 *
 * @param navController The [NavController] for navigation.
 * @param transactions The list of transactions to display.
 * @param totalBalance The total balance from the difference of all listed expenses and income.
 * @param viewModel The [FinanceViewModel] that handles transaction operations.
 * @param modifier The [Modifier] to be applied to the screen
 */
@Composable
fun HistoryScreen(
    navController: NavController,
    transactions: List<Transaction>,
    totalBalance: Double,
    viewModel: FinanceViewModel,
    modifier: Modifier = Modifier
) {
    // Tracks which card is picked to bring up a dialog
    var selectedEntry by remember { mutableStateOf<Transaction?>(null) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Total amount displayed
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color = Color.Gray,
                    shape = RoundedCornerShape(8.dp)
                ),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.titleMedium
                )

                val sign = if (totalBalance < 0) "-" else ""
                Text(
                    text = "$sign £${"%.2f".format(abs(totalBalance))}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Row of Nav Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    navController.navigate(FinanceAppScreen.Expense.name)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(id = R.string.add_expense))
            }

            Button(
                onClick = {
                    navController.navigate(FinanceAppScreen.Income.name)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(text = stringResource(id = R.string.add_income))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add the "Delete All" button
        Button(
            onClick = { viewModel.deleteAllTransactions() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Delete All Entries")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Separate LazyColumn for the finance log
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(transactions) { transaction ->
                FinanceItem(
                    entry = transaction,
                    onClick = {
                        selectedEntry = it
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    selectedEntry?.let { entry ->
        FinanceItemDialog(
            entry = entry,
            onDismiss = { selectedEntry = null},
            onUpdate = { viewModel.updateTransaction(it)},
            onDelete = { viewModel.deleteTransaction(it) }
        )
    }
}
/**
 * A single transaction item card UI component.
 *
 * @param entry The [Transaction] to display.
 * @param modifier The [Modifier] for styling.
 * @param onClick Callback when the card is clicked.
 */
@Composable
fun FinanceItem(
    entry: Transaction,
    modifier: Modifier = Modifier,
    onClick: (Transaction) -> Unit
) {
    val cardColor = when (entry.type) {
        "Income" -> IncomeCardColor
        "Expense" -> ExpenseCardColor
        else -> MaterialTheme.colorScheme.surface
    }

    val labelColor = when (entry.type) {
        "Income" -> IncomeLabelColor
        "Expense" -> ExpenseLabelColor
        else -> MaterialTheme.colorScheme.onSurface
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(entry) },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            FinanceInfo(entry = entry, labelColor = labelColor)
        }
    }
}

/**
 * Dialog popup for editing or deleting a selected transaction.
 *
 * @param entry The [Transaction] being edited.
 * @param onDismiss Callback when the dialog is dismissed.
 * @param onUpdate Callback to trigger when updating a transaction.
 * @param onDelete Callback to trigger when deleting a transaction.
 */
@Composable
fun FinanceItemDialog(
    entry: Transaction,
    onDismiss: () -> Unit,
    onUpdate: (Transaction) -> Unit,
    onDelete: (Transaction) -> Unit
) {
    var updatedTitle by remember { mutableStateOf(entry.title) }
    var updatedPrice by remember { mutableStateOf(entry.price.toString()) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Edit Transaction", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    value = updatedTitle,
                    onValueChange = { updatedTitle = it },
                    label = { Text("Title") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                TextField(
                    value = updatedPrice,
                    onValueChange = { updatedPrice = it },
                    label = { Text("Amount") },
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Row consisting of Save, Delete and Cancel buttons
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        val newPrice = updatedPrice.toDoubleOrNull()
                        if (updatedTitle.isNotBlank() && newPrice != null) {
                            val updatedTransaction = entry.copy(
                                title = updatedTitle,
                                price = newPrice
                            )
                            onUpdate(updatedTransaction)
                            onDismiss()
                        }
                    }) {
                        Text("Save")
                    }

                    Button(onClick = {
                        onDelete(entry)
                        onDismiss()
                    }) {
                        Text("Delete")
                    }

                    Button(onClick = onDismiss) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

/**
 * Displays the transaction details, including title, date, and price.
 *
 * @param entry The [Transaction] entry details that are displayed.
 * @param labelColor Dynamic text color for "Income" or "Expense" label.
 * @param modifier The [Modifier] for additional styling.
 */
@Composable
fun FinanceInfo(
    entry: Transaction,
    labelColor: Color,
    modifier: Modifier = Modifier
) {
    val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    val formattedDate = dateFormat.format(Date(entry.date))

    val sign = when (entry.type) {
        "Income" -> "+"
        "Expense" -> "-"
        else -> ""
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left side: Title + Date in a column
        Column {
            Text(
                text = "Title: ${entry.title}",
                style = TitleLabel.copy(color = labelColor)
            )
            Text(
                text = "Date: $formattedDate",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = labelColor
                )
            )
        }

        // Right side: Price
        Text(
            text = "$sign £${"%.2f".format(entry.price)}",
            style = PriceLabel.copy(color = labelColor)
        )
    }
}
