package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.Question

/**
 * Visual centerpiece of the gameplay screen:
 * Displays category badge, difficulty tier indicator, and prominent question text
 * with sleek smooth entrance animation between questions.
 */
@Composable
fun QuestionCard(
  question: Question,
  questionNumber: Int,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val shape = RoundedCornerShape(26.dp)

  val categoryColor = when (question.category.lowercase()) {
    "science" -> Color(0xFF10B981) // Emerald
    "space" -> Color(0xFF8B5CF6) // Purple
    "history" -> Color(0xFFF59E0B) // Amber
    "technology" -> Color(0xFF06B6D4) // Cyan
    "geography" -> Color(0xFF3B82F6) // Blue
    "mathematics" -> Color(0xFFEC4899) // Pink
    "animals" -> Color(0xFF14B8A6) // Teal
    "food" -> Color(0xFFF97316) // Orange
    "sports" -> Color(0xFFEF4444) // Red
    "art" -> Color(0xFFA855F7) // Violet
    "language" -> Color(0xFFEAB308) // Yellow
    else -> Color(0xFF6366F1) // Indigo
  }

  val categoryIcon = when (question.category.lowercase()) {
    "science" -> "🔬"
    "space" -> "🚀"
    "history" -> "📜"
    "technology" -> "💻"
    "geography" -> "🌍"
    "mathematics" -> "📐"
    "animals" -> "🐾"
    "food" -> "🍳"
    "sports" -> "🏆"
    "art" -> "🎨"
    "language" -> "📖"
    else -> "💡"
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .shadow(elevation = 16.dp, shape = shape, spotColor = categoryColor.copy(alpha = 0.35f))
      .clip(shape)
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFF141E33),
            Color(0xFF0D1322)
          )
        )
      )
      .border(
        width = 1.5.dp,
        brush = Brush.verticalGradient(
          colors = listOf(
            categoryColor.copy(alpha = 0.65f),
            Color(0xFF334155).copy(alpha = 0.3f)
          )
        ),
        shape = shape
      )
      .padding(horizontal = 24.dp, vertical = 24.dp)
  ) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      // Category & Difficulty Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Category Badge
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(categoryColor.copy(alpha = 0.15f))
            .border(1.dp, categoryColor.copy(alpha = 0.45f), RoundedCornerShape(10.dp))
            .padding(horizontal = 12.dp, vertical = 5.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = categoryIcon,
              fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = question.category.uppercase(),
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 1.2.sp,
                fontSize = 11.sp
              ),
              color = categoryColor
            )
          }
        }

        // Difficulty score badge
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1E293B).copy(alpha = 0.8f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
          val tierName = when (question.difficultyTier.name) {
            "VERY_EASY" -> "EASY"
            "EASY" -> "EASY"
            "MEDIUM" -> "MEDIUM"
            "HARD" -> "HARD"
            "VERY_HARD" -> "EXPERT"
            "CHALLENGE" -> "CHALLENGE"
            else -> "TIER"
          }
          Text(
            text = tierName,
            style = MaterialTheme.typography.labelSmall.copy(
              fontWeight = FontWeight.Bold,
              letterSpacing = 1.sp,
              fontSize = 10.sp
            ),
            color = Color(0xFF94A3B8)
          )
        }
      }

      Spacer(modifier = Modifier.height(20.dp))

      // Animated Question Text Transition
      AnimatedContent(
        targetState = question.question,
        transitionSpec = {
          (fadeIn(tween(220)) + scaleIn(tween(220), initialScale = 0.96f))
            .togetherWith(fadeOut(tween(140)))
        },
        label = "question_text_anim"
      ) { qText ->
        Text(
          text = qText,
          style = MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = if (qText.length > 80) 22.sp else 25.sp,
            lineHeight = if (qText.length > 80) 32.sp else 36.sp
          ),
          color = Color.White,
          textAlign = TextAlign.Center,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("gameplay_question_text")
        )
      }

      Spacer(modifier = Modifier.height(10.dp))
    }
  }
}
