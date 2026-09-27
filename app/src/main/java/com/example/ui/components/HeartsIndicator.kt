package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun HeartsIndicator(
  attemptsLeft: Int,
  maxAttempts: Int = 3,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    for (i in 1..maxAttempts) {
      val isAvailable = i <= attemptsLeft
      Icon(
        imageVector = if (isAvailable) Icons.Default.Favorite else Icons.Outlined.FavoriteBorder,
        contentDescription = "Attempt $i",
        tint = if (isAvailable) Color(0xFFEF4444) else Color(0xFF64748B),
        modifier = Modifier.size(24.dp)
      )
    }
  }
}
