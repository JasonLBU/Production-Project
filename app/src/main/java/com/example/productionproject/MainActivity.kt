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
import java.math.BigDecimal

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ProductionProjectTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { AppTopBar() },
                    bottomBar = { BottomNavBar() }
                ) { innerPadding ->
                    BottomNavBar(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}


@Composable
fun LogPurchaseList(
    purchases: List<PurchaseEntry>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(16.dp)
    ) {
        items(purchases) { purchase ->
            PurchaseItem(title = purchase.title, price = purchase.price)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun PurchaseItem(title: String, price: BigDecimal, modifier: Modifier = Modifier) {
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
    price: BigDecimal,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(text = "Title: $title")
        Text(text = "Price: £${price.setScale(2)}")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text("Purchase History") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
fun BottomNavBar(modifier: Modifier = Modifier) {
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
