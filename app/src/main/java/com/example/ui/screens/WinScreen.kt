package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ui.components.ConfettiEffect
import com.example.ui.components.TQPrimaryButton
import com.example.ui.components.TQStarRating

@Composable
fun WinScreen(
  stageNumber: Int,
  stageTotalXp: Int, // 0..1000 XP
  starsEarned: Int, // 0..3 stars
  language: AppLanguage,
  onNextStage: () -> Unit,
  onReplay: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(modifier = modifier.fillMaxSize()) {
    Surface(
      modifier = Modifier.fillMaxSize(),
      color = Color(0xFF090D18)
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
      ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Center Content
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          // 🎉 Celebration Icon
          Text(
            text = "🎉",
            fontSize = 56.sp
          )

          Spacer(modifier = Modifier.height(16.dp))

          Text(
            text = "STAGE COMPLETE",
            style = MaterialTheme.typography.displaySmall.copy(
              fontWeight = FontWeight.Black,
              letterSpacing = 2.sp,
              fontSize = 30.sp
            ),
            color = Color.White,
            textAlign = TextAlign.Center
          )

          Spacer(modifier = Modifier.height(28.dp))

          // 10 / 10 Score
          Text(
            text = "10 / 10",
            style = MaterialTheme.typography.headlineLarge.copy(
              fontWeight = FontWeight.Black,
              fontSize = 38.sp
            ),
            color = Color(0xFF38BDF8)
          )

          Spacer(modifier = Modifier.height(8.dp))

          // 947 / 1000 XP
          Text(
            text = "$stageTotalXp / 1000 XP",
            style = MaterialTheme.typography.titleLarge.copy(
              fontWeight = FontWeight.Bold,
              fontSize = 24.sp
            ),
            color = Color(0xFFFBBF24)
          )

          Spacer(modifier = Modifier.height(18.dp))

          // ⭐⭐⭐ Stars Display
          TQStarRating(stars = starsEarned, starSize = 36)
        }

        // Action Buttons: NEXT STAGE & REPLAY
        Column(
          modifier = Modifier.fillMaxWidth(),
          horizontalAlignment = Alignment.CenterHorizontally,
          verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
          TQPrimaryButton(
            text = "NEXT STAGE",
            onClick = onNextStage,
            testTag = "stage_complete_next_button"
          )

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(56.dp)
              .clip(RoundedCornerShape(18.dp))
              .background(Color(0xFF13192B))
              .border(1.dp, Color(0xFF232D48), RoundedCornerShape(18.dp))
              .clickable(onClick = onReplay)
              .testTag("stage_complete_replay_button"),
            contentAlignment = Alignment.Center
          ) {
            Text(
              text = "REPLAY",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
              ),
              color = Color(0xFF94A3B8)
            )
          }
        }
      }
    }

    // Subtle celebration confetti
    ConfettiEffect()
  }
}
