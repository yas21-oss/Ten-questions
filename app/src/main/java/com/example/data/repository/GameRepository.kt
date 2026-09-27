package com.example.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.example.data.db.AppDatabase
import com.example.data.db.QuestionHistoryEntity
import com.example.data.model.AppLanguage
import com.example.data.model.Question
import com.example.data.model.StageProgress
import com.example.data.model.UserAccount
import com.example.data.questions.QuestionBank
import com.example.data.questions.QuestionHistoryTracker
import com.example.data.validation.AntiCheatEngine
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class SyncStatus(
  val label: String,
  val isOnline: Boolean,
  val isCloudSynced: Boolean,
  val hasPendingSync: Boolean
)

class GameRepository(
  private val context: Context,
  private val database: AppDatabase
) {

  private var hasUnsyncedChanges: Boolean = false
  val playerHistoryTracker = QuestionHistoryTracker()

  private val firestore: FirebaseFirestore? by lazy {
    try {
      if (FirebaseApp.getApps(context).isNotEmpty()) {
        FirebaseFirestore.getInstance()
      } else {
        null
      }
    } catch (e: Exception) {
      Log.w("GameRepository", "Firestore not available: ${e.message}")
      null
    }
  }

  suspend fun getQuestionsForStage(
    stage: Int,
    language: AppLanguage,
    playerId: String = "guest_player",
    isReplay: Boolean = false
  ): List<Question> = withContext(Dispatchers.IO) {
    if (playerHistoryTracker.getSeenQuestionIds().isEmpty()) {
      try {
        val seenIds = database.questionHistoryDao().getSeenQuestionIds(language.code)
        val seenConcepts = database.questionHistoryDao().getSeenConceptIds(language.code)
        val recentAnswers = database.questionHistoryDao().getRecentAnswers(language.code, 20)
        playerHistoryTracker.addExistingSeen(seenIds, seenConcepts, recentAnswers)
      } catch (e: Exception) {
        Log.w("GameRepository", "Could not load question history: ${e.message}")
      }
    }

    // 1. Check for an existing uncompleted assignment (session persistence when player closes and reopens app)
    val existingAssignment = try {
      database.stageAssignmentDao().getAssignment(playerId, stage)
    } catch (e: Exception) {
      null
    }

    if (!isReplay && existingAssignment != null && !existingAssignment.isCompleted) {
      try {
        val qIds = existingAssignment.questionIdsJson.split(",").filter { it.isNotBlank() }
        val pool = QuestionBank.curatedQuestions[language.code] ?: QuestionBank.getAllQuestions()
        val mappedQuestions = qIds.mapNotNull { id -> pool.find { it.id == id } }
        if (mappedQuestions.size == 10) {
          return@withContext mappedQuestions
        }
      } catch (e: Exception) {
        Log.w("GameRepository", "Could not restore existing assignment: ${e.message}")
      }
    }

    // 2. Generate personalized questions for this player & stage
    val attemptIndex = if (isReplay && existingAssignment != null) {
      existingAssignment.attemptIndex + 1
    } else {
      existingAssignment?.attemptIndex ?: 0
    }

    val questions = QuestionBank.getQuestionsForStage(
      stage = stage,
      language = language,
      historyTracker = playerHistoryTracker,
      playerId = playerId,
      attemptIndex = attemptIndex
    )

    // 3. Save assignment into database for persistence
    try {
      val assignment = com.example.data.db.StageAssignmentEntity(
        playerId = playerId,
        stageId = stage,
        assignmentId = "asg_${playerId}_s${stage}_att${attemptIndex}_${System.currentTimeMillis()}",
        seed = QuestionBank.computePlayerStageSeed(playerId, stage, QuestionBank.QUESTION_POOL_VERSION, attemptIndex),
        questionIdsJson = questions.joinToString(",") { it.id },
        questionPoolVersion = QuestionBank.QUESTION_POOL_VERSION,
        attemptIndex = attemptIndex,
        isCompleted = false
      )
      database.stageAssignmentDao().saveAssignment(assignment)
    } catch (e: Exception) {
      Log.w("GameRepository", "Could not persist stage assignment: ${e.message}")
    }

    // 4. Record into question history
    try {
      val entities = questions.map {
        QuestionHistoryEntity(
          questionId = it.id,
          conceptId = it.effectiveConceptId,
          primaryAnswer = it.primaryAnswer,
          stageSeen = stage,
          language = language.code
        )
      }
      database.questionHistoryDao().recordSeenQuestions(entities)
    } catch (e: Exception) {
      Log.w("GameRepository", "Could not persist question history: ${e.message}")
    }

    questions
  }

  fun getGlobalRanking(): Flow<List<UserAccount>> {
    return database.userDao().getAllUsersRankedByStars()
  }

  /**
   * Calculates stars from stage XP:
   * 900–1000 XP = 3 stars (⭐⭐⭐)
   * 700–899 XP  = 2 stars (⭐⭐)
   * 400–699 XP  = 1 star  (⭐)
   * 0–399 XP    = 0 stars
   */
  fun calculateStars(stageXp: Int): Int {
    return when {
      stageXp >= 900 -> 3
      stageXp >= 700 -> 2
      stageXp >= 400 -> 1
      else -> 0
    }
  }

  fun getSyncStatus(): SyncStatus {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val network = cm?.activeNetwork
    val caps = cm?.getNetworkCapabilities(network)
    val isOnline = caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

    if (!isOnline) {
      return if (hasUnsyncedChanges) {
        SyncStatus(
          label = "Offline (Unsynchronized Local Progress)",
          isOnline = false,
          isCloudSynced = false,
          hasPendingSync = true
        )
      } else {
        SyncStatus(
          label = "Offline (Local Storage Active)",
          isOnline = false,
          isCloudSynced = false,
          hasPendingSync = false
        )
      }
    }

    return if (firestore != null) {
      if (hasUnsyncedChanges) {
        SyncStatus(
          label = "Unsynchronized Progress (Pending Cloud Sync)",
          isOnline = true,
          isCloudSynced = false,
          hasPendingSync = true
        )
      } else {
        SyncStatus(
          label = "Server Synchronized (Cloud Active)",
          isOnline = true,
          isCloudSynced = true,
          hasPendingSync = false
        )
      }
    } else {
      SyncStatus(
        label = if (hasUnsyncedChanges) "Local Cache (Pending Cloud Push)" else "Local Cache Active (Production OAuth Pending)",
        isOnline = true,
        isCloudSynced = false,
        hasPendingSync = hasUnsyncedChanges
      )
    }
  }

  suspend fun recordStageWin(
    user: UserAccount,
    stageNumber: Int,
    stageTotalXp: Int, // 0..1000 XP
    timeTakenSeconds: Long,
    token: String
  ): Result<Triple<UserAccount, Int, Int>> = withContext(Dispatchers.IO) {
    val validation = AntiCheatEngine.validateStageCompletion(
      uid = user.uid,
      stageNumber = stageNumber,
      currentUnlockedStage = user.unlockedStage,
      score = 10,
      timeTakenSeconds = timeTakenSeconds,
      token = token
    )

    if (validation.isFailure) {
      return@withContext Result.failure(validation.exceptionOrNull() ?: Exception("Validation failed"))
    }

    val clampedStageXp = stageTotalXp.coerceIn(0, 1000)
    val starsEarned = calculateStars(clampedStageXp)

    // Check existing progress to handle replaying correctly without corrupting total XP or duplicating stars
    val existingProgress = database.stageProgressDao().getProgressForStage(stageNumber)
    val previousStageXp = existingProgress?.stageXp ?: 0
    val previousStars = existingProgress?.starsEarned ?: 0

    val netNewXp = if (existingProgress != null && existingProgress.completed) {
      (clampedStageXp - previousStageXp).coerceAtLeast(0)
    } else {
      clampedStageXp
    }

    val netNewStars = if (existingProgress != null && existingProgress.completed) {
      (starsEarned - previousStars).coerceAtLeast(0)
    } else {
      starsEarned
    }

    val nextStage = stageNumber + 1
    val newUnlockedStage = maxOf(user.unlockedStage, nextStage)
    val newHighestStage = maxOf(user.highestStage, stageNumber)

    val updatedUser = user.copy(
      unlockedStage = newUnlockedStage,
      highestStage = newHighestStage,
      xp = user.xp + netNewXp,
      totalStars = user.totalStars + netNewStars,
      stagesWon = if (existingProgress?.completed == true) user.stagesWon else user.stagesWon + 1,
      questionsAnswered = user.questionsAnswered + 10,
      bestStreak = user.bestStreak + 1,
      totalTimeSeconds = user.totalTimeSeconds + timeTakenSeconds,
      lastSyncTimestamp = System.currentTimeMillis()
    )

    // Save to local database
    database.userDao().updateUser(updatedUser)

    val progressRecord = StageProgress(
      stageNumber = stageNumber,
      completed = true,
      score = 10,
      stageXp = maxOf(clampedStageXp, previousStageXp),
      starsEarned = maxOf(starsEarned, previousStars),
      timeTakenSeconds = timeTakenSeconds,
      attemptsUsed = 10,
      completedAt = System.currentTimeMillis(),
      validationToken = token
    )
    database.stageProgressDao().insertStageProgress(progressRecord)

    // Mark current stage assignment as completed
    try {
      database.stageAssignmentDao().markCompleted(user.uid, stageNumber)
    } catch (e: Exception) {
      Log.w("GameRepository", "Could not mark assignment completed: ${e.message}")
    }

    // Sync online to cloud if Firestore is available
    syncProgressOnline(updatedUser, progressRecord)

    Result.success(Triple(updatedUser, clampedStageXp, starsEarned))
  }

  suspend fun restoreProgressFromCloud(user: UserAccount): UserAccount? = withContext(Dispatchers.IO) {
    try {
      val db = firestore ?: return@withContext null
      val doc = db.collection("users").document(user.uid).get().await()
      if (doc.exists()) {
        val cloudUnlocked = doc.getLong("unlockedStage")?.toInt() ?: user.unlockedStage
        val cloudHighest = doc.getLong("highestStage")?.toInt() ?: user.highestStage
        val cloudXp = doc.getLong("xp") ?: user.xp
        val cloudStars = doc.getLong("totalStars")?.toInt() ?: user.totalStars
        val cloudWins = doc.getLong("stagesWon")?.toInt() ?: user.stagesWon

        val merged = user.copy(
          unlockedStage = maxOf(user.unlockedStage, cloudUnlocked),
          highestStage = maxOf(user.highestStage, cloudHighest),
          xp = maxOf(user.xp, cloudXp),
          totalStars = maxOf(user.totalStars, cloudStars),
          stagesWon = maxOf(user.stagesWon, cloudWins),
          lastSyncTimestamp = System.currentTimeMillis()
        )
        database.userDao().updateUser(merged)
        hasUnsyncedChanges = false
        merged
      } else {
        null
      }
    } catch (e: Exception) {
      Log.w("GameRepository", "Cloud restore skipped: ${e.message}")
      null
    }
  }

  private fun syncProgressOnline(user: UserAccount, stageProgress: StageProgress) {
    try {
      val db = firestore
      if (db == null) {
        hasUnsyncedChanges = true
        return
      }

      val userMap = hashMapOf(
        "uid" to user.uid,
        "displayName" to user.displayName,
        "unlockedStage" to user.unlockedStage,
        "highestStage" to user.highestStage,
        "xp" to user.xp,
        "totalStars" to user.totalStars,
        "stagesWon" to user.stagesWon,
        "lastSync" to System.currentTimeMillis()
      )
      db.collection("users").document(user.uid).set(userMap, SetOptions.merge())

      val stageMap = hashMapOf(
        "stageNumber" to stageProgress.stageNumber,
        "score" to stageProgress.score,
        "stageXp" to stageProgress.stageXp,
        "starsEarned" to stageProgress.starsEarned,
        "timeTaken" to stageProgress.timeTakenSeconds,
        "completedAt" to stageProgress.completedAt,
        "token" to stageProgress.validationToken
      )
      db.collection("users")
        .document(user.uid)
        .collection("stages")
        .document("stage_${stageProgress.stageNumber}")
        .set(stageMap, SetOptions.merge())

      hasUnsyncedChanges = false
    } catch (e: Exception) {
      Log.w("GameRepository", "Online sync queued: ${e.message}")
      hasUnsyncedChanges = true
    }
  }
}
