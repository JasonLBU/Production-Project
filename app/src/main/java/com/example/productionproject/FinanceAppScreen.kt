package com.example.productionproject

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


enum class FinanceAppScreen(@StringRes val title: Int) {
    History(R.string.budget_history_screen),
    Income(R.string.input_income_screen),
    Expense(R.string.input_expense_screen)
}


@Composable
fun FinanceApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    viewModel: FinanceViewModel = viewModel()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = FinanceAppScreen.valueOf(
        backStackEntry?.destination?.route ?: FinanceAppScreen.History.name
    )

    Scaffold(
        topBar = { FinanceAppBar(currentScreen = currentScreen) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = FinanceAppScreen.History.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = FinanceAppScreen.History.name) {
                HistoryScreen(
                    navController = navController,
                    purchases = viewModel.purchaseList,
                )
            }

            composable(route = FinanceAppScreen.Expense.name) {
                InputScreen(
                    navController = navController,
                    onSubmit = { title, amount ->
                        viewModel.addPurchase(title, amount)
                    }
                )
            }

            composable(route = FinanceAppScreen.Income.name) {
                InputScreen(
                    navController = navController,
                    onSubmit = { title, amount ->
                        viewModel.addPurchase(title, amount)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceAppBar(
    currentScreen: FinanceAppScreen,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = currentScreen.title))
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier
    )
}
