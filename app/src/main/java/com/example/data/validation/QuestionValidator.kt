package com.example.data.validation

import com.example.data.model.AnswerType
import com.example.data.model.Question

sealed class QuestionValidationResult {
  object Valid : QuestionValidationResult()
  data class Invalid(val reason: String) : QuestionValidationResult()

  val isValid: Boolean get() = this is Valid
}

object QuestionValidator {

  val VALID_CATEGORIES = setOf(
    "animals", "food", "science", "geography", "mathematics",
    "technology", "sports", "everyday", "language", "logic",
    "art", "space", "general", "history"
  )

  /**
   * Validates if a question's real difficulty score (1–100) fits the target stage and question number.
   *
   * Rules:
   * Stages 1–5:
   *   Q1–Q3: Very Easy (1–20)
   *   Q4–Q8: Easy (21–40)
   *   Q9: Easy / Medium (20–50)
   *   Q10: Medium (35–60)
   *   No Hard (>60), Very Hard (>80), or Challenge (>95) allowed in stages 1–5.
   *
   * Stages 6–10:
   *   Mostly Easy and Medium (max 65).
   *
   * Stages 11–20:
   *   Mostly Medium, with some Hard near the end (max 78).
   *
   * Stages 21–50:
   *   Gradually introduce Hard questions, max 90.
   *
   * Stages 51+:
   *   Allows progressive increase up to 100.
   */
  fun validateQuestionDifficulty(question: Question, stage: Int, questionNumber: Int): Boolean {
    val score = question.difficultyScore
    if (score !in 1..100) return false

    return when {
      stage in 1..5 -> {
        when (questionNumber) {
          in 1..3 -> score in 1..20
          in 4..8 -> score in 15..40
          9 -> score in 20..50
          10 -> score in 35..60
          else -> score <= 60
        }
      }
      stage in 6..10 -> {
        when (questionNumber) {
          in 1..4 -> score in 10..40
          in 5..8 -> score in 25..60
          in 9..10 -> score in 35..65
          else -> score <= 65
        }
      }
      stage in 11..20 -> {
        when (questionNumber) {
          in 1..4 -> score in 25..55
          in 5..8 -> score in 38..65
          in 9..10 -> score in 45..78
          else -> score <= 78
        }
      }
      stage in 21..50 -> {
        when (questionNumber) {
          in 1..4 -> score in 30..65
          in 5..7 -> score in 50..80
          in 8..10 -> score in 60..90
          else -> score <= 90
        }
      }
      else -> {
        // Stage 51+
        when (questionNumber) {
          in 1..2 -> score in 45..75
          in 3..5 -> score in 60..88
          in 6..8 -> score in 70..95
          in 9..10 -> score in 80..100
          else -> true
        }
      }
    }
  }

