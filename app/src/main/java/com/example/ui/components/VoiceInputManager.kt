package com.example.ui.components

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.AppLanguage

@Composable
fun rememberVoiceRecognizer(
  language: AppLanguage,
  onSpeechResult: (String) -> Unit,
  onListeningStateChanged: (Boolean) -> Unit
): () -> Unit {
  val context = LocalContext.current
  var speechRecognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }
  var isListening by remember { mutableStateOf(false) }

  val languageTag = when (language) {
    AppLanguage.ARABIC -> "ar-SA"
    AppLanguage.SPANISH -> "es-ES"
    AppLanguage.FRENCH -> "fr-FR"
    else -> "en-US"
  }

  val startRecognition = {
    if (!SpeechRecognizer.isRecognitionAvailable(context)) {
      Toast.makeText(context, "Voice input is not available on this device", Toast.LENGTH_SHORT).show()
    } else {
      try {
        speechRecognizer?.destroy()
        val recognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
          setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
              isListening = true
              onListeningStateChanged(true)
            }

            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
              isListening = false
              onListeningStateChanged(false)
            }

            override fun onError(error: Int) {
              isListening = false
              onListeningStateChanged(false)
              Log.d("VoiceRecognizer", "Speech error code: $error")
            }

            override fun onResults(results: Bundle?) {
              isListening = false
              onListeningStateChanged(false)
              val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
              if (!matches.isNullOrEmpty()) {
                val bestMatch = matches[0].trim()
                if (bestMatch.isNotBlank()) {
                  // Populates text field WITHOUT auto-submitting
                  onSpeechResult(bestMatch)
                }
              }
            }

            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
          })
        }
        speechRecognizer = recognizer

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
          putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
          putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageTag)
          putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your answer...")
          putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }
        recognizer.startListening(intent)
      } catch (e: Exception) {
        Log.e("VoiceRecognizer", "Failed to start listening", e)
        isListening = false
        onListeningStateChanged(false)
      }
    }
  }

  val permissionLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestPermission()
  ) { isGranted ->
    if (isGranted) {
      startRecognition()
    } else {
      Toast.makeText(context, "Microphone permission is required for voice answers", Toast.LENGTH_SHORT).show()
    }
  }

  DisposableEffect(Unit) {
    onDispose {
      try {
        speechRecognizer?.destroy()
      } catch (_: Exception) {}
      speechRecognizer = null
    }
  }

  return {
    val hasPermission = ContextCompat.checkSelfPermission(
      context,
      Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED

    if (hasPermission) {
      if (isListening) {
        speechRecognizer?.stopListening()
        isListening = false
        onListeningStateChanged(false)
      } else {
        startRecognition()
      }
    } else {
      permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
  }
}

/**
 * Animated listening banner displayed while speech recognition is active.
 */
@Composable
fun VoiceListeningIndicator(
  isListening: Boolean,
  onCancel: () -> Unit,
  modifier: Modifier = Modifier
) {
  if (!isListening) return

  val infiniteTransition = rememberInfiniteTransition(label = "mic_pulse")
  val pulseScale by infiniteTransition.animateFloat(
    initialValue = 0.9f,
    targetValue = 1.25f,
    animationSpec = infiniteRepeatable(
      animation = tween(650, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "pulse_scale"
  )

  Box(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(16.dp))
      .background(Color(0xFF0F172A))
      .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.6f), RoundedCornerShape(16.dp))
      .padding(horizontal = 16.dp, vertical = 10.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
          modifier = Modifier
            .size(32.dp)
            .scale(pulseScale)
            .clip(CircleShape)
            .background(Color(0xFFEF4444).copy(alpha = 0.25f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Microphone Active",
            tint = Color(0xFFEF4444),
            modifier = Modifier.size(18.dp)
          )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
          text = "Listening... Speak your answer",
          style = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          ),
          color = Color(0xFFF1F5F9)
        )
      }

      Text(
        text = "Tap mic to stop",
        style = MaterialTheme.typography.labelSmall,
        color = Color(0xFF94A3B8)
      )
    }
  }
}
