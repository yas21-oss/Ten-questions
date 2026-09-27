package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.squareup.moshi.JsonClass

enum class AnswerType {
  TEXT,
  NUMBER,
  DATE,
  YEAR,
  DECIMAL,
  UNIT,
  MULTI_ACCEPTED_TEXT
}

enum class DifficultyTier(val minScore: Int, val maxScore: Int, val label: String) {
  VERY_EASY(1, 20, "Very Easy"),
  EASY(21, 40, "Easy"),
  MEDIUM(41, 60, "Medium"),
  HARD(61, 80, "Hard"),
  VERY_HARD(81, 95, "Very Hard"),
  CHALLENGE(96, 100, "Challenge");

  companion object {
    fun fromScore(score: Int): DifficultyTier = when {
      score <= 20 -> VERY_EASY
      score <= 40 -> EASY
      score <= 60 -> MEDIUM
      score <= 80 -> HARD
      score <= 95 -> VERY_HARD
      else -> CHALLENGE
    }
  }
}

fun calculateDefaultScore(difficulty: Int): Int {
  return when (difficulty) {
    1 -> 10
    2 -> 20
    3 -> 30
    4 -> 40
    5 -> 50
    6 -> 60
    7 -> 70
    8 -> 80
    9 -> 90
    10 -> 98
    else -> (difficulty * 10).coerceIn(1, 100)
  }
}

@JsonClass(generateAdapter = true)
@Entity(tableName = "questions")
@TypeConverters(StringListConverter::class)
data class Question(
  @PrimaryKey
  val id: String,
  val language: String,
  val category: String,
  val difficulty: Int = 1, // Legacy 1 to 10 tier
  val question: String,
  val answerType: AnswerType = AnswerType.TEXT,
  val acceptedAnswers: List<String>,
  val correctNumericValue: Double? = null,
  val tolerance: Double? = null, // relative tolerance e.g. 0.05 = 5%
  val explanation: String = "",
  val source: String = "Verified Trivia Database",
  val verified: Boolean = true,
  val difficultyScore: Int = calculateDefaultScore(difficulty), // 1 to 100 real difficulty score
  val conceptId: String = "" // Semantic concept identifier for deduplication
) {
  val difficultyTier: DifficultyTier
    get() = DifficultyTier.fromScore(difficultyScore)

  val effectiveConceptId: String
    get() = if (conceptId.isNotBlank()) conceptId else id

  val primaryAnswer: String
    get() = acceptedAnswers.firstOrNull()?.trim()?.lowercase() ?: ""
}

class StringListConverter {
  @TypeConverter
  fun fromList(list: List<String>?): String {
    return list?.joinToString("||") ?: ""
  }

  @TypeConverter
  fun toList(data: String?): List<String> {
    if (data.isNullOrEmpty()) return emptyList()
    return data.split("||")
  }
}
