package com.example.checkit.ui.theme


import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    small = RoundedCornerShape(4.dp),    // 0.25rem -> botones pequeños, checkboxes
    medium = RoundedCornerShape(14.dp),   // 0.5rem -> tarjetas, listas
    large = RoundedCornerShape(24.dp),   // 0.75rem -> secciones grandes, contenedores
    extraLarge = RoundedCornerShape(9999.dp) // full -> círculos, icon buttons
)