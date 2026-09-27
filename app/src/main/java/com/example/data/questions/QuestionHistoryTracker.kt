package com.example.data.questions

import com.example.data.model.Question

/**
 * Tracks questions, concepts, and answers that the player has encountered
 * to guarantee no semantic or answer duplication across stages.
 */
class QuestionHistoryTracker {
  private val seenQuestionIds = mutableSetOf<String>()
  private val seenConceptIds = mutableSetOf<String>()
  private val recentAnswers = mutableListOf<String>()

  fun isQuestionSeen(questionId: String): Boolean = questionId in seenQuestionIds

  fun isConceptSeen(conceptId: String): Boolean = conceptId in seenConceptIds

  fun isAnswerRecent(answer: String, windowSize: Int = 15): Boolean {
    val normalized = answer.trim().lowercase()
    if (normalized.isBlank()) return false
    return recentAnswers.takeLast(windowSize).contains(normalized)
  }

  fun record(question: Question, stage: Int) {
    seenQuestionIds.add(question.id)
    seenConceptIds.add(question.effectiveConceptId)
    val ans = question.primaryAnswer
    if (ans.isNotBlank()) {
      recentAnswers.add(ans)
    }
  }

  fun recordStage(questions: List<Question>, stage: Int) {
    for (q in questions) {
      record(q, stage)
    }
  }

  fun getSeenQuestionIds(): Set<String> = seenQuestionIds.toSet()

  fun getSeenConceptIds(): Set<String> = seenConceptIds.toSet()

  fun getRecentAnswers(): List<String> = recentAnswers.toList()

  fun clear() {
    seenQuestionIds.clear()
    seenConceptIds.clear()
    recentAnswers.clear()
  }

  fun addExistingSeen(questionIds: Collection<String>, conceptIds: Collection<String>, answers: Collection<String>) {
    seenQuestionIds.addAll(questionIds)
    seenConceptIds.addAll(conceptIds)
    recentAnswers.addAll(answers)
  }
}
