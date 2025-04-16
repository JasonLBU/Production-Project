package com.example.productionproject

import android.os.Bundle
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



        enableEdgeToEdge()
        setContent {

            val dao = TransactionDatabase.getDatabase(applicationContext).transactionDao()
            val viewModelFactory = FinanceViewModelFactory(dao)
            val viewModel: FinanceViewModel by viewModels { viewModelFactory }

            ProductionProjectTheme {
                FinanceApp(viewModel = viewModel)
            }
        }
    }
}
