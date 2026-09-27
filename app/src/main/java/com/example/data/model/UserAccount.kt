package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserAccount(
  @PrimaryKey
  val uid: String,
  val displayName: String,
  val email: String?,
  val photoUrl: String?,
  val provider: String, // "google", "facebook", "guest"
  val selectedLanguage: String = "en",
  val unlockedStage: Int = 1,
  val highestStage: Int = 0,
  val xp: Long = 0L,
  val totalStars: Int = 0,
  val stagesWon: Int = 0,
  val questionsAnswered: Int = 0,
  val bestStreak: Int = 0,
  val totalTimeSeconds: Long = 0L,
  val lastSyncTimestamp: Long = System.currentTimeMillis()
) {
  val level: Int
    get() = (xp / 1000).toInt() + 1

  val levelProgress: Float
    get() = ((xp % 1000) / 1000f)
}
