package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.Localization
import com.example.data.model.AppLanguage
import com.example.ui.components.HeartsIndicator
import com.example.ui.components.TQAnswerInput
import com.example.ui.components.TQPrimaryButton
import com.example.ui.components.TQStageProgress
import com.example.ui.components.TQXpDisplay
import com.example.ui.components.TQXpPopupAnimation
import com.example.ui.viewmodel.GameState

@Composable
fun GameScreen(
  state: GameState.Playing,
  language: AppLanguage,
  onInputChange: (String) -> Unit,
  onSubmit: () -> Unit,
  onExit: () -> Unit,
  modifier: Modifier = Modifier
) {
  val questionNumber = state.currentQuestionIndex + 1
  val question = state.currentQuestion

  Surface(
    modifier = modifier.fillMaxSize(),
    color = Color(0xFF090D18)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp, vertical = 16.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Top Area: App title, Stage, Question counter, Hearts attempts, Stage XP
      Column(modifier = Modifier.fillMaxWidth()) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          IconButton(
            onClick = onExit,
            modifier = Modifier.testTag("game_back_button")
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
              text = "Stage ${state.stageNumber}",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                fontSize = 18.sp
              ),
              color = Color.White
            )
            Text(
              text = "Question $questionNumber / 10",
              style = MaterialTheme.typography.labelMedium,
              color = Color(0xFF38BDF8)
            )
          }

          // Remaining attempts (3 small hearts)
          HeartsIndicator(attemptsLeft = state.attemptsLeft)
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Clean Stage Progress indicator (10 items)
        TQStageProgress(currentIndex = state.currentQuestionIndex)

        Spacer(modifier = Modifier.height(12.dp))

        // XP counter and floating popup animation
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.End,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(contentAlignment = Alignment.Center) {
            TQXpDisplay(currentXp = state.stageAccumulatedXp, label = "/ 1000 XP")
            TQXpPopupAnimation(addedXp = state.lastAwardedXp)
          }
        }
      }

      // Center: The Question in large bold typography (The Visual Focus)
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Text(
          text = question.question,
          style = MaterialTheme.typography.headlineMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = 38.sp
          ),
          color = Color.White,
          textAlign = TextAlign.Center,
          modifier = Modifier.testTag("gameplay_question_text")
        )
      }

      // Bottom Area: Large Premium Answer Field & ANSWER Button
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Feedback message on incorrect attempts
        AnimatedVisibility(
          visible = state.feedbackMessage != null,
          enter = fadeIn(),
          exit = fadeOut()
        ) {
          Text(
            text = state.feedbackMessage ?: "",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = Color(0xFFF43F5E),
            modifier = Modifier
              .padding(bottom = 12.dp)
              .testTag("game_error_text")
          )
        }

        // Large Premium Answer Field
        TQAnswerInput(
          value = state.userInput,
          onValueChange = onInputChange,
          onDone = onSubmit,
          placeholder = Localization.get("type_your_answer", language),
          testTag = "tq_game_answer_field"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Primary ANSWER Button
        TQPrimaryButton(
          text = "ANSWER",
          onClick = onSubmit,
          enabled = state.userInput.isNotBlank(),
          testTag = "tq_game_answer_button"
        )
      }
    }
  }
}
