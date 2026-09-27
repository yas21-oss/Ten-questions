package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
  onSplashFinished: () -> Unit,
  modifier: Modifier = Modifier
) {
  // Phase 1: Company Logo (0 to 1800ms)
  // Phase 2: Game Title "Ten Questions" (1800ms to 3600ms)
  // Then finish
  var currentPhase by remember { mutableIntStateOf(1) }
  val scaleAnim = remember { Animatable(0.92f) }

  LaunchedEffect(Unit) {
    scaleAnim.animateTo(
      targetValue = 1.05f,
      animationSpec = tween(durationMillis = 1800, easing = FastOutSlowInEasing)
    )
    currentPhase = 2
    scaleAnim.snapTo(0.95f)
    scaleAnim.animateTo(
      targetValue = 1.02f,
      animationSpec = tween(durationMillis = 1600, easing = FastOutSlowInEasing)
    )
    delay(400)
    onSplashFinished()
  }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(
            Color(0xFF0A0E1A),
            Color(0xFF131B2E),
            Color(0xFF0F172A)
          )
        )
      ),
    contentAlignment = Alignment.Center
  ) {
    // Phase 1: Company Logo
    AnimatedVisibility(
      visible = currentPhase == 1,
      enter = fadeIn(tween(600)),
      exit = fadeOut(tween(500))
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.scale(scaleAnim.value)
      ) {
        // Phase 1: Studio Logo
        Image(
          painter = painterResource(id = R.drawable.company_logo),
          contentDescription = "Studio Logo",
          modifier = Modifier
            .size(180.dp)
            .clip(RoundedCornerShape(32.dp))
            .testTag("company_logo_image")
        )
      }
    }

    // Phase 2: Game Title "Ten Questions"
    AnimatedVisibility(
      visible = currentPhase == 2,
      enter = fadeIn(tween(600)),
      exit = fadeOut(tween(400))
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.scale(scaleAnim.value)
      ) {
        // App icon badge
        Image(
          painter = painterResource(id = R.drawable.app_launcher_icon_1790422476071),
          contentDescription = "Game Emblem",
          modifier = Modifier
            .size(110.dp)
            .clip(RoundedCornerShape(26.dp))
        )
        Spacer(modifier = Modifier.height(28.dp))
        Text(
          text = "Ten Questions",
          style = MaterialTheme.typography.displayMedium.copy(
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
          ),
          color = Color.White,
          textAlign = TextAlign.Center,
          modifier = Modifier.testTag("game_title_splash")
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "Think. Type. Conquer.",
          style = MaterialTheme.typography.bodyMedium.copy(
            letterSpacing = 3.sp,
            fontWeight = FontWeight.Medium
          ),
          color = Color(0xFFF59E0B)
        )
      }
    }
  }
}
