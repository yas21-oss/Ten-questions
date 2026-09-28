package com.example.ui.audio

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

/**
 * Subtle and purposeful haptic feedback engine for Ten Questions.
 * Supports Android 8.0+ VibrationEffect and gracefully falls back on older or non-vibrating devices.
 */
class GameHaptics(private val context: Context) {

  private val vibrator: Vibrator? by lazy {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
      val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
      vibratorManager?.defaultVibrator
    } else {
      @Suppress("DEPRECATION")
      context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
    }
  }

  fun vibrateKey(isHapticEnabled: Boolean) {
    if (!isHapticEnabled) return
    vibrateSimple(12L, 40)
  }

  fun vibrateButton(isHapticEnabled: Boolean) {
    if (!isHapticEnabled) return
    vibrateSimple(22L, 80)
  }

  fun vibrateCorrect(isHapticEnabled: Boolean) {
    if (!isHapticEnabled) return
    vibrateWaveform(longArrayOf(0, 30, 40, 40), intArrayOf(0, 100, 0, 160))
  }

  fun vibrateIncorrect(isHapticEnabled: Boolean) {
    if (!isHapticEnabled) return
    vibrateWaveform(longArrayOf(0, 50, 40, 70), intArrayOf(0, 180, 0, 220))
  }

  fun vibrateHeartLost(isHapticEnabled: Boolean) {
    if (!isHapticEnabled) return
    vibrateSimple(50L, 150)
  }

  fun vibrateStar(isHapticEnabled: Boolean) {
    if (!isHapticEnabled) return
    vibrateWaveform(longArrayOf(0, 25, 30, 45), intArrayOf(0, 90, 0, 200))
  }

  private fun vibrateSimple(durationMs: Long, amplitude: Int) {
    try {
      val vib = vibrator ?: return
      if (!vib.hasVibrator()) return
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vib.vibrate(VibrationEffect.createOneShot(durationMs, amplitude.coerceIn(1, 255)))
      } else {
        @Suppress("DEPRECATION")
        vib.vibrate(durationMs)
      }
    } catch (_: Exception) {
      // Ignore on devices without vibrator or restrictions
    }
  }

  private fun vibrateWaveform(timings: LongArray, amplitudes: IntArray) {
    try {
      val vib = vibrator ?: return
      if (!vib.hasVibrator()) return
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vib.vibrate(VibrationEffect.createWaveform(timings, amplitudes, -1))
      } else {
        @Suppress("DEPRECATION")
        vib.vibrate(timings, -1)
      }
    } catch (_: Exception) {
      // Ignore
    }
  }
}
