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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.productionproject.ui.theme.ProductionProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProductionProjectTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavBar() }
                ) { innerPadding ->
                    InputPurchases(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun InputPurchases(modifier: Modifier = Modifier) {
    var textState by remember { mutableStateOf("") }
    val purchaseList = remember { mutableStateListOf<String>() }

    Column(
        modifier = modifier
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
                purchaseList.add(textState)
                textState = ""
            }
        }) {
            Text("Click me!")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LogPurchaseList(purchases = purchaseList)
    }
}

@Composable
fun LogPurchaseList(
    purchases: List<String>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(16.dp)
    ) {
        items(purchases) { purchase ->
            PurchaseItem(title = purchase, price = "£0.00")
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PurchaseItem(title: String, price: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            PurchaseInfo(title = title, price = price)
        }
    }
}

@Composable
fun PurchaseInfo(
    title: String,
    price: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = "Title: $title")
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
        modifier = Modifier.padding(8.dp)
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
        InputPurchases()
    }
}
