package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.example.data.model.UserAccount

// --- 1. PRIMARY BUTTON ---
@Composable
fun TQPrimaryButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  testTag: String = "tq_primary_button"
) {
  val shape = RoundedCornerShape(18.dp)
  val brush = if (enabled) {
    Brush.horizontalGradient(
      colors = listOf(Color(0xFF6366F1), Color(0xFF4F46E5), Color(0xFF4338CA))
    )
  } else {
    Brush.horizontalGradient(
      colors = listOf(Color(0xFF334155), Color(0xFF1E293B))
    )
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(60.dp)
      .shadow(elevation = if (enabled) 8.dp else 0.dp, shape = shape, spotColor = Color(0xFF6366F1))
      .clip(shape)
      .background(brush)
      .clickable(enabled = enabled, onClick = onClick)
      .testTag(testTag),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text.uppercase(),
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Black,
        letterSpacing = 2.sp,
        fontSize = 18.sp
      ),
      color = if (enabled) Color.White else Color(0xFF64748B)
    )
  }
}

// --- 2. ANSWER INPUT ---
@Composable
fun TQAnswerInput(
  value: String,
  onValueChange: (String) -> Unit,
  onDone: () -> Unit,
  placeholder: String,
  modifier: Modifier = Modifier,
  testTag: String = "tq_answer_input"
) {
  val shape = RoundedCornerShape(20.dp)
  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(68.dp)
      .clip(shape)
      .background(Color(0xFF141A2D))
      .border(
        width = 1.5.dp,
        brush = Brush.horizontalGradient(
          colors = listOf(Color(0xFF6366F1).copy(alpha = 0.6f), Color(0xFF38BDF8).copy(alpha = 0.4f))
        ),
        shape = shape
      )
      .padding(horizontal = 20.dp),
    contentAlignment = Alignment.CenterStart
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(modifier = Modifier.weight(1f)) {
        if (value.isEmpty()) {
          Text(
            text = placeholder,
            style = MaterialTheme.typography.titleMedium.copy(
              fontWeight = FontWeight.Medium,
              fontSize = 18.sp
            ),
            color = Color(0xFF64748B)
          )
        }
        BasicTextField(
          value = value,
          onValueChange = onValueChange,
          singleLine = true,
          textStyle = TextStyle(
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
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
      if (value.isNotEmpty()) {
        IconButton(
          onClick = { onValueChange("") },
          modifier = Modifier.size(32.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Clear,
            contentDescription = "Clear",
            tint = Color(0xFF94A3B8)
          )
        }
      }
    }
  }
}

// --- 3. XP DISPLAY & POPUP ANIMATION ---
@Composable
fun TQXpDisplay(
  currentXp: Int,
  modifier: Modifier = Modifier,
  label: String = "XP"
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(12.dp))
      .background(Color(0xFF1A2238))
      .border(1.dp, Color(0xFFF59E0B).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
      .padding(horizontal = 12.dp, vertical = 6.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(
        text = "⚡ $currentXp",
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        color = Color(0xFFFBBF24)
      )
      Spacer(modifier = Modifier.width(4.dp))
      Text(
        text = label,
        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
        color = Color(0xFF94A3B8)
      )
    }
  }
}

@Composable
fun TQXpPopupAnimation(
  addedXp: Int?,
  modifier: Modifier = Modifier
) {
  val animY = remember { Animatable(0f) }
  val alpha = remember { Animatable(1f) }

  LaunchedEffect(addedXp) {
    if (addedXp != null && addedXp > 0) {
      animY.snapTo(0f)
      alpha.snapTo(1f)
      animY.animateTo(
        targetValue = -70f,
        animationSpec = tween(durationMillis = 850, easing = FastOutSlowInEasing)
      )
      alpha.animateTo(
        targetValue = 0f,
        animationSpec = tween(durationMillis = 250)
      )
    }
  }

  if (addedXp != null && addedXp > 0 && alpha.value > 0.05f) {
    Box(
      modifier = modifier
        .offset { IntOffset(0, animY.value.toInt()) }
        .clip(RoundedCornerShape(14.dp))
        .background(Color(0xFF10B981).copy(alpha = 0.25f * alpha.value))
        .border(1.dp, Color(0xFF10B981).copy(alpha = alpha.value), RoundedCornerShape(14.dp))
        .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
      Text(
        text = "+$addedXp XP",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.Black,
          fontSize = 18.sp
        ),
        color = Color(0xFF34D399).copy(alpha = alpha.value)
      )
    }
  }
}

// --- 4. STAR DISPLAY ---
@Composable
fun TQStarRating(
  stars: Int, // 0..3
  modifier: Modifier = Modifier,
  starSize: Int = 28
) {
  Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    for (i in 1..3) {
      val isFilled = i <= stars
      Icon(
        imageVector = if (isFilled) Icons.Default.Star else Icons.Outlined.StarBorder,
        contentDescription = "Star $i",
        tint = if (isFilled) Color(0xFFFBBF24) else Color(0xFF475569),
        modifier = Modifier.size(starSize.dp)
      )
    }
  }
}

