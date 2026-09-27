package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "question_history")
data class QuestionHistoryEntity(
  @PrimaryKey
  val questionId: String,
  val conceptId: String,
  val primaryAnswer: String,
  val stageSeen: Int,
  val language: String,
  val timestamp: Long = System.currentTimeMillis()
)
