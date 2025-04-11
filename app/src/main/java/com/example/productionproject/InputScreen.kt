package com.example.productionproject

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

/**
 * Input screen for adding a new transaction. Shared between "Income" and "Expense" screens,
 * due to using the same format in storing data.
 *
 * This composable provides text fields for a transaction's title and amount,
 * and a button to submit the information. It then calls the [onSubmit] callback.
 *
 * @param navController The [NavController] for navigating back after submission.
 * @param transactionType A string representing the type of transaction "Income" or"Expense").
 * @param onSubmit A callback that handles submission of the transaction.
 */
@Composable
fun InputScreen(
    navController: NavController,
    transactionType: String,
    onSubmit: (String, Double, String) -> Unit
) {
    var titleState by rememberSaveable { mutableStateOf("") }
    var amountState by rememberSaveable { mutableStateOf("") }

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
                    val price = amountState.toDouble()
                    onSubmit(titleState, price, transactionType)

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