package com.example.productionproject

import androidx.compose.foundation.border
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.math.BigDecimal

@Composable
fun HistoryScreen(
    purchases: List<PurchaseEntry>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
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
                Text(
                    text = "£0.00", // Placeholder for now
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
    }

    // Separate LazyColumn for the finance log
    LazyColumn(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(top = 8.dp)
    ) {
        items(purchases) { purchase ->
            FinanceItem(title = purchase.title, price = purchase.price)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun FinanceItem(title: String, price: BigDecimal, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            FinanceInfo(title = title, price = price)
        }
    }
}

@Composable
fun FinanceInfo(
    title: String,
    price: BigDecimal,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = "Title: $title")
        Text(text = "Price: £${price.setScale(2)}")
    }
}