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
import kotlinx.coroutines.selects.select
import java.math.BigDecimal

@Composable
fun HistoryScreen(
    purchases: List<PurchaseEntry>,
    navController: NavController,
    totalBalance: BigDecimal,
    modifier: Modifier = Modifier
) {
    var selectedEntry by remember { mutableStateOf<PurchaseEntry?>(null) }

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

                val sign = if (totalBalance < BigDecimal.ZERO) "-" else ""
                Text(
                    text = "$sign £${totalBalance.abs().setScale(2)}",
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
            onDismiss = { selectedEntry = null}
        )
    }
}

@Composable
fun FinanceItem(
    entry: PurchaseEntry,
    modifier: Modifier = Modifier,
    onClick: (PurchaseEntry) -> Unit
) {
    val cardColor = when (entry.type) {
        TransactionType.Income -> IncomeCardColor
        TransactionType.Expense -> ExpenseCardColor
    }

    val labelColor = when (entry.type) {
        TransactionType.Income -> IncomeLabelColor
        TransactionType.Expense -> ExpenseLabelColor
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
    entry: PurchaseEntry,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Transaction Details", style = MaterialTheme.typography.titleLarge)

                Spacer(modifier = Modifier.height(12.dp))

                Text("Title: ${entry.title}")
                Text("Amount: £${entry.price.setScale(2)}")
                Text("Type: ${entry.type.name}")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = onDismiss) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
fun FinanceInfo(
    entry: PurchaseEntry,
    labelColor: Color,
    modifier: Modifier = Modifier
) {
    val sign = when (entry.type) {
        TransactionType.Income -> "+"
        TransactionType.Expense -> "-"
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Title: ${entry.title}",
            style = TitleLabel.copy(color = labelColor)
        )
        Text(
            text = "$sign £${entry.price.setScale(2)}",
            style = PriceLabel.copy(color = labelColor)
        )
    }
}

fun getTransactionSign(type: TransactionType): String = when (type) {
    TransactionType.Income -> "+"
    TransactionType.Expense -> "-"
}

