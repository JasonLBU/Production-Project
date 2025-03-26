package com.example.productionproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.productionproject.data.Purchase
import com.example.productionproject.data.PurchaseDao
import com.example.productionproject.data.PurchaseDatabase
import com.example.productionproject.ui.theme.ProductionProjectTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    private lateinit var purchaseDao: PurchaseDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProductionProjectTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavBar() }
                ) { innerPadding ->
                    TestInput(
                        modifier = Modifier.padding(innerPadding),
                        purchaseDao = purchaseDao
                    )
                }
            }
        }

        val db = PurchaseDatabase.getDatabase(this)
        purchaseDao = db.purchaseDao()
    }
}

@Composable
fun TestInput(modifier: Modifier = Modifier, purchaseDao: PurchaseDao) {
    var textState by remember { mutableStateOf("") }
    val textList = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = textState,
            onValueChange = { newText ->
                textState = newText
                Log.i("USER_INPUT", "value: $newText")
            },
            label = { Text("Enter Name") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (textState.isNotBlank()) {

                CoroutineScope(Dispatchers.IO).launch {
                    val newPurchase = Purchase(title = textState)
                    purchaseDao.insertPurchase(newPurchase)

                    // Retrieve all entries from the database
                    val purchases = purchaseDao.getAllPurchases()

                    // Update the UI on the main thread
                    withContext(Dispatchers.Main) {
                        textList.clear()
                        textList.addAll(purchases.map { it.title })
                        textState = ""
                    }
                }
            }
        }) {
            Text(text = "Click me!")
        }

        LazyColumn {
            items(textList) { text ->
                Text(text = text)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun ExpenseList(modifier: Modifier = Modifier) {
    val expenses = listOf(
        Pair(stringResource(id = R.string.expense_1), stringResource(id = R.string.expense_price_1)),
        Pair(stringResource(id = R.string.expense_2), stringResource(id = R.string.expense_price_2)),
        Pair(stringResource(id = R.string.expense_3), stringResource(id = R.string.expense_price_3)),
        Pair(stringResource(id = R.string.expense_4), stringResource(id = R.string.expense_price_4)),
        Pair(stringResource(id = R.string.expense_5), stringResource(id = R.string.expense_price_5))
    )

    LazyColumn(
        modifier = modifier.padding(16.dp)
    ) {
        items(expenses) { expense ->
            ExpenseItem(title = expense.first, price = expense.second)
            Spacer(modifier = Modifier.height(8.dp)) // Space between items
        }
    }
}

@Composable
fun ExpenseItem(title: String, price: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            ExpenseInfo(title = title, price = price)
        }
    }
}

@Composable
fun ExpenseInfo(
    title: String,
    price: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Title: $title"
        )
        Text(text = "Price: $price")
    }
}

@Composable
fun BottomNavBar() {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            BottomNavButton(icon = Icons.Filled.Home, label = "Home")
            BottomNavButton(icon = Icons.Filled.Search, label = "Search")
            BottomNavButton(icon = Icons.Filled.Person, label = "Profile")
        }
    }
}

@Composable
fun BottomNavButton(icon: ImageVector, label: String) {
    Column(
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp) // Add some padding around the button to avoid clipping
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$label Icon",
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    ProductionProjectTheme {
        TestInput(

        )
    }
}

