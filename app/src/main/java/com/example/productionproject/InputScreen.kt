package com.example.productionproject

import android.util.Log
import androidx.annotation.StringRes
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.productionproject.ui.theme.ProductionProjectTheme
import java.math.BigDecimal

data class PurchaseEntry(val title: String, val price: BigDecimal)

@Composable
fun InputScreen(
    purchaseList: MutableList<PurchaseEntry>,
    navController: NavController,
) {
    var titleState by remember { mutableStateOf("") }
    var amountState by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = titleState,
            onValueChange = { newText ->
                titleState = newText
            },
            label = { Text(text = stringResource(id = R.string.enter_title)) },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = amountState,
            onValueChange = { newText ->
                amountState = newText
            },
            label = { Text(text = stringResource(id = R.string.enter_amount)) },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (titleState.isNotBlank() && amountState.isNotBlank()) {
                try {
                    val priceDecimal = amountState.toBigDecimal()
                    purchaseList.add(0, PurchaseEntry(titleState, priceDecimal))
                    titleState = ""
                    amountState = ""
                    navController.popBackStack() // Return to history screen
                } catch (e: NumberFormatException) {
                    Log.e("INPUT_ERROR", "Invalid price input")
                }
            }
        }) {
            Text("Confirm")
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}