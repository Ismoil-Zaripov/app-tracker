package uz.learningtracker.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(50),   // pill: chips, rating circles, button
    small = RoundedCornerShape(16.dp),     // text fields
    medium = RoundedCornerShape(20.dp),    // cards
    large = RoundedCornerShape(24.dp),     // dialogs, sheets
    extraLarge = RoundedCornerShape(28.dp),
)