  /**
   * Comprehensive validation for playable questions (curated or procedurally/AI generated):
   * 1. Answer verification
   * 2. Difficulty verification
   * 3. Language verification
   * 4. Ambiguity check
   * 5. Duplicate check
   * 6. Category validation & variety
   */
  fun validateQuestion(
    question: Question,
    stage: Int,
    questionNumber: Int,
    existingQuestionsInStage: List<Question>
  ): QuestionValidationResult {
    // 1. Answer verification
    if (question.acceptedAnswers.isEmpty()) {
      return QuestionValidationResult.Invalid("No accepted answers provided")
    }
    for (ans in question.acceptedAnswers) {
      if (ans.isBlank()) {
        return QuestionValidationResult.Invalid("Accepted answer contains blank entries")
      }
    }
    if (question.answerType in listOf(AnswerType.NUMBER, AnswerType.DECIMAL, AnswerType.YEAR)) {
      val num = question.correctNumericValue
      if (num == null || num.isNaN() || num.isInfinite()) {
        return QuestionValidationResult.Invalid("Numeric question missing valid correctNumericValue")
      }
    }

    // 2. Difficulty verification
    if (!validateQuestionDifficulty(question, stage, questionNumber)) {
      return QuestionValidationResult.Invalid(
        "Question difficulty score (${question.difficultyScore}) invalid for stage $stage, Q$questionNumber"
      )
    }

    // 3. Language verification
    if (question.language !in listOf("en", "ar", "es", "fr")) {
      return QuestionValidationResult.Invalid("Unsupported language code: ${question.language}")
    }
    if (question.question.isBlank() || question.question.trim().length < 5) {
      return QuestionValidationResult.Invalid("Question text is too short or blank")
    }
    if (question.language == "ar") {
      val hasArabic = question.question.any { it in '\u0600'..'\u06FF' }
      if (!hasArabic) {
        return QuestionValidationResult.Invalid("Arabic question missing Arabic script")
      }
    }
    if (question.question.contains("TODO") ||
      question.question.contains("[Insert") ||
      question.question.contains("{placeholder}") ||
      question.question.contains("Lorem ipsum")
    ) {
      return QuestionValidationResult.Invalid("Question contains placeholder tokens")
    }

    // 4. Ambiguity check
    val trimmed = question.question.trim()
    val isAmbiguous = trimmed.equals("What is it?", ignoreCase = true) ||
      trimmed.equals("Guess the answer", ignoreCase = true) ||
      (trimmed.length < 7 && !trimmed.contains("+") && !trimmed.contains("-") && !trimmed.contains("×") && !trimmed.contains("*"))
    if (isAmbiguous) {
      return QuestionValidationResult.Invalid("Question text is ambiguous or unspecific")
    }

    // 5. Concept, text, and answer duplicate check
    if (existingQuestionsInStage.any { it.id == question.id }) {
      return QuestionValidationResult.Invalid("Duplicate question ID: ${question.id}")
    }
    if (existingQuestionsInStage.any { it.effectiveConceptId.equals(question.effectiveConceptId, ignoreCase = true) }) {
      return QuestionValidationResult.Invalid("Duplicate concept in stage: ${question.effectiveConceptId}")
    }
    val normalizedQ = question.question.trim().lowercase()
    if (existingQuestionsInStage.any { it.question.trim().lowercase() == normalizedQ }) {
      return QuestionValidationResult.Invalid("Duplicate question text in stage: ${question.question}")
    }
    if (question.primaryAnswer.isNotBlank() && existingQuestionsInStage.any { it.primaryAnswer.equals(question.primaryAnswer, ignoreCase = true) }) {
      return QuestionValidationResult.Invalid("Duplicate primary answer in stage: ${question.primaryAnswer}")
    }

    // 6. Category validation & variety
    val normalizedCategory = question.category.trim().lowercase()
    if (normalizedCategory !in VALID_CATEGORIES) {
      return QuestionValidationResult.Invalid("Invalid category: ${question.category}")
    }
    // Limit same category to at most 2 per stage to guarantee natural variety
    val categoryCount = existingQuestionsInStage.count { it.category.trim().lowercase() == normalizedCategory }
    if (categoryCount >= 2 && existingQuestionsInStage.size < 9) {
      return QuestionValidationResult.Invalid(
        "Stage already contains max allowed ($categoryCount) questions for category $normalizedCategory"
      )
    }

    return QuestionValidationResult.Valid
  }

  /**
   * Validates a generated or imported question before it can enter the playable pool.
   * Rejects if:
   * - Concept already exists in the same difficulty pool
   * - Question text or ID is duplicate
   * - Category is invalid
   * - Answers are ambiguous or invalid
   */
  fun validateNewQuestionForPool(
    candidate: Question,
    existingPool: Collection<Question>
  ): QuestionValidationResult {
    if (candidate.acceptedAnswers.isEmpty() || candidate.acceptedAnswers.any { it.isBlank() }) {
      return QuestionValidationResult.Invalid("Candidate has invalid or empty accepted answers")
    }
    if (candidate.question.isBlank() || candidate.question.trim().length < 5) {
      return QuestionValidationResult.Invalid("Candidate question is too short")
    }
    val category = candidate.category.trim().lowercase()
    if (category !in VALID_CATEGORIES) {
      return QuestionValidationResult.Invalid("Candidate category $category is not recognized")
    }
    if (candidate.difficultyScore !in 1..100) {
      return QuestionValidationResult.Invalid("Candidate difficulty score ${candidate.difficultyScore} out of range 1..100")
    }
    // Check concept duplication within the same difficulty tier
    val candidateConcept = candidate.effectiveConceptId
    val duplicateConcept = existingPool.any {
      it.effectiveConceptId.equals(candidateConcept, ignoreCase = true) &&
        it.difficultyTier == candidate.difficultyTier
    }
    if (duplicateConcept) {
      return QuestionValidationResult.Invalid("Concept $candidateConcept already exists in ${candidate.difficultyTier} pool")
    }
    val duplicateText = existingPool.any {
      it.question.trim().equals(candidate.question.trim(), ignoreCase = true)
    }
    if (duplicateText) {
      return QuestionValidationResult.Invalid("Question text already exists in pool")
    }
    return QuestionValidationResult.Valid
  }
}
