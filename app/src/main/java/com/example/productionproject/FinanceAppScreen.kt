package com.example.productionproject

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


enum class FinanceAppScreen(@StringRes val title: Int) {
    History(R.string.budget_history_screen),
    Income(R.string.input_income_screen),
    Expense(R.string.input_expense_screen)
}

@Composable
fun FinanceApp(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = FinanceAppScreen.History.name,
        modifier = modifier
    ) {
        composable(route = FinanceAppScreen.History.name) {
            HistoryScreen(
                purchases = emptyList(), // will update with real data later
                navController = navController,
                modifier = modifier
            )
        }

        composable(route = FinanceAppScreen.Expense.name) {
            InputScreen(
                navController = navController,
                modifier = modifier
            )
        }

        composable(route = FinanceAppScreen.Income.name) {
            InputScreen(
                navController = navController,
                modifier = modifier
            )
        }
    }
}