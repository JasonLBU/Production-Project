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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

/**
 * Class to define the available routes of the app
 */
enum class FinanceAppScreen(@StringRes val title: Int) {
    History(R.string.budget_history_screen),
    Income(R.string.input_income_screen),
    Expense(R.string.input_expense_screen)
}

/**
 * Composable function to set up the main finance app navigation and layout
 *
 * @param navController NavHostController used for navigation
 * @param modifier Modifier applied to the layout
 * @param viewModel FinanceViewModel used for data management
 */
@Composable
fun FinanceApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    viewModel: FinanceViewModel
) {
    val transactions by viewModel.transactionList.collectAsState()
    val backStackEntry by navController.currentBackStackEntryAsState()
    // Determine the current screen based on the current nav route; if no route, default to History.
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
                    transactions = transactions,
                    totalBalance = viewModel.totalBalance,
                    viewModel = viewModel
                )
            }

            composable(route = FinanceAppScreen.Expense.name) {
                InputScreen(
                    navController = navController,
                    transactionType = "Expense",
                    onSubmit = { title, amount, type->
                        viewModel.addTransaction(title, amount, type)
                    }
                )
            }

            composable(route = FinanceAppScreen.Income.name) {
                InputScreen(
                    navController = navController,
                    transactionType = "Income",
                    onSubmit = { title, amount, type->
                        viewModel.addTransaction(title, amount, type)
                    }
                )
            }
        }
    }
}

/**
 * Function to set up the top bar UI
 *
 * @param currentScreen Adjusts the title based on the current screen
 * @param modifier Modifier applied to the top bar
 */
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
