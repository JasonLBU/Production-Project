package com.example.productionproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.productionproject.data.PurchaseDatabase
import com.example.productionproject.ui.theme.ProductionProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val dao = PurchaseDatabase.getDatabase(applicationContext).purchaseDao()
            val viewModelFactory = FinanceViewModelFactory(dao)
            val viewModel: FinanceViewModel by viewModels { viewModelFactory }

            ProductionProjectTheme {
                FinanceApp(viewModel = viewModel)
            }
        }
    }
}
