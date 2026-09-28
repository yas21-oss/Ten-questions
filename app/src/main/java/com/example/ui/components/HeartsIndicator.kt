package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun HeartsIndicator(
  attemptsLeft: Int,
  maxAttempts: Int = 5,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    for (i in 1..maxAttempts) {
      val isAvailable = i <= attemptsLeft
      Box(
        modifier = Modifier
          .size(24.dp)
          .testTag("hearts_indicator_$i"),
        contentAlignment = Alignment.Center
      ) {
        if (isAvailable) {
          Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Heart $i Available",
            tint = Color(0xFFEF4444),
            modifier = Modifier.size(22.dp)
          )
        } else {
          // Broken heart 💔
          Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = "Heart $i Lost",
            tint = Color(0xFF64748B),
            modifier = Modifier.size(20.dp)
          )
          Canvas(modifier = Modifier.size(16.dp)) {
            drawLine(
              color = Color(0xFFEF4444).copy(alpha = 0.75f),
              start = Offset(x = size.width * 0.2f, y = size.height * 0.2f),
              end = Offset(x = size.width * 0.8f, y = size.height * 0.8f),
              strokeWidth = 2.dp.toPx(),
              cap = StrokeCap.Round
            )
          }
        }
      }
    }
  }
}
