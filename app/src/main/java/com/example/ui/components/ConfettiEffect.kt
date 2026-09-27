package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

data class Particle(
  val x: Float,
  val initialY: Float,
  val speed: Float,
  val size: Float,
  val color: Color
)

@Composable
fun ConfettiEffect(modifier: Modifier = Modifier) {
  val animProgress = remember { Animatable(0f) }

  val particles = remember {
    val colors = listOf(
      Color(0xFFF59E0B),
      Color(0xFF6366F1),
      Color(0xFF10B981),
      Color(0xFFEF4444),
      Color(0xFFEC4899),
      Color(0xFF3B82F6)
    )
    List(70) {
      Particle(
        x = Random.nextFloat(),
        initialY = Random.nextFloat() * -500f,
        speed = Random.nextFloat() * 1200f + 800f,
        size = Random.nextFloat() * 12f + 8f,
        color = colors.random()
      )
    }
  }

  LaunchedEffect(Unit) {
    animProgress.animateTo(
      targetValue = 1f,
      animationSpec = tween(durationMillis = 3500, easing = LinearEasing)
    )
  }

  Canvas(modifier = modifier.fillMaxSize()) {
    val progress = animProgress.value
    particles.forEach { p ->
      val currentY = p.initialY + (p.speed * progress)
      val currentX = p.x * size.width + kotlin.math.sin(progress * 10f + p.x * 20f) * 40f
      if (currentY in 0f..size.height) {
        drawRect(
          color = p.color,
          topLeft = Offset(currentX, currentY),
          size = Size(p.size, p.size * 1.5f)
        )
      }
    }
  }
}
