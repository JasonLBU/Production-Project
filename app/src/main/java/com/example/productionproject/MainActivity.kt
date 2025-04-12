package com.example.productionproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.productionproject.data.TransactionDatabase
import com.example.productionproject.ui.theme.ProductionProjectTheme
import com.google.firebase.Firebase
import com.google.firebase.database.database


/**
 * The main entry point for the Production Project app.
 *
 * It initializes the [TransactionDatabase] and retrieves the DAO to create an
 * instance of [FinanceViewModel] via [FinanceViewModelFactory].
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Write a message to the firebase realtime database
        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")
            .addOnSuccessListener {
                Log.d("FirebaseDB", "Message written successfully!")
            }
            .addOnFailureListener { error ->
                Log.w("FirebaseDB", "Error writing message", error)
            }

        enableEdgeToEdge()
        setContent {
            val dao = TransactionDatabase.getDatabase(applicationContext).transactionDao()
            val viewModelFactory = FinanceViewModelFactory(dao)
            val viewModel: FinanceViewModel by viewModels { viewModelFactory }

            val database = Firebase.database
            val myRef = database.getReference("message")

            myRef.setValue("Hello, World!")

            ProductionProjectTheme {
                FinanceApp(viewModel = viewModel)
            }
        }
    }
}
