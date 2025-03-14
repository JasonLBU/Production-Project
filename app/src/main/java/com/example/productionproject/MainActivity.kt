package com.example.productionproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.productionproject.ui.theme.ProductionProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProductionProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ExpenseList(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TestInput(modifier: Modifier = Modifier) {
    var textState by remember { mutableStateOf("") }

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
            singleLine = true // Ensure single-line input
        )

        Spacer(modifier = Modifier.height(16.dp)) // Add space between UI elements

        Text(text = "You typed: $textState")
    }
}

@Composable
fun ExpenseList() {

}

@Composable
fun ExpenseItem(modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Row(modifier = Modifier) {

            // Call another composable that holds the info
            ExpenseInfo()
            // Potentially add image of its expense category
        }
    }
}

@Composable
fun ExpenseInfo(
    title: String,
    price: Double,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = "Title: $title"
        )
        Text(text = "Price: $price")
    }
}



@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    ProductionProjectTheme {
        ExpenseList()
    }
}

