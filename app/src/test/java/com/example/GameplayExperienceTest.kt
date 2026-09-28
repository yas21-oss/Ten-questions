package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.db.AppDatabase
import com.example.data.model.AppLanguage
import com.example.data.repository.GameRepository
import com.example.ui.audio.GameHaptics
import com.example.ui.audio.GameSoundEngine
import com.example.ui.audio.SoundEffect
import com.example.ui.viewmodel.GameState
import com.example.ui.viewmodel.GameViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class GameplayExperienceTest {

  private lateinit var context: Context
  private lateinit var gameViewModel: GameViewModel
  private lateinit var gameRepository: GameRepository

  @Before
  fun setUp() {
    context = ApplicationProvider.getApplicationContext<Context>()
    val database = AppDatabase.getDatabase(context)
    gameRepository = GameRepository(context, database)
    gameViewModel = GameViewModel(ApplicationProvider.getApplicationContext())
  }

  // 1. Sound Engine Verification: Tests all sound effects and mute handling
  @Test
  fun testGameSoundEngineAllEffectsAndMute() {
    val soundEngine = GameSoundEngine(context)

    SoundEffect.entries.forEach { effect ->
      // Play when enabled
      soundEngine.play(effect, isSoundEnabled = true)
      // Play when disabled (muted)
      soundEngine.play(effect, isSoundEnabled = false)
    }

    soundEngine.release()
  }

  // 2. Haptics Engine Verification: Tests all tactile vibration patterns
  @Test
  fun testGameHapticsAllPatterns() {
    val haptics = GameHaptics(context)

    haptics.vibrateKey(isHapticEnabled = true)
    haptics.vibrateKey(isHapticEnabled = false)

    haptics.vibrateButton(isHapticEnabled = true)
    haptics.vibrateButton(isHapticEnabled = false)

    haptics.vibrateCorrect(isHapticEnabled = true)
    haptics.vibrateCorrect(isHapticEnabled = false)

    haptics.vibrateIncorrect(isHapticEnabled = true)
    haptics.vibrateIncorrect(isHapticEnabled = false)

    haptics.vibrateHeartLost(isHapticEnabled = true)
    haptics.vibrateHeartLost(isHapticEnabled = false)

    haptics.vibrateStar(isHapticEnabled = true)
    haptics.vibrateStar(isHapticEnabled = false)
  }

  private fun waitUntilState(predicate: (GameState) -> Boolean): GameState {
    var count = 0
    while (!predicate(gameViewModel.gameState.value) && count < 60) {
      Thread.sleep(40)
      ShadowLooper.idleMainLooper()
      count++
    }
    return gameViewModel.gameState.value
  }

  // 3. Gameplay Loop: Start stage, wrong answer loses heart, retry, correct answer advances
  @Test
  fun testGameplayHeartLossAndProgression() {
    gameViewModel.startStage(stageNumber = 1, language = AppLanguage.ENGLISH, playerId = "test_player_gamefeel")
    var state = waitUntilState { it is GameState.Playing } as GameState.Playing

    assertEquals(0, state.currentQuestionIndex)
    assertEquals(3, state.attemptsLeft)
    assertEquals(0, state.stageAccumulatedXp)

    val currentQ = state.currentQuestion
    assertNotNull(currentQ)

    // Submit wrong answer
    gameViewModel.updateInput("COMPLETELY_WRONG_ANSWER_XYZ")
    gameViewModel.submitAnswer(null)
    state = waitUntilState { it is GameState.Playing && (it as GameState.Playing).attemptsLeft == 2 } as GameState.Playing

    assertEquals("Should have lost one attempt", 2, state.attemptsLeft)
    assertTrue("Feedback message should be present", state.feedbackMessage != null)
    assertTrue("Error shake should be triggered", state.isErrorShake)
    assertEquals("Still on first question", 0, state.currentQuestionIndex)

    // Submit second wrong answer
    gameViewModel.updateInput("STILL_WRONG")
    gameViewModel.submitAnswer(null)
    state = waitUntilState { it is GameState.Playing && (it as GameState.Playing).attemptsLeft == 1 } as GameState.Playing

    assertEquals("Should have 1 attempt remaining", 1, state.attemptsLeft)

    // Now submit the correct answer
    val correctAnswer = currentQ.acceptedAnswers.first()
    gameViewModel.updateInput(correctAnswer)
    gameViewModel.submitAnswer(null)
    state = waitUntilState { it is GameState.Playing && (it as GameState.Playing).currentQuestionIndex == 1 } as GameState.Playing

    // Advances to question 1 (second question) with 3 full hearts restored
    assertEquals("Should advance to question 2", 1, state.currentQuestionIndex)
    assertEquals("Hearts should be reset to 3", 3, state.attemptsLeft)
    assertTrue("Accumulated XP should increase", state.stageAccumulatedXp > 0)
    assertTrue("Last awarded XP should be recorded", state.lastAwardedXp != null && state.lastAwardedXp!! > 0)
    assertFalse("Error shake reset", state.isErrorShake)
  }

  // 4. Stage Failure: Exhausting all 3 attempts triggers GameState.Failed with correct explanation
  @Test
  fun testStageFailureAfterThreeFailedAttempts() {
    gameViewModel.startStage(stageNumber = 1, language = AppLanguage.ENGLISH, playerId = "player_fail_test")
    val state = waitUntilState { it is GameState.Playing } as GameState.Playing
    val question = state.currentQuestion

    // Attempt 1 fail
    gameViewModel.updateInput("wrong_1")
    gameViewModel.submitAnswer(null)
    waitUntilState { it is GameState.Playing && (it as GameState.Playing).attemptsLeft == 2 }

    // Attempt 2 fail
    gameViewModel.updateInput("wrong_2")
    gameViewModel.submitAnswer(null)
    waitUntilState { it is GameState.Playing && (it as GameState.Playing).attemptsLeft == 1 }

    // Attempt 3 fail -> Triggers failure
    gameViewModel.updateInput("wrong_3")
    gameViewModel.submitAnswer(null)
    val failedState = waitUntilState { it is GameState.Failed } as GameState.Failed

    assertEquals(1, failedState.stageNumber)
    assertEquals(question.id, failedState.failedQuestion.id)
    assertTrue("Must provide correct answers", failedState.correctAnswers.isNotEmpty())
    assertEquals(question.explanation, failedState.explanation)
  }

  // 5. Star Thresholds Verification: 900-1000 = 3 stars, 700-899 = 2 stars, 400-699 = 1 star, <400 = 0 stars
  @Test
  fun testStarThresholdCalculations() {
    assertEquals(3, gameRepository.calculateStars(1000))
    assertEquals(3, gameRepository.calculateStars(950))
    assertEquals(3, gameRepository.calculateStars(900))

    assertEquals(2, gameRepository.calculateStars(899))
    assertEquals(2, gameRepository.calculateStars(750))
    assertEquals(2, gameRepository.calculateStars(700))

    assertEquals(1, gameRepository.calculateStars(699))
    assertEquals(1, gameRepository.calculateStars(500))
    assertEquals(1, gameRepository.calculateStars(400))

    assertEquals(0, gameRepository.calculateStars(399))
    assertEquals(0, gameRepository.calculateStars(200))
    assertEquals(0, gameRepository.calculateStars(0))
  }

  // 6. Complete 10-Question Stage Win
  @Test
  fun testCompleteStageWinFlow() {
    gameViewModel.startStage(stageNumber = 1, language = AppLanguage.ENGLISH, playerId = "player_win_test")
    waitUntilState { it is GameState.Playing }

    for (qIdx in 0..9) {
      val state = waitUntilState { it is GameState.Playing && (it as GameState.Playing).currentQuestionIndex == qIdx } as GameState.Playing
      assertEquals(qIdx, state.currentQuestionIndex)

      val currentQ = state.currentQuestion
      val answer = currentQ.acceptedAnswers.first()
      gameViewModel.updateInput(answer)
      gameViewModel.submitAnswer(null)
    }

    val wonState = waitUntilState { it is GameState.Won } as GameState.Won
    assertEquals(1, wonState.stageNumber)
    assertEquals(1000, wonState.stageTotalXp)
    assertEquals(3, wonState.starsEarned)
  }

  // 7. Personalization Preservation: Two players get different seeds and assignments
  @Test
  fun testPersonalizationPreservation() = runBlocking {
    val questionsPlayerA = gameRepository.getQuestionsForStage(1, AppLanguage.ENGLISH, playerId = "player_A")
    val questionsPlayerB = gameRepository.getQuestionsForStage(1, AppLanguage.ENGLISH, playerId = "player_B")

    assertEquals(10, questionsPlayerA.size)
    assertEquals(10, questionsPlayerB.size)

    val idsA = questionsPlayerA.map { it.id }.toSet()
    val idsB = questionsPlayerB.map { it.id }.toSet()

    assertFalse("Personalized assignments must differ between players", idsA == idsB)
  }

  // 8. Arabic Game Experience: Language support and Arabic normalization
  @Test
  fun testArabicGameExperience() = runBlocking {
    val questionsAr = gameRepository.getQuestionsForStage(1, AppLanguage.ARABIC, playerId = "player_arabic")
    assertEquals(10, questionsAr.size)

    for (q in questionsAr) {
      assertEquals("ar", q.language)
      assertTrue("Arabic question must contain Arabic text", q.question.any { it in '\u0600'..'\u06FF' })
    }
  }
}