// --- 5. STAGE PROGRESS (10 Question Dots/Pills) ---
@Composable
fun TQStageProgress(
  currentIndex: Int, // 0..9
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(6.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    for (i in 0 until 10) {
      val isAnswered = i < currentIndex
      val isCurrent = i == currentIndex
      Box(
        modifier = Modifier
          .weight(1f)
          .height(6.dp)
          .clip(RoundedCornerShape(3.dp))
          .background(
            when {
              isCurrent -> Color(0xFF38BDF8)
              isAnswered -> Color(0xFF10B981)
              else -> Color(0xFF1E293B)
            }
          )
      )
    }
  }
}

// --- 6. QUESTION HEADER ---
@Composable
fun TQQuestionHeader(
  stageNumber: Int,
  questionNumber: Int, // 1..10
  attemptsLeft: Int,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .fillMaxWidth()
      .padding(top = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    IconButton(
      onClick = onBack,
      modifier = Modifier.testTag("tq_back_button")
    ) {
      Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = "Back",
        tint = Color(0xFF94A3B8)
      )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = "TEN QUESTIONS",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Black,
          letterSpacing = 2.5.sp
        ),
        color = Color(0xFF6366F1)
      )
      Text(
        text = "STAGE $stageNumber",
        style = MaterialTheme.typography.titleMedium.copy(
          fontWeight = FontWeight.ExtraBold,
          letterSpacing = 1.sp
        ),
        color = Color.White
      )
      Text(
        text = "Question $questionNumber / 10",
        style = MaterialTheme.typography.labelMedium,
        color = Color(0xFF94A3B8)
      )
    }

    HeartsIndicator(attemptsLeft = attemptsLeft)
  }
}

// --- 7. RESULT CARD ---
@Composable
fun TQResultCard(
  score: String,
  xpText: String,
  stars: Int,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(24.dp))
      .background(Color(0xFF13192B))
      .border(1.dp, Color(0xFF232D48), RoundedCornerShape(24.dp))
      .padding(28.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Text(
        text = score,
        style = MaterialTheme.typography.headlineLarge.copy(
          fontWeight = FontWeight.Black,
          fontSize = 40.sp
        ),
        color = Color.White
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = xpText,
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Bold,
          fontSize = 24.sp
        ),
        color = Color(0xFFFBBF24)
      )
      Spacer(modifier = Modifier.height(16.dp))
      TQStarRating(stars = stars, starSize = 36)
    }
  }
}

// --- 8. RANKING ROW ---
@Composable
fun TQRankingRow(
  rank: Int,
  playerName: String,
  stars: Int,
  isCurrentUser: Boolean = false,
  modifier: Modifier = Modifier
) {
  val rankColor = when (rank) {
    1 -> Color(0xFFFBBF24) // Gold
    2 -> Color(0xFFE2E8F0) // Silver
    3 -> Color(0xFFCD7F32) // Bronze
    else -> Color(0xFF64748B)
  }

  Row(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(if (isCurrentUser) Color(0xFF1E2846) else Color(0xFF13192B))
      .border(
        width = 1.dp,
        color = if (isCurrentUser) Color(0xFF6366F1) else Color(0xFF1E293B),
        shape = RoundedCornerShape(16.dp)
      )
      .padding(horizontal = 20.dp, vertical = 16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    // Rank Number
    Text(
      text = "$rank",
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Black),
      color = rankColor,
      modifier = Modifier.width(36.dp)
    )

    // Player Name
    Text(
      text = playerName,
      style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
      color = if (isCurrentUser) Color(0xFF38BDF8) else Color.White,
      modifier = Modifier.weight(1f)
    )

    // Stars Count
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = Icons.Default.Star,
        contentDescription = "Stars",
        tint = Color(0xFFFBBF24),
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = "$stars",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = Color.White
      )
    }
  }
}

// --- 9. PROFILE HEADER ---
@Composable
fun TQProfileHeader(
  user: UserAccount?,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(60.dp)
        .clip(CircleShape)
        .background(
          Brush.linearGradient(
            colors = listOf(Color(0xFF6366F1), Color(0xFF38BDF8))
          )
        ),
      contentAlignment = Alignment.Center
    ) {
      Text(
        text = (user?.displayName?.take(1) ?: "P").uppercase(),
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
        color = Color.White
      )
    }

    Spacer(modifier = Modifier.width(16.dp))

    Column {
      Text(
        text = user?.displayName ?: "Player",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = Color.White
      )
      Text(
        text = "Stage ${user?.unlockedStage ?: 1} Master",
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF94A3B8)
      )
    }
  }
}
