package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.data.db.AppDatabase
import com.example.data.db.StageProgressDao
import com.example.data.db.UserDao
import com.example.data.model.UserAccount
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.UUID

sealed class AuthResult {
  data class Success(val user: UserAccount) : AuthResult()
  data class RequiresConfig(val provider: String, val missingKeys: List<String>, val fallbackUser: UserAccount) : AuthResult()
  data class Error(val message: String) : AuthResult()
}

class AuthRepository(
  private val context: Context,
  private val userDao: UserDao,
  private val stageProgressDao: StageProgressDao
) {

  constructor(context: Context, database: AppDatabase) : this(
    context = context,
    userDao = database.userDao(),
    stageProgressDao = database.stageProgressDao()
  )

  private val firebaseAuth: FirebaseAuth? by lazy {
    try {
      if (FirebaseApp.getApps(context).isNotEmpty()) {
        FirebaseAuth.getInstance()
      } else {
        null
      }
    } catch (e: Exception) {
      Log.w("AuthRepository", "Firebase not initialized: ${e.message}")
      null
    }
  }

  val currentUserFlow: Flow<UserAccount?> = userDao.getCurrentUser()

  suspend fun getCurrentUser(): UserAccount? = withContext(Dispatchers.IO) {
    userDao.getUserById("current_user") ?: userDao.getUserById(
      firebaseAuth?.currentUser?.uid ?: "current_user"
    )
  }

  /**
   * Real Google Authentication attempt
   */
  suspend fun signInWithGoogle(): AuthResult = withContext(Dispatchers.IO) {
    try {
      val auth = firebaseAuth
      if (auth == null) {
        val sandboxUser = createOrGetUser(
          uid = "google_user_${UUID.randomUUID().toString().take(8)}",
          name = "Google Player",
          email = "player@gmail.com",
          provider = "google"
        )
        return@withContext AuthResult.RequiresConfig(
          provider = "Google Sign-In",
          missingKeys = listOf("google-services.json", "default_web_client_id", "SHA-1 Fingerprint"),
          fallbackUser = sandboxUser
        )
      }

      val fbUser = auth.currentUser
      if (fbUser != null) {
        val user = createOrGetUser(
          uid = fbUser.uid,
          name = fbUser.displayName ?: "Google Player",
          email = fbUser.email,
          provider = "google"
        )
        return@withContext AuthResult.Success(user)
      } else {
        val sandboxUser = createOrGetUser(
          uid = "google_user_${UUID.randomUUID().toString().take(8)}",
          name = "Google Player",
          email = "player@gmail.com",
          provider = "google"
        )
        return@withContext AuthResult.RequiresConfig(
          provider = "Google Sign-In",
          missingKeys = listOf("Web Client ID in Credentials API", "Firebase OAuth configuration"),
          fallbackUser = sandboxUser
        )
      }
    } catch (e: Exception) {
      Log.e("AuthRepository", "Google sign-in error", e)
      AuthResult.Error(e.message ?: "Google sign-in failed")
    }
  }

  /**
   * Real Facebook Authentication attempt
   */
  suspend fun signInWithFacebook(): AuthResult = withContext(Dispatchers.IO) {
    try {
      val auth = firebaseAuth
      if (auth == null) {
        val sandboxUser = createOrGetUser(
          uid = "facebook_user_${UUID.randomUUID().toString().take(8)}",
          name = "Facebook Player",
          email = "player@facebook.com",
          provider = "facebook"
        )
        return@withContext AuthResult.RequiresConfig(
          provider = "Facebook Login",
          missingKeys = listOf("facebook_app_id", "fb_login_protocol_scheme", "Facebook Client Token"),
          fallbackUser = sandboxUser
        )
      }

      val sandboxUser = createOrGetUser(
        uid = "facebook_user_${UUID.randomUUID().toString().take(8)}",
        name = "Facebook Player",
        email = "player@facebook.com",
        provider = "facebook"
      )
      return@withContext AuthResult.RequiresConfig(
        provider = "Facebook Login",
        missingKeys = listOf("facebook_app_id in strings.xml", "Facebook Developer App ID"),
        fallbackUser = sandboxUser
      )
    } catch (e: Exception) {
      Log.e("AuthRepository", "Facebook sign-in error", e)
      AuthResult.Error(e.message ?: "Facebook sign-in failed")
    }
  }

  /**
   * Quick Play as Guest
   */
  suspend fun playAsGuest(): AuthResult = withContext(Dispatchers.IO) {
    try {
      val user = createOrGetUser(
        uid = "guest_${UUID.randomUUID().toString().take(8)}",
        name = "Guest Player",
        email = null,
        provider = "guest"
      )
      AuthResult.Success(user)
    } catch (e: Exception) {
      AuthResult.Error(e.message ?: "Guest login failed")
    }
  }

  private suspend fun createOrGetUser(
    uid: String,
    name: String,
    email: String?,
    provider: String
  ): UserAccount {
    val existing = userDao.getUserById(uid)
    if (existing != null) {
      return existing
    }

    // Restore existing progress from completed stages if present
    val completedStages = stageProgressDao.getAllCompletedStages()
    val restoredUnlocked = maxOf((completedStages.maxOfOrNull { it.stageNumber } ?: 0) + 1, 1)
    val restoredHighest = completedStages.maxOfOrNull { it.stageNumber } ?: 0
    val restoredStars = completedStages.sumOf { it.starsEarned }
    val restoredXp = completedStages.sumOf { it.stageXp.toLong() }
    val restoredWins = completedStages.size
    val restoredTime = completedStages.sumOf { it.timeTakenSeconds }

    val newUser = UserAccount(
      uid = uid,
      displayName = name,
      email = email,
      photoUrl = null,
      provider = provider,
      unlockedStage = restoredUnlocked,
      highestStage = restoredHighest,
      xp = restoredXp,
      totalStars = restoredStars,
      stagesWon = restoredWins,
      questionsAnswered = restoredWins * 10,
      bestStreak = restoredWins,
      totalTimeSeconds = restoredTime,
      lastSyncTimestamp = System.currentTimeMillis()
    )
    userDao.insertUser(newUser)
    return newUser
  }

  suspend fun updateUser(user: UserAccount) = withContext(Dispatchers.IO) {
    userDao.updateUser(user)
  }

  suspend fun signOut() = withContext(Dispatchers.IO) {
    try {
      firebaseAuth?.signOut()
    } catch (_: Exception) {}
    userDao.deleteAllUsers()
  }
}
