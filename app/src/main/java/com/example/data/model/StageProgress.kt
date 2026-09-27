package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stage_progress")
data class StageProgress(
  @PrimaryKey
  val stageNumber: Int,
  val completed: Boolean,
  val score: Int, // 10 out of 10
  val stageXp: Int, // 0 to 1000 XP
  val starsEarned: Int, // 0 to 3 stars
  val timeTakenSeconds: Long,
  val attemptsUsed: Int,
  val completedAt: Long,
  val validationToken: String
)
