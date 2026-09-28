package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.Localization
import com.example.data.model.AppLanguage
import com.example.ui.audio.GameHaptics
import com.example.ui.audio.GameSoundEngine
import com.example.ui.audio.SoundEffect
import com.example.ui.components.GameAnswerButton
import com.example.ui.components.GameAnswerInput
import com.example.ui.components.GameHud
import com.example.ui.components.GameKeyboard
import com.example.ui.components.QuestionCard
import com.example.ui.components.VoiceListeningIndicator
import com.example.ui.components.rememberVoiceRecognizer
import com.example.ui.viewmodel.GameState

/**
 * Complete Gameplay UI Redesign.
 * Transforms the gameplay experience into an exciting, responsive, commercial mobile quiz game:
 * - Premium dark aesthetic with glowing accents
 * - Top HUD with stage, question progress, stage XP, and animated hearts
 * - Large animated QuestionCard as the visual focus
 * - Custom tactile in-game keyboard with adaptive numeric keypad and language layouts
 * - Voice speech-to-text input with pulse animation
 * - Immediate visual/audio/haptic feedback on every attempt
 * - Absolutely NO countdown timer or time limit.
 */
@Composable
fun GameScreen(
  state: GameState.Playing,
  language: AppLanguage,
  isSoundEnabled: Boolean = true,
  isHapticEnabled: Boolean = true,
  onInputChange: (String) -> Unit,
  onSubmit: () -> Unit,
  onExit: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val soundEngine = remember { GameSoundEngine(context) }
  val haptics = remember { GameHaptics(context) }

  var isListening by remember { mutableStateOf(false) }
  var useSystemKeyboard by remember { mutableStateOf(false) }

  // Sound and haptic effect triggers for question transitions and answers
  var previousQuestionIndex by remember { mutableStateOf(state.currentQuestionIndex) }
  var previousAttemptsLeft by remember { mutableStateOf(state.attemptsLeft) }

  LaunchedEffect(state.currentQuestionIndex) {
    if (state.currentQuestionIndex != previousQuestionIndex) {
      soundEngine.play(SoundEffect.QUESTION_TRANSITION, isSoundEnabled)
      if (state.lastAwardedXp != null && state.lastAwardedXp > 0) {
        soundEngine.play(SoundEffect.CORRECT_ANSWER, isSoundEnabled)
        haptics.vibrateCorrect(isHapticEnabled)
      }
      previousQuestionIndex = state.currentQuestionIndex
      previousAttemptsLeft = 3
    }
  }

  LaunchedEffect(state.attemptsLeft) {
    if (state.attemptsLeft < previousAttemptsLeft && state.attemptsLeft > 0) {
      soundEngine.play(SoundEffect.INCORRECT_ANSWER, isSoundEnabled)
      soundEngine.play(SoundEffect.HEART_LOST, isSoundEnabled)
      haptics.vibrateIncorrect(isHapticEnabled)
      previousAttemptsLeft = state.attemptsLeft
    }
  }

  val startVoiceRecognition = rememberVoiceRecognizer(
    language = language,
    onSpeechResult = { recognizedText ->
      onInputChange(recognizedText)
    },
    onListeningStateChanged = { listening ->
      isListening = listening
      if (listening) {
        soundEngine.play(SoundEffect.MIC_START, isSoundEnabled)
      } else {
        soundEngine.play(SoundEffect.MIC_END, isSoundEnabled)
      }
    }
  )

  DisposableEffect(Unit) {
    onDispose {
      soundEngine.release()
    }
  }

  Surface(
    modifier = modifier.fillMaxSize(),
    color = Color(0xFF070A13) // Deep dark space background
  ) {
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          Brush.radialGradient(
            colors = listOf(Color(0xFF131D38), Color(0xFF070A13)),
            radius = 1200f
          )
        )
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        // 1. TOP GAME HUD
        GameHud(
          stageNumber = state.stageNumber,
          currentQuestionIndex = state.currentQuestionIndex,
          stageXp = state.stageAccumulatedXp,
          attemptsLeft = state.attemptsLeft,
          lastAwardedXp = state.lastAwardedXp,
          onExit = onExit
        )

        // 2. CENTER: Large Visual Focus Question Card
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp, vertical = 8.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          QuestionCard(
            question = state.currentQuestion,
            questionNumber = state.currentQuestionIndex + 1,
            language = language
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Feedback Message on incorrect attempt
          AnimatedVisibility(
            visible = state.feedbackMessage != null,
            enter = fadeIn() + slideInVertically(),
            exit = fadeOut() + slideOutVertically()
          ) {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFEF4444).copy(alpha = 0.15f))
                .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Warning,
                  contentDescription = null,
                  tint = Color(0xFFEF4444),
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                  text = state.feedbackMessage ?: "",
                  style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                  color = Color(0xFFFCA5A5),
                  modifier = Modifier.testTag("game_error_text")
                )
              }
            }
          }

          // Voice Listening Indicator
          VoiceListeningIndicator(
            isListening = isListening,
            onCancel = { startVoiceRecognition() },
            modifier = Modifier.padding(top = 8.dp)
          )
        }

        // 3. BOTTOM AREA: Answer Input, Answer Button, and Custom Keyboard
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // Premium Gaming Answer Input Console
          GameAnswerInput(
            value = state.userInput,
            onValueChange = onInputChange,
            onDone = {
              soundEngine.play(SoundEffect.BUTTON_CLICK, isSoundEnabled)
              haptics.vibrateButton(isHapticEnabled)
              onSubmit()
            },
            onMicClick = {
              soundEngine.play(SoundEffect.BUTTON_CLICK, isSoundEnabled)
              haptics.vibrateButton(isHapticEnabled)
              startVoiceRecognition()
            },
            placeholder = Localization.get("type_your_answer", language),
            isListening = isListening,
            isErrorShake = state.isErrorShake,
            enabled = true,
            testTag = "tq_game_answer_field"
          )

          Spacer(modifier = Modifier.height(10.dp))

          // Primary ANSWER Button
          GameAnswerButton(
            text = Localization.get("answer", language).ifEmpty { "ANSWER" },
            onClick = {
              soundEngine.play(SoundEffect.BUTTON_CLICK, isSoundEnabled)
              haptics.vibrateButton(isHapticEnabled)
              onSubmit()
            },
            enabled = state.userInput.isNotBlank(),
            testTag = "tq_game_answer_button"
          )

          Spacer(modifier = Modifier.height(8.dp))

          // Custom In-Game Keyboard (when system keyboard is not active)
          if (!useSystemKeyboard) {
            GameKeyboard(
              question = state.currentQuestion,
              language = language,
              onKeyPressed = { char ->
                soundEngine.play(SoundEffect.KEY_PRESS, isSoundEnabled)
                haptics.vibrateKey(isHapticEnabled)
                onInputChange(state.userInput + char)
              },
              onBackspace = {
                soundEngine.play(SoundEffect.KEY_PRESS, isSoundEnabled)
                haptics.vibrateKey(isHapticEnabled)
                if (state.userInput.isNotEmpty()) {
                  onInputChange(state.userInput.dropLast(1))
                }
              },
              onSubmit = {
                soundEngine.play(SoundEffect.BUTTON_CLICK, isSoundEnabled)
                haptics.vibrateButton(isHapticEnabled)
                onSubmit()
              },
              onToggleSystemKeyboard = {
                useSystemKeyboard = true
              },
              canSubmit = state.userInput.isNotBlank()
            )
          } else {
            // Button to return to custom in-game keyboard
            Row(
              modifier = Modifier
                .padding(vertical = 4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF1E293B))
                .clickable { useSystemKeyboard = false }
                .padding(horizontal = 14.dp, vertical = 6.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "Switch to In-Game Keyboard",
                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF38BDF8)),
                fontWeight = FontWeight.SemiBold
              )
            }
          }
        }
      }
    }
  }
}
