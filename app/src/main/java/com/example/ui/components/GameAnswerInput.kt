package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Premium gaming answer input console.
 * Features dynamic glow states, microphone voice input launcher, clear action,
 * and high-impact shake animation on incorrect attempts.
 */
@Composable
fun GameAnswerInput(
  value: String,
  onValueChange: (String) -> Unit,
  onDone: () -> Unit,
  onMicClick: () -> Unit,
  placeholder: String,
  isListening: Boolean = false,
  isErrorShake: Boolean = false,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  testTag: String = "tq_game_answer_field"
) {
  val shape = RoundedCornerShape(22.dp)
  val shakeOffset = remember { Animatable(0f) }

  // Trigger brief dramatic shake on error
  LaunchedEffect(isErrorShake) {
    if (isErrorShake) {
      shakeOffset.animateTo(
        targetValue = 0f,
        animationSpec = keyframes {
          durationMillis = 350
          0f at 0
          -14f at 50
          14f at 100
          -10f at 150
          10f at 200
          -5f at 250
          5f at 300
          0f at 350
        }
      )
    }
  }

  val borderBrush = when {
    isErrorShake -> Brush.horizontalGradient(listOf(Color(0xFFEF4444), Color(0xFFDC2626)))
    isListening -> Brush.horizontalGradient(listOf(Color(0xFFEF4444), Color(0xFFF97316)))
    value.isNotBlank() -> Brush.horizontalGradient(listOf(Color(0xFF38BDF8), Color(0xFF6366F1)))
    else -> Brush.horizontalGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
  }

  val glowColor = when {
    isErrorShake -> Color(0xFFEF4444)
    isListening -> Color(0xFFF97316)
    value.isNotBlank() -> Color(0xFF38BDF8)
    else -> Color(0xFF1E293B)
  }

  Box(
    modifier = modifier
      .offset { IntOffset(shakeOffset.value.toInt(), 0) }
      .fillMaxWidth()
      .height(68.dp)
      .shadow(elevation = 10.dp, shape = shape, spotColor = glowColor.copy(alpha = 0.4f))
      .clip(shape)
      .background(Color(0xFF0F172A))
      .border(width = 1.5.dp, brush = borderBrush, shape = shape)
      .padding(horizontal = 16.dp),
    contentAlignment = Alignment.CenterStart
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Microphone button for voice input
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(CircleShape)
          .background(if (isListening) Color(0xFFEF4444).copy(alpha = 0.25f) else Color(0xFF1E293B))
          .border(
            1.dp,
            if (isListening) Color(0xFFEF4444) else Color(0xFF334155),
            CircleShape
          )
          .clickable(enabled = enabled, onClick = onMicClick)
          .testTag("game_mic_button"),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = Icons.Default.Mic,
          contentDescription = "Voice Input",
          tint = if (isListening) Color(0xFFEF4444) else Color(0xFF38BDF8),
          modifier = Modifier.size(20.dp)
        )
      }

      Spacer(modifier = Modifier.width(12.dp))

      // Center: Text input or Placeholder
      Box(
        modifier = Modifier.weight(1f),
        contentAlignment = Alignment.CenterStart
      ) {
        if (value.isEmpty()) {
          Text(
            text = if (isListening) "Listening..." else placeholder,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Medium,
              fontSize = 17.sp
            ),
            color = if (isListening) Color(0xFFEF4444) else Color(0xFF64748B)
          )
        }

        BasicTextField(
          value = value,
          onValueChange = onValueChange,
          singleLine = true,
          enabled = enabled,
          textStyle = TextStyle(
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
          ),
          cursorBrush = SolidColor(Color(0xFF38BDF8)),
          keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done
          ),
          keyboardActions = KeyboardActions(onDone = { onDone() }),
          modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
        )
      }

      // Clear button when text present
      if (value.isNotEmpty()) {
        IconButton(
          onClick = { onValueChange("") },
          modifier = Modifier
            .size(34.dp)
            .testTag("game_input_clear_button")
        ) {
          Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Clear input",
            tint = Color(0xFF94A3B8),
            modifier = Modifier.size(18.dp)
          )
        }
      }
    }
  }
}
