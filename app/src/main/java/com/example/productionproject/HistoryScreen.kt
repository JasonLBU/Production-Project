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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.productionproject.data.Purchase
import kotlinx.coroutines.selects.select
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.abs

@Composable
fun HistoryScreen(
    purchases: List<Purchase>,
    navController: NavController,
    totalBalance: Double,
    viewModel: FinanceViewModel,
    modifier: Modifier = Modifier
) {
    var selectedEntry by remember { mutableStateOf<Purchase?>(null) }

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
                Text(text = stringResource(id = R.string.add_purchase))
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

        // Separate LazyColumn for the finance log
        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(purchases) { purchase ->
                FinanceItem(
                    entry = purchase,
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
            onUpdate = { viewModel.updatePurchase(it)},
            onDelete = { viewModel.deletePurchase(it) }
        )
    }
}

@Composable
fun FinanceItem(
    entry: Purchase,
    modifier: Modifier = Modifier,
    onClick: (Purchase) -> Unit
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

@Composable
fun FinanceItemDialog(
    entry: Purchase,
    onDismiss: () -> Unit,
    onUpdate: (Purchase) -> Unit,
    onDelete: (Purchase) -> Unit
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

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = {
                        val newPrice = updatedPrice.toDoubleOrNull()
                        if (updatedTitle.isNotBlank() && newPrice != null) {
                            val updatedPurchase = entry.copy(
                                title = updatedTitle,
                                price = newPrice
                            )
                            onUpdate(updatedPurchase)
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

@Composable
fun FinanceInfo(
    entry: Purchase,
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
