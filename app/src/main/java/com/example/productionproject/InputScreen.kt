package com.example.productionproject

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.productionproject.ui.theme.ProductionProjectTheme
import java.math.BigDecimal

data class PurchaseEntry(val title: String, val price: BigDecimal)

@Composable
fun InputScreen() {
    var titleState by remember { mutableStateOf("") }
    var priceState by remember { mutableStateOf("") }
    val purchaseList = remember { mutableStateListOf<PurchaseEntry>() }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        TextField(
            value = titleState,
            onValueChange = { newText ->
                titleState = newText
            },
            label = { Text("Enter Title") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = priceState,
            onValueChange = { newText ->
                priceState = newText
            },
            label = { Text("Enter Price") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (titleState.isNotBlank() && priceState.isNotBlank()) {
                try {
                    val priceDecimal = priceState.toBigDecimal()
                    purchaseList.add(PurchaseEntry(titleState, priceDecimal))
                    titleState = ""
                    priceState = ""
                } catch (e: NumberFormatException) {
                    Log.e("INPUT_ERROR", "Invalid price input")
                }
            }
        }) {
            Text("Add Purchase")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LogPurchaseList(purchases = purchaseList)
    }
}



@Preview(showBackground = true)
@Composable
fun MainPagePreview() {
    ProductionProjectTheme {
        InputScreen()
    }
}