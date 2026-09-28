package com.example.ui.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

enum class SoundEffect {
  KEY_PRESS,
  BUTTON_CLICK,
  CORRECT_ANSWER,
  INCORRECT_ANSWER,
  HEART_LOST,
  XP_GAINED,
  STAR_EARNED,
  STAGE_COMPLETE,
  STAGE_FAILED,
  MIC_START,
  MIC_END,
  QUESTION_TRANSITION
}

/**
 * Pure in-memory procedural audio engine using Android AudioTrack.
 * Directly synthesizes raw 16-bit PCM waveforms without writing to disk or utilizing
 * native media decoders (Codec2 / OMX / MediaExtractor). This completely eliminates
 * "Failed to query component interface for required system resources" errors.
 */
class GameSoundEngine(private val context: Context) {

  private val pcmCache = ConcurrentHashMap<SoundEffect, ShortArray>()
  private var audioExecutor: ExecutorService? = null

  init {
    try {
      // Purge any legacy cached audio files on disk from earlier builds
      val legacyDir = File(context.cacheDir, "game_sounds")
      if (legacyDir.exists()) {
        legacyDir.deleteRecursively()
      }
    } catch (_: Throwable) {}

    try {
      audioExecutor = Executors.newSingleThreadExecutor { runnable ->
        Thread(runnable, "TQAudioEngineThread").apply {
          isDaemon = true
          priority = Thread.NORM_PRIORITY
        }
      }
    } catch (e: Throwable) {
      Log.w("GameSoundEngine", "Failed to start audio executor", e)
    }
  }

  fun play(effect: SoundEffect, isSoundEnabled: Boolean, volume: Float = 0.85f) {
    if (!isSoundEnabled) return
    val executor = audioExecutor ?: return
    if (executor.isShutdown || executor.isTerminated) return

    executor.execute {
      try {
        val minBufferSize = AudioTrack.getMinBufferSize(
          SAMPLE_RATE,
          AudioFormat.CHANNEL_OUT_MONO,
          AudioFormat.ENCODING_PCM_16BIT
        )
        if (minBufferSize <= 0) return@execute

        val samples = pcmCache.getOrPut(effect) { generatePcmForEffect(effect) }
        if (samples.isEmpty()) return@execute

        val bufferSize = maxOf(minBufferSize, samples.size * 2)

        val audioAttributes = AudioAttributes.Builder()
          .setUsage(AudioAttributes.USAGE_GAME)
          .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
          .build()

        val audioFormat = AudioFormat.Builder()
          .setSampleRate(SAMPLE_RATE)
          .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
          .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
          .build()

        val track = AudioTrack.Builder()
          .setAudioAttributes(audioAttributes)
          .setAudioFormat(audioFormat)
          .setBufferSizeInBytes(bufferSize)
          .setTransferMode(AudioTrack.MODE_STATIC)
          .build()

        track.write(samples, 0, samples.size)
        val clampedVol = volume.coerceIn(0f, 1f)
        track.setVolume(clampedVol)
        track.play()

        val durationMs = (samples.size * 1000L) / SAMPLE_RATE
        Thread.sleep(durationMs + 20L)
        try {
          track.stop()
        } catch (_: Throwable) {}
        try {
          track.release()
        } catch (_: Throwable) {}
      } catch (_: Throwable) {
        // Safe no-op for headless or audio-restricted runtimes
      }
    }
  }

  fun release() {
    try {
      audioExecutor?.shutdownNow()
      audioExecutor = null
      pcmCache.clear()
    } catch (_: Throwable) {}
  }

