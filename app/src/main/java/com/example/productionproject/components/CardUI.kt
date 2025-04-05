package com.example.productionproject.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val IncomeCardColor = Color(0xFFD0F0C0) // light green
val IncomeLabelColor = Color(0xFF2E7D32)  // dark green

val ExpenseCardColor = Color(0xFFFFC1C1) // light red
val ExpenseLabelColor = Color(0xFFD32F2F) // dark red

val PriceLabel = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.Bold,
    fontFamily = FontFamily.Default
)

val TitleLabel = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.Medium,
    fontFamily = FontFamily.Default
)