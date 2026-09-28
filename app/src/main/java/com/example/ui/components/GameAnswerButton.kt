package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * High-impact game ANSWER button.
 * Features tactile press animation, glowing gradient styling, and disabled/submitting states.
 */
@Composable
fun GameAnswerButton(
  text: String,
  onClick: () -> Unit,
  enabled: Boolean,
  isSubmitting: Boolean = false,
  modifier: Modifier = Modifier,
  testTag: String = "tq_game_answer_button"
) {
  val shape = RoundedCornerShape(20.dp)
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(
    targetValue = if (isPressed && enabled) 0.96f else 1f,
    label = "btn_scale"
  )

  val bgBrush = when {
    !enabled || isSubmitting -> Brush.horizontalGradient(
      listOf(Color(0xFF1E293B), Color(0xFF1E293B))
    )

    else -> Brush.horizontalGradient(
      listOf(
        Color(0xFF6366F1), // Indigo
        Color(0xFF4F46E5),
        Color(0xFF06B6D4)  // Cyan
      )
    )
  }

  val glowColor = if (enabled && !isSubmitting) Color(0xFF6366F1) else Color.Transparent

  Box(
    modifier = modifier
      .scale(scale)
      .fillMaxWidth()
      .height(60.dp)
      .shadow(
        elevation = if (enabled && !isSubmitting) 10.dp else 0.dp,
        shape = shape,
        spotColor = glowColor
      )
      .clip(shape)
      .background(bgBrush)
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        enabled = enabled && !isSubmitting,
        onClick = onClick
      )
      .testTag(testTag),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = if (isSubmitting) "CHECKING..." else text.uppercase(),
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Black,
        letterSpacing = 2.sp,
        fontSize = 17.sp
      ),
      color = if (enabled && !isSubmitting) Color.White else Color(0xFF64748B)
    )
  }
}
