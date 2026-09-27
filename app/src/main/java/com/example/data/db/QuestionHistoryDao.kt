package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuestionHistoryDao {
  @Query("SELECT questionId FROM question_history WHERE language = :lang")
  suspend fun getSeenQuestionIds(lang: String): List<String>

  @Query("SELECT conceptId FROM question_history WHERE language = :lang")
  suspend fun getSeenConceptIds(lang: String): List<String>

  @Query("SELECT primaryAnswer FROM question_history WHERE language = :lang ORDER BY timestamp DESC LIMIT :limit")
  suspend fun getRecentAnswers(lang: String, limit: Int): List<String>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun recordSeenQuestions(records: List<QuestionHistoryEntity>)

  @Query("DELETE FROM question_history")
  suspend fun clearHistory()
}