  companion object {
    private const val SAMPLE_RATE = 22050

    private fun generatePcmForEffect(effect: SoundEffect): ShortArray {
      val durationSec = when (effect) {
        SoundEffect.KEY_PRESS -> 0.025
        SoundEffect.BUTTON_CLICK -> 0.04
        SoundEffect.QUESTION_TRANSITION -> 0.08
        SoundEffect.HEART_LOST -> 0.18
        SoundEffect.INCORRECT_ANSWER -> 0.16
        SoundEffect.XP_GAINED -> 0.18
        SoundEffect.MIC_START -> 0.09
        SoundEffect.MIC_END -> 0.09
        SoundEffect.CORRECT_ANSWER -> 0.28
        SoundEffect.STAR_EARNED -> 0.35
        SoundEffect.STAGE_FAILED -> 0.32
        SoundEffect.STAGE_COMPLETE -> 0.55
      }

      val numSamples = (SAMPLE_RATE * durationSec).toInt()
      val samples = ShortArray(numSamples)

      for (i in 0 until numSamples) {
        val t = i.toDouble() / SAMPLE_RATE
        val sample = when (effect) {
          SoundEffect.KEY_PRESS -> {
            val env = exp(-t * 120.0)
            (sin(2 * PI * 1100.0 * t) * env * 0.4)
          }

          SoundEffect.BUTTON_CLICK -> {
            val freq = 550.0 - (t / durationSec) * 250.0
            val env = exp(-t * 70.0)
            (sin(2 * PI * freq * t) * env * 0.5)
          }

          SoundEffect.CORRECT_ANSWER -> {
            // High arpeggiated major chord: C5 (523Hz), E5 (659Hz), G5 (784Hz), C6 (1046Hz)
            val note1 = sin(2 * PI * 523.25 * t) * exp(-t * 10.0)
            val note2 = if (t > 0.05) sin(2 * PI * 659.25 * (t - 0.05)) * exp(-(t - 0.05) * 9.0) else 0.0
            val note3 = if (t > 0.10) sin(2 * PI * 783.99 * (t - 0.10)) * exp(-(t - 0.10) * 8.0) else 0.0
            val note4 = if (t > 0.15) sin(2 * PI * 1046.50 * (t - 0.15)) * exp(-(t - 0.15) * 6.0) else 0.0
            ((note1 + note2 + note3 + note4) * 0.35)
          }

          SoundEffect.INCORRECT_ANSWER -> {
            // Soft resonant double thud
            val freq = if (t < 0.08) 180.0 else 130.0
            val env = exp(-(t % 0.08) * 45.0)
            (sin(2 * PI * freq * t) * env * 0.6)
          }

          SoundEffect.HEART_LOST -> {
            // Descending tone
            val freq = 440.0 - (t / durationSec) * 160.0
            val env = exp(-t * 16.0)
            (sin(2 * PI * freq * t) * env * 0.5)
          }

          SoundEffect.XP_GAINED -> {
            // Rising sparkle tone
            val freq = 880.0 + (t / durationSec) * 880.0
            val env = (1.0 - t / durationSec)
            (sin(2 * PI * freq * t) * env * 0.45)
          }

          SoundEffect.STAR_EARNED -> {
            // Shimmering bell chord
            val s1 = sin(2 * PI * 1174.66 * t) * exp(-t * 8.0)
            val s2 = sin(2 * PI * 1567.98 * t) * exp(-t * 9.0)
            val s3 = sin(2 * PI * 2349.32 * t) * exp(-t * 12.0)
            ((s1 + s2 * 0.7 + s3 * 0.4) * 0.4)
          }

          SoundEffect.STAGE_COMPLETE -> {
            // Upbeat fanfare: C5, G5, C6 arpeggio
            val n1 = sin(2 * PI * 523.25 * t) * exp(-t * 5.0)
            val n2 = if (t > 0.09) sin(2 * PI * 659.25 * (t - 0.09)) * exp(-(t - 0.09) * 4.5) else 0.0
            val n3 = if (t > 0.18) sin(2 * PI * 783.99 * (t - 0.18)) * exp(-(t - 0.18) * 4.0) else 0.0
            val n4 = if (t > 0.27) sin(2 * PI * 1046.50 * (t - 0.27)) * exp(-(t - 0.27) * 3.0) else 0.0
            ((n1 + n2 + n3 + n4) * 0.35)
          }

          SoundEffect.STAGE_FAILED -> {
            val freq = 220.0 - (t / durationSec) * 70.0
            val env = exp(-t * 7.0)
            (sin(2 * PI * freq * t) * env * 0.5)
          }

          SoundEffect.MIC_START -> {
            val freq = 587.33 + (t / durationSec) * 293.66
            val env = exp(-t * 30.0)
            (sin(2 * PI * freq * t) * env * 0.5)
          }

          SoundEffect.MIC_END -> {
            val freq = 880.0 - (t / durationSec) * 293.66
            val env = exp(-t * 30.0)
            (sin(2 * PI * freq * t) * env * 0.5)
          }

          SoundEffect.QUESTION_TRANSITION -> {
            val freq = 600.0 + sin(t * 50.0) * 150.0
            val env = sin(PI * (t / durationSec))
            (sin(2 * PI * freq * t) * env * 0.3)
          }
        }
        val clamped = (sample * Short.MAX_VALUE).coerceIn(Short.MIN_VALUE.toDouble(), Short.MAX_VALUE.toDouble())
        samples[i] = clamped.toInt().toShort()
      }

      return samples
    }
  }
}
