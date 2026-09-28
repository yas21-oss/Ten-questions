package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Polished game HUD featuring Stage badge, Question counter, Stage XP,
 * animated Hearts (attempts indicator), and a modern 10-Question Progress track.
 */
@Composable
fun GameHud(
  stageNumber: Int,
  currentQuestionIndex: Int, // 0..9
  stageXp: Int,
  attemptsLeft: Int, // 0..5
  lastAwardedXp: Int?,
  onExit: () -> Unit,
  modifier: Modifier = Modifier
) {
  val questionNumber = currentQuestionIndex + 1

  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = 16.dp, vertical = 8.dp)
  ) {
    // Top Row: Back button, Stage Pill, Question X/10, XP Badge
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // Exit button
      IconButton(
        onClick = onExit,
        modifier = Modifier
          .size(42.dp)
          .clip(CircleShape)
          .background(Color(0xFF1E293B).copy(alpha = 0.7f))
          .border(1.dp, Color(0xFF334155), CircleShape)
          .testTag("game_back_button")
      ) {
        Icon(
          imageVector = Icons.AutoMirrored.Filled.ArrowBack,
          contentDescription = "Exit to Menu",
          tint = Color(0xFF94A3B8),
          modifier = Modifier.size(20.dp)
        )
      }

      // Stage Badge
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(
            Brush.horizontalGradient(
              listOf(Color(0xFF4F46E5).copy(alpha = 0.25f), Color(0xFF06B6D4).copy(alpha = 0.2f))
            )
          )
          .border(
            1.dp,
            Brush.horizontalGradient(listOf(Color(0xFF6366F1), Color(0xFF06B6D4))),
            RoundedCornerShape(12.dp)
          )
          .padding(horizontal = 14.dp, vertical = 6.dp)
      ) {
        Text(
          text = "STAGE $stageNumber",
          style = MaterialTheme.typography.labelSmall.copy(
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp,
            fontSize = 12.sp
          ),
          color = Color(0xFF38BDF8)
        )
      }

      // Question X / 10
      Box(
        modifier = Modifier
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFF0F172A))
          .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp))
          .padding(horizontal = 12.dp, vertical = 6.dp)
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "$questionNumber",
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Black,
              fontSize = 16.sp
            ),
            color = Color.White
          )
          Text(
            text = " / 10",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFF64748B)
          )
        }
      }

      // XP Badge with popup
      Box(contentAlignment = Alignment.Center) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF172554).copy(alpha = 0.6f))
            .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = "⚡",
              fontSize = 13.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "$stageXp",
              style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Black,
                fontSize = 15.sp
              ),
              color = Color(0xFFFBBF24)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = "XP",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp
              ),
              color = Color(0xFF94A3B8)
            )
          }
        }

        // Floating XP animation
        TQXpPopupAnimation(addedXp = lastAwardedXp)
      }
    }

    Spacer(modifier = Modifier.height(12.dp))

    // Second Row: 10-Question Progress Bar & Animated Hearts Indicator
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      // 10-question progress track
      QuestionProgressTrack(
        currentIndex = currentQuestionIndex,
        modifier = Modifier.weight(1f)
      )

      Spacer(modifier = Modifier.width(16.dp))

      // 5 Animated Hearts
      AnimatedHeartsIndicator(
        attemptsLeft = attemptsLeft,
        maxAttempts = 5
      )
    }
  }
}

/**
 * 10-Question Progress Track with glowing active dot and completed markers.
 */
@Composable
fun QuestionProgressTrack(
  currentIndex: Int, // 0..9
  modifier: Modifier = Modifier
) {
  val infiniteTransition = rememberInfiniteTransition(label = "hud_pulse")
  val pulseGlow by infiniteTransition.animateFloat(
    initialValue = 0.7f,
    targetValue = 1.0f,
    animationSpec = infiniteRepeatable(
      animation = tween(800, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "progress_pulse"
  )

  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    for (i in 0 until 10) {
      val isAnswered = i < currentIndex
      val isCurrent = i == currentIndex

      val barColor = when {
        isAnswered -> Color(0xFF10B981) // Emerald completed
        isCurrent -> Color(0xFF38BDF8) // Glowing cyan
        else -> Color(0xFF1E293B) // Unanswered slot
      }

      Box(
        modifier = Modifier
          .weight(1f)
          .height(if (isCurrent) 8.dp else 5.dp)
          .clip(RoundedCornerShape(4.dp))
          .background(barColor.copy(alpha = if (isCurrent) pulseGlow else 1f))
          .then(
            if (isCurrent) {
              Modifier.border(1.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(4.dp))
            } else Modifier
          )
      )
    }
  }
}

/**
 * Animated Hearts Indicator: displays exactly 5 hearts and provides dramatic visual
 * transition from alive (❤️) to broken (💔) when attempts are lost.
 */
@Composable
fun AnimatedHeartsIndicator(
  attemptsLeft: Int,
  maxAttempts: Int = 5,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    for (heartIndex in 1..maxAttempts) {
      val isAlive = heartIndex <= attemptsLeft
      AnimatedHeart(
        isAlive = isAlive,
        index = heartIndex
      )
    }
  }
}

@Composable
private fun AnimatedHeart(
  isAlive: Boolean,
  index: Int
) {
  val scale = remember { Animatable(1f) }

  LaunchedEffect(isAlive) {
    if (!isAlive) {
      // Pop scale animation on lost life
      scale.animateTo(1.4f, tween(110, easing = FastOutSlowInEasing))
      scale.animateTo(0.85f, tween(150, easing = FastOutSlowInEasing))
      scale.animateTo(1.0f, tween(90))
    } else {
      scale.snapTo(1f)
    }
  }

  Box(
    modifier = Modifier
      .scale(scale.value)
      .size(22.dp)
      .testTag("game_heart_$index"),
    contentAlignment = Alignment.Center
  ) {
    if (isAlive) {
      // Full active heart ❤️
      Icon(
        imageVector = Icons.Default.Favorite,
        contentDescription = "Attempt $index (Active)",
        tint = Color(0xFFEF4444),
        modifier = Modifier
          .size(20.dp)
          .shadow(elevation = 6.dp, shape = CircleShape, spotColor = Color(0xFFEF4444))
      )
    } else {
      // Broken heart 💔
      Box(contentAlignment = Alignment.Center) {
        Icon(
          imageVector = Icons.Outlined.FavoriteBorder,
          contentDescription = "Attempt $index (Broken)",
          tint = Color(0xFF64748B),
          modifier = Modifier.size(18.dp)
        )
        androidx.compose.foundation.Canvas(modifier = Modifier.size(14.dp)) {
          drawLine(
            color = Color(0xFFEF4444).copy(alpha = 0.85f),
            start = androidx.compose.ui.geometry.Offset(x = size.width * 0.2f, y = size.height * 0.2f),
            end = androidx.compose.ui.geometry.Offset(x = size.width * 0.8f, y = size.height * 0.8f),
            strokeWidth = 2.dp.toPx(),
            cap = androidx.compose.ui.graphics.StrokeCap.Round
          )
        }
      }
    }
  }
}
