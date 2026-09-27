package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StageAssignmentDao {
  @Query("SELECT * FROM stage_assignments WHERE playerId = :playerId AND stageId = :stageId")
  suspend fun getAssignment(playerId: String, stageId: Int): StageAssignmentEntity?

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun saveAssignment(assignment: StageAssignmentEntity)

  @Query("UPDATE stage_assignments SET isCompleted = 1 WHERE playerId = :playerId AND stageId = :stageId")
  suspend fun markCompleted(playerId: String, stageId: Int)

  @Query("DELETE FROM stage_assignments WHERE playerId = :playerId AND stageId = :stageId")
  suspend fun clearAssignment(playerId: String, stageId: Int)
}
