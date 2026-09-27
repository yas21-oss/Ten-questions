package com.example.ui.viewmodel

import android.app.Application
import android.os.SystemClock
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.AppLanguage
import com.example.data.model.Question
import com.example.data.model.UserAccount
import com.example.data.repository.GameRepository
import com.example.data.repository.SyncStatus
import com.example.data.validation.AnswerValidator
import com.example.data.validation.AntiCheatEngine
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class GameState {
  data object Idle : GameState()
  data object Loading : GameState()
  data class Playing(
    val stageNumber: Int,
    val questions: List<Question>,
    val currentQuestionIndex: Int, // 0..9
    val attemptsLeft: Int, // 3 down to 0
    val userInput: String,
    val stageAccumulatedXp: Int = 0, // 0..1000
    val lastAwardedXp: Int? = null,
    val feedbackMessage: String? = null,
    val isErrorShake: Boolean = false,
    val elapsedSeconds: Long = 0L
  ) : GameState() {
    val currentQuestion: Question
      get() = questions[currentQuestionIndex]
  }

  data class Won(
    val stageNumber: Int,
    val timeTakenSeconds: Long,
    val stageTotalXp: Int, // 0..1000 XP
    val starsEarned: Int // 0..3 stars
  ) : GameState()

  data class Failed(
    val stageNumber: Int,
    val failedQuestion: Question,
    val correctAnswers: List<String>,
    val explanation: String
  ) : GameState()
}

class GameViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = GameRepository(application, AppDatabase.getDatabase(application))

  private val _gameState = MutableStateFlow<GameState>(GameState.Idle)
  val gameState: StateFlow<GameState> = _gameState.asStateFlow()

  val globalRanking: Flow<List<UserAccount>> = repository.getGlobalRanking()

  val syncStatus: SyncStatus
    get() = repository.getSyncStatus()

  private var timerJob: Job? = null
  private var stageStartTime: Long = 0L
  private var isSubmitting = false

  fun startStage(
    stageNumber: Int,
    language: AppLanguage,
    playerId: String = "guest_player",
    isReplay: Boolean = false
  ) {
    viewModelScope.launch {
      _gameState.value = GameState.Loading
      val questions = repository.getQuestionsForStage(stageNumber, language, playerId, isReplay)
      stageStartTime = SystemClock.elapsedRealtime()
      isSubmitting = false

      _gameState.value = GameState.Playing(
        stageNumber = stageNumber,
        questions = questions,
        currentQuestionIndex = 0,
        attemptsLeft = 3,
        userInput = "",
        stageAccumulatedXp = 0,
        lastAwardedXp = null,
        elapsedSeconds = 0L
      )

      startTimer()
    }
  }

  private fun startTimer() {
    timerJob?.cancel()
    timerJob = viewModelScope.launch {
      while (true) {
        delay(1000L)
        _gameState.update { state ->
          if (state is GameState.Playing) {
            val seconds = (SystemClock.elapsedRealtime() - stageStartTime) / 1000L
            state.copy(elapsedSeconds = seconds)
          } else {
            state
          }
        }
      }
    }
  }

  fun updateInput(input: String) {
    _gameState.update { state ->
      if (state is GameState.Playing) {
        state.copy(userInput = input, feedbackMessage = null, isErrorShake = false)
      } else state
    }
  }

  fun submitAnswer(currentUser: UserAccount?) {
    if (isSubmitting) return
    val currentState = _gameState.value as? GameState.Playing ?: return
    val answer = currentState.userInput.trim()
    if (answer.isEmpty()) return

    isSubmitting = true
    val currentQuestion = currentState.currentQuestion
    val validation = AnswerValidator.evaluate(answer, currentQuestion)

    if (validation.isCorrect) {
      val earnedXp = validation.xpEarned
      val newAccumulatedXp = (currentState.stageAccumulatedXp + earnedXp).coerceAtMost(1000)

      if (currentState.currentQuestionIndex >= 9) {
        // All 10 questions answered correctly!
        timerJob?.cancel()
        val totalTime = ((SystemClock.elapsedRealtime() - stageStartTime) / 1000L).coerceAtLeast(4L)
        val uid = currentUser?.uid ?: "guest_player"
        val token = AntiCheatEngine.generateToken(uid, currentState.stageNumber, 10, totalTime)
        val stars = repository.calculateStars(newAccumulatedXp)

        viewModelScope.launch {
          try {
            if (currentUser != null) {
              val result = repository.recordStageWin(
                user = currentUser,
                stageNumber = currentState.stageNumber,
                stageTotalXp = newAccumulatedXp,
                timeTakenSeconds = totalTime,
                token = token
              )
              val finalStars = result.getOrNull()?.third ?: stars
              _gameState.value = GameState.Won(
                stageNumber = currentState.stageNumber,
                timeTakenSeconds = totalTime,
                stageTotalXp = newAccumulatedXp,
                starsEarned = finalStars
              )
            } else {
              _gameState.value = GameState.Won(
                stageNumber = currentState.stageNumber,
                timeTakenSeconds = totalTime,
                stageTotalXp = newAccumulatedXp,
                starsEarned = stars
              )
            }
          } finally {
            isSubmitting = false
          }
        }
      } else {
        // Next question with XP popup trigger
        _gameState.update {
          currentState.copy(
            currentQuestionIndex = currentState.currentQuestionIndex + 1,
            attemptsLeft = 3,
            userInput = "",
            stageAccumulatedXp = newAccumulatedXp,
            lastAwardedXp = earnedXp,
            feedbackMessage = null,
            isErrorShake = false
          )
        }
        isSubmitting = false
      }
    } else {
      // Incorrect answer: decrease attempt
      val remainingAttempts = currentState.attemptsLeft - 1
      if (remainingAttempts <= 0) {
        timerJob?.cancel()
        _gameState.value = GameState.Failed(
          stageNumber = currentState.stageNumber,
          failedQuestion = currentQuestion,
          correctAnswers = currentQuestion.acceptedAnswers,
          explanation = currentQuestion.explanation
        )
      } else {
        _gameState.update {
          currentState.copy(
            attemptsLeft = remainingAttempts,
            feedbackMessage = "Incorrect. $remainingAttempts attempts remaining.",
            isErrorShake = true
          )
        }
      }
      isSubmitting = false
    }
  }

  fun resetToIdle() {
    timerJob?.cancel()
    isSubmitting = false
    _gameState.value = GameState.Idle
  }

  override fun onCleared() {
    super.onCleared()
    timerJob?.cancel()
  }
}
