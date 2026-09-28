package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
import com.example.ui.audio.GameHaptics
import com.example.ui.audio.GameSoundEngine
import com.example.ui.audio.SoundEffect
import com.example.ui.components.ConfettiEffect
import com.example.ui.components.StarRewardDisplay
import com.example.ui.components.TQPrimaryButton

/**
 * Redesigned Stage Complete Screen.
 * Prominently showcases sequential star rewards, celebratory confetti,
 * stage XP summary, and modern action controls.
 */
@Composable
fun WinScreen(
  stageNumber: Int,
  stageTotalXp: Int, // 0..1000 XP
  starsEarned: Int, // 0..3 stars
  language: AppLanguage,
  onNextStage: () -> Unit,
  onReplay: () -> Unit,
  modifier: Modifier = Modifier,
  isSoundEnabled: Boolean = true,
  isHapticEnabled: Boolean = true
) {
  val context = LocalContext.current
  val soundEngine = remember { GameSoundEngine(context) }
  val haptics = remember { GameHaptics(context) }

  var showButtons by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    soundEngine.play(SoundEffect.STAGE_COMPLETE, isSoundEnabled)
    kotlinx.coroutines.delay(1200)
    showButtons = true
  }

  DisposableEffect(Unit) {
    onDispose {
      soundEngine.release()
    }
  }

  Box(modifier = modifier.fillMaxSize()) {
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = Color(0xFF070A13)
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.radialGradient(
              colors = listOf(Color(0xFF1E1B4B), Color(0xFF070A13)),
              radius = 1100f
            )
          )
      ) {
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 28.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          Spacer(modifier = Modifier.height(10.dp))

          // Center Card Content
          Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            // Stage Pill
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF38BDF8).copy(alpha = 0.15f))
                .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
              Text(
                text = "STAGE $stageNumber",
                style = MaterialTheme.typography.labelSmall.copy(
                  fontWeight = FontWeight.Black,
                  letterSpacing = 2.sp
                ),
                color = Color(0xFF38BDF8)
              )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
              text = "STAGE COMPLETE",
              style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                fontSize = 32.sp
              ),
              color = Color.White,
              textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Sequential Star Reveal Animation
            StarRewardDisplay(
              earnedStars = starsEarned,
              onStarRevealed = { starIndex ->
                soundEngine.play(SoundEffect.STAR_EARNED, isSoundEnabled)
                haptics.vibrateStar(isHapticEnabled)
              }
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Score & XP Summary Card
            Box(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(22.dp))
                .background(Color(0xFF0F172A))
                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(22.dp))
                .padding(vertical = 20.dp, horizontal = 24.dp)
            ) {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
              ) {
                // Questions Solved
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = "ACCURACY",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      letterSpacing = 1.sp
                    ),
                    color = Color(0xFF64748B)
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = "10 / 10",
                    style = MaterialTheme.typography.headlineMedium.copy(
                      fontWeight = FontWeight.Black,
                      fontSize = 26.sp
                    ),
                    color = Color(0xFF38BDF8)
                  )
                }

                Box(
                  modifier = Modifier
                    .width(1.dp)
                    .height(40.dp)
                    .background(Color(0xFF334155))
                )

                // Total Stage XP
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                  Text(
                    text = "TOTAL XP",
                    style = MaterialTheme.typography.labelSmall.copy(
                      fontWeight = FontWeight.Bold,
                      letterSpacing = 1.sp
                    ),
                    color = Color(0xFF64748B)
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  Text(
                    text = "$stageTotalXp XP",
                    style = MaterialTheme.typography.headlineMedium.copy(
                      fontWeight = FontWeight.Black,
                      fontSize = 26.sp
                    ),
                    color = Color(0xFFFBBF24)
                  )
                }
              }
            }
          }

          // Action Buttons: NEXT STAGE & REPLAY
          AnimatedVisibility(
            visible = showButtons,
            enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 2 }
          ) {
            Column(
              modifier = Modifier.fillMaxWidth(),
              horizontalAlignment = Alignment.CenterHorizontally,
              verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
              TQPrimaryButton(
                text = Localization.get("next_stage", language).ifEmpty { "NEXT STAGE" },
                onClick = {
                  soundEngine.play(SoundEffect.BUTTON_CLICK, isSoundEnabled)
                  haptics.vibrateButton(isHapticEnabled)
                  onNextStage()
                },
                testTag = "stage_complete_next_button"
              )

              // Replay Button
              Box(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(54.dp)
                  .clip(RoundedCornerShape(18.dp))
                  .background(Color(0xFF1E293B).copy(alpha = 0.8f))
                  .border(1.dp, Color(0xFF334155), RoundedCornerShape(18.dp))
                  .clickable {
                    soundEngine.play(SoundEffect.BUTTON_CLICK, isSoundEnabled)
                    haptics.vibrateButton(isHapticEnabled)
                    onReplay()
                  }
                  .testTag("stage_complete_replay_button"),
                contentAlignment = Alignment.Center
              ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                  Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(18.dp)
                  )
                  Spacer(modifier = Modifier.width(8.dp))
                  Text(
                    text = Localization.get("replay", language).ifEmpty { "REPLAY STAGE" },
                    style = MaterialTheme.typography.titleMedium.copy(
                      fontWeight = FontWeight.Bold,
                      fontSize = 15.sp,
                      letterSpacing = 1.sp
                    ),
                    color = Color(0xFF94A3B8)
                  )
                }
              }
            }
          }
        }
      }
    }

    // Celebratory Confetti Effect
    ConfettiEffect()
  }
}
