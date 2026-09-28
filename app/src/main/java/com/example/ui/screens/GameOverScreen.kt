package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.Localization
import com.example.data.model.AppLanguage
import com.example.data.model.Question
import com.example.ui.audio.GameHaptics
import com.example.ui.audio.GameSoundEngine
import com.example.ui.audio.SoundEffect
import com.example.ui.components.TQPrimaryButton

/**
 * Redesigned Stage Failure Screen.
 * Provides an encouraging, non-punishing feedback loop:
 * - Shows failed question with revealed verified answer and explanation
 * - Smooth retry flow with sound/haptic feedback
 */
@Composable
fun GameOverScreen(
  stageNumber: Int,
  failedQuestion: Question,
  correctAnswers: List<String>,
  explanation: String,
  language: AppLanguage,
  onRetryStage: () -> Unit,
  modifier: Modifier = Modifier,
  isSoundEnabled: Boolean = true,
  isHapticEnabled: Boolean = true
) {
  val context = LocalContext.current
  val soundEngine = remember { GameSoundEngine(context) }
  val haptics = remember { GameHaptics(context) }

  LaunchedEffect(Unit) {
    soundEngine.play(SoundEffect.STAGE_FAILED, isSoundEnabled)
  }

  DisposableEffect(Unit) {
    onDispose {
      soundEngine.release()
    }
  }

  Surface(
    modifier = modifier.fillMaxSize(),
    color = Color(0xFF070A13)
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.radialGradient(
            colors = listOf(Color(0xFF2D1219), Color(0xFF070A13)),
            radius = 1100f
          )
        )
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 24.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Spacer(modifier = Modifier.height(8.dp))

        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Status Pill
          Box(
            modifier = Modifier
              .clip(RoundedCornerShape(12.dp))
              .background(Color(0xFFEF4444).copy(alpha = 0.15f))
              .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
              .padding(horizontal = 16.dp, vertical = 6.dp)
          ) {
            Text(
              text = "ATTEMPTS EXHAUSTED",
              style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
              ),
              color = Color(0xFFEF4444)
            )
          }

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "STAGE FAILED",
            style = MaterialTheme.typography.displaySmall.copy(
              fontWeight = FontWeight.Black,
              letterSpacing = 2.sp,
              fontSize = 32.sp
            ),
            color = Color(0xFFFCA5A5),
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(8.dp))

          Text(
            text = "Keep going! Review the answer and master Stage $stageNumber.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF94A3B8),
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(28.dp))

          // Reveal Correct Answer & Explanation Card
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(24.dp))
              .background(Color(0xFF0F172A))
              .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(24.dp))
              .padding(22.dp)
          ) {
            Column(horizontalAlignment = Alignment.Start) {
              // The question
              Text(
                text = failedQuestion.question,
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Bold,
                  fontSize = 18.sp,
                  lineHeight = 26.sp
                ),
                color = Color.White
              )

              Spacer(modifier = Modifier.height(18.dp))

              // Revealed correct answer box
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .clip(RoundedCornerShape(16.dp))
                  .background(Color(0xFF10B981).copy(alpha = 0.12f))
                  .border(1.dp, Color(0xFF10B981).copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                  .padding(horizontal = 16.dp, vertical = 14.dp)
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(24.dp)
                  )
                  Spacer(modifier = Modifier.width(12.dp))
                  Column {
                    Text(
                      text = "CORRECT ANSWER",
                      style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        fontSize = 11.sp
                      ),
                      color = Color(0xFF10B981)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                      text = correctAnswers.firstOrNull() ?: "",
                      style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 20.sp
                      ),
                      color = Color.White
                    )
                  }
                }
              }

              if (explanation.isNotBlank()) {
                Spacer(modifier = Modifier.height(14.dp))
                Row(verticalAlignment = Alignment.Top) {
                  Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF64748B),
                    modifier = Modifier
                      .size(16.dp)
                      .padding(top = 2.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = explanation,
                    style = MaterialTheme.typography.bodyMedium.copy(
                      fontSize = 13.sp,
                      lineHeight = 18.sp
                    ),
                    color = Color(0xFF94A3B8)
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Action Button: RETRY STAGE
        TQPrimaryButton(
          text = Localization.get("retry_stage", language).ifEmpty { "RETRY STAGE" },
          onClick = {
            soundEngine.play(SoundEffect.BUTTON_CLICK, isSoundEnabled)
            haptics.vibrateButton(isHapticEnabled)
            onRetryStage()
          },
          testTag = "stage_failed_retry_button"
        )
      }
    }
  }
}
