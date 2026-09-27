package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
  tableName = "stage_assignments",
  primaryKeys = ["playerId", "stageId"]
)
data class StageAssignmentEntity(
  val playerId: String,
  val stageId: Int,
  val assignmentId: String,
  val seed: Long,
  val questionIdsJson: String,
  val questionPoolVersion: Int = 1,
  val attemptIndex: Int = 0,
  val isCompleted: Boolean = false,
  val createdAt: Long = System.currentTimeMillis()
)
