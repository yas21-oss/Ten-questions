package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.StageProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface StageProgressDao {
  @Query("SELECT * FROM stage_progress ORDER BY stageNumber ASC")
  fun getAllStageProgress(): Flow<List<StageProgress>>

  @Query("SELECT * FROM stage_progress WHERE stageNumber = :stageNumber LIMIT 1")
  suspend fun getProgressForStage(stageNumber: Int): StageProgress?

  @Query("SELECT * FROM stage_progress WHERE completed = 1")
  suspend fun getAllCompletedStages(): List<StageProgress>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertStageProgress(progress: StageProgress)

  @Query("SELECT MAX(stageNumber) FROM stage_progress WHERE completed = 1")
  suspend fun getHighestCompletedStage(): Int?

  @Query("DELETE FROM stage_progress")
  suspend fun deleteAllProgress()
}
