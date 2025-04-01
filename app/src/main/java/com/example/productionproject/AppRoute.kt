package com.example.productionproject

import androidx.annotation.StringRes

enum class AppRoute(@StringRes val title: Int) {
    History(R.string.budget_history_screen),
    Input(R.string.input_screen)
}