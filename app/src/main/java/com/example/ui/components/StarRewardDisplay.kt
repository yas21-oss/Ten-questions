package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Sequential star reveal reward animation for Stage Complete.
 * Reveals earned stars one at a time with spring pop, glowing illumination,
 * and calls onStarRevealed for synchronized audio/haptic cues.
 */
@Composable
fun StarRewardDisplay(
  earnedStars: Int, // 0..3
  modifier: Modifier = Modifier,
  onStarRevealed: (Int) -> Unit = {}
) {
  val star1Scale = remember { Animatable(0f) }
  val star2Scale = remember { Animatable(0f) }
  val star3Scale = remember { Animatable(0f) }

  LaunchedEffect(earnedStars) {
    delay(400) // Brief anticipation pause
    if (earnedStars >= 1) {
      onStarRevealed(1)
      star1Scale.animateTo(1.35f, tween(160, easing = FastOutSlowInEasing))
      star1Scale.animateTo(1.0f, tween(120, easing = FastOutSlowInEasing))
    }
    if (earnedStars >= 2) {
      delay(280)
      onStarRevealed(2)
      star2Scale.animateTo(1.35f, tween(160, easing = FastOutSlowInEasing))
      star2Scale.animateTo(1.0f, tween(120, easing = FastOutSlowInEasing))
    }
    if (earnedStars >= 3) {
      delay(280)
      onStarRevealed(3)
      star3Scale.animateTo(1.4f, tween(180, easing = FastOutSlowInEasing))
      star3Scale.animateTo(1.0f, tween(120, easing = FastOutSlowInEasing))
    }
  }

  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Star 1
    StarSlot(
      isFilled = earnedStars >= 1,
      scale = if (earnedStars >= 1) star1Scale.value else 1f
    )

    // Star 2 (Center star is slightly larger)
    StarSlot(
      isFilled = earnedStars >= 2,
      scale = if (earnedStars >= 2) star2Scale.value else 1f,
      size = 56
    )

    // Star 3
    StarSlot(
      isFilled = earnedStars >= 3,
      scale = if (earnedStars >= 3) star3Scale.value else 1f
    )
  }
}

@Composable
private fun StarSlot(
  isFilled: Boolean,
  scale: Float,
  size: Int = 46
) {
  Box(
    modifier = Modifier.size((size + 14).dp),
    contentAlignment = Alignment.Center
  ) {
    // Outline placeholder
    Icon(
      imageVector = Icons.Outlined.StarBorder,
      contentDescription = null,
      tint = Color(0xFF334155),
      modifier = Modifier.size(size.dp)
    )

    if (isFilled && scale > 0.05f) {
      Icon(
        imageVector = Icons.Default.Star,
        contentDescription = "Golden Star",
        tint = Color(0xFFFBBF24),
        modifier = Modifier
          .scale(scale)
          .size(size.dp)
          .shadow(elevation = 14.dp, shape = CircleShape, spotColor = Color(0xFFF59E0B))
      )
    }
  }
}
