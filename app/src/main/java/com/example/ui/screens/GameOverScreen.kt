package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.Question
import com.example.ui.components.TQPrimaryButton

@Composable
fun GameOverScreen(
  stageNumber: Int,
  failedQuestion: Question,
  correctAnswers: List<String>,
  explanation: String,
  language: AppLanguage,
  onRetryStage: () -> Unit,
  modifier: Modifier = Modifier
) {
  Surface(
    modifier = modifier.fillMaxSize(),
    color = Color(0xFF090D18)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 24.dp, vertical = 32.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      Spacer(modifier = Modifier.height(10.dp))

      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Red indicator pill
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFEF4444).copy(alpha = 0.15f))
            .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
          Text(
            text = "3 ATTEMPTS EXHAUSTED",
            style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp),
            color = Color(0xFFEF4444),
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
          text = "STAGE FAILED",
          style = MaterialTheme.typography.displaySmall.copy(
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp,
            fontSize = 32.sp
          ),
          color = Color(0xFFEF4444),
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
          text = "Stage $stageNumber was not completed",
          style = MaterialTheme.typography.bodyMedium,
          color = Color(0xFF94A3B8)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Reveal Correct Answer & Explanation Box
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF13192B))
            .border(1.dp, Color(0xFF232D48), RoundedCornerShape(24.dp))
            .padding(24.dp)
        ) {
          Column(horizontalAlignment = Alignment.Start) {
            Text(
              text = failedQuestion.question,
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
              text = "CORRECT ANSWER",
              style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp),
              color = Color(0xFF94A3B8),
              fontWeight = FontWeight.Bold
            )

            Text(
              text = correctAnswers.firstOrNull() ?: "",
              style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Black,
                color = Color(0xFF10B981)
              ),
              modifier = Modifier
                .padding(top = 4.dp)
                .testTag("failed_revealed_answer")
            )

            if (explanation.isNotBlank()) {
              Spacer(modifier = Modifier.height(16.dp))
              Text(
                text = "EXPLANATION",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp),
                color = Color(0xFF94A3B8),
                fontWeight = FontWeight.Bold
              )
              Text(
                text = explanation,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFCBD5E1),
                modifier = Modifier.padding(top = 4.dp)
              )
            }
          }
        }
      }

      // Bottom: Primary RETRY STAGE button
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        TQPrimaryButton(
          text = "RETRY STAGE",
          onClick = onRetryStage,
          testTag = "retry_stage_button"
        )
      }
    }
  }
}
