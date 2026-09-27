package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.Question

@Dao
interface QuestionDao {
  @Query("SELECT * FROM questions WHERE language = :lang AND difficulty = :diff ORDER BY RANDOM() LIMIT :limit")
  suspend fun getQuestionsByDifficulty(lang: String, diff: Int, limit: Int): List<Question>

  @Query("SELECT * FROM questions WHERE language = :lang ORDER BY RANDOM() LIMIT :limit")
  suspend fun getRandomQuestions(lang: String, limit: Int): List<Question>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertQuestions(questions: List<Question>)

  @Query("SELECT COUNT(*) FROM questions WHERE language = :lang")
  suspend fun getQuestionCountForLanguage(lang: String): Int
}
