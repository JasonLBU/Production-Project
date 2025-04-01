package com.example.productionproject

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

    // Nav buttons
    Column(modifier = modifier.padding(16.dp)) {

        // Row of buttons at the top
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

    // List
    LazyColumn(
        modifier = modifier.padding(16.dp)
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