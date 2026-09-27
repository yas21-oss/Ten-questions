package com.example.data.questions

import com.example.data.model.AppLanguage
import com.example.data.model.DifficultyTier
import com.example.data.model.Question
import com.example.data.validation.QuestionValidator
import kotlin.random.Random

object QuestionBank {

  const val QUESTION_POOL_VERSION = 1

  // Global question repository per language: Merges curated language files with KnowledgeCatalog & Advanced Catalogs
  val curatedQuestions: Map<String, List<Question>> by lazy {
    mapOf(
      "en" to (EnglishQuestions.list +
        KnowledgeCatalog.getCatalog(AppLanguage.ENGLISH) +
        AdvancedMediumCatalog.getCatalog(AppLanguage.ENGLISH) +
        AdvancedHardCatalog.getCatalog(AppLanguage.ENGLISH) +
        AdvancedExpertCatalog.getCatalog(AppLanguage.ENGLISH) +
        HighDifficultyBalancePart1.getQuestions(AppLanguage.ENGLISH) +
        HighDifficultyBalancePart2.getQuestions(AppLanguage.ENGLISH)).distinctBy { it.id },
      "ar" to (ArabicQuestions.list +
        KnowledgeCatalog.getCatalog(AppLanguage.ARABIC) +
        AdvancedMediumCatalog.getCatalog(AppLanguage.ARABIC) +
        AdvancedHardCatalog.getCatalog(AppLanguage.ARABIC) +
        AdvancedExpertCatalog.getCatalog(AppLanguage.ARABIC) +
        HighDifficultyBalancePart1.getQuestions(AppLanguage.ARABIC) +
        HighDifficultyBalancePart2.getQuestions(AppLanguage.ARABIC)).distinctBy { it.id },
      "es" to (SpanishQuestions.list +
        KnowledgeCatalog.getCatalog(AppLanguage.SPANISH) +
        AdvancedMediumCatalog.getCatalog(AppLanguage.SPANISH) +
        AdvancedHardCatalog.getCatalog(AppLanguage.SPANISH) +
        AdvancedExpertCatalog.getCatalog(AppLanguage.SPANISH) +
        HighDifficultyBalancePart1.getQuestions(AppLanguage.SPANISH) +
        HighDifficultyBalancePart2.getQuestions(AppLanguage.SPANISH)).distinctBy { it.id },
      "fr" to (FrenchQuestions.list +
        KnowledgeCatalog.getCatalog(AppLanguage.FRENCH) +
        AdvancedMediumCatalog.getCatalog(AppLanguage.FRENCH) +
        AdvancedHardCatalog.getCatalog(AppLanguage.FRENCH) +
        AdvancedExpertCatalog.getCatalog(AppLanguage.FRENCH) +
        HighDifficultyBalancePart1.getQuestions(AppLanguage.FRENCH) +
        HighDifficultyBalancePart2.getQuestions(AppLanguage.FRENCH)).distinctBy { it.id }
    )
  }

  /**
   * Generates a deterministic, personalized seed for a player's stage attempt.
   */
  fun computePlayerStageSeed(
    playerId: String,
    stageId: Int,
    poolVersion: Int = QUESTION_POOL_VERSION,
    attemptIndex: Int = 0
  ): Long {
    val key = "player_${playerId}_stage_${stageId}_v${poolVersion}_att_${attemptIndex}"
    var h = 1125899906842597L
    for (c in key) {
      h = 31L * h + c.code
    }
    return h
  }

  /**
   * Returns 10 personalized, difficulty-calibrated, and semantically unique questions
   * for the given player, stage, and language.
   */
  fun getQuestionsForStage(
    stage: Int,
    language: AppLanguage,
    historyTracker: QuestionHistoryTracker? = null,
    playerId: String = "default_player",
    seed: Long? = null,
    poolVersion: Int = QUESTION_POOL_VERSION,
    attemptIndex: Int = 0
  ): List<Question> {
    val langCode = language.code
    val rawPool = curatedQuestions[langCode] ?: curatedQuestions["en"] ?: EnglishQuestions.list

    val assignmentSeed = seed ?: computePlayerStageSeed(playerId, stage, poolVersion, attemptIndex)
    val rng = Random(assignmentSeed)

    val result = mutableListOf<Question>()
    val stageCategoryPlan = getTargetCategoryOrder(stage, rng)

    val seenQuestions = historyTracker?.getSeenQuestionIds() ?: emptySet()
    val seenConcepts = historyTracker?.getSeenConceptIds() ?: emptySet()

    for (qNum in 1..10) {
      val targetRange = getTargetDifficultyRange(stage, qNum)
      val targetCategory = stageCategoryPlan.getOrNull(qNum - 1)

      // 1. Filter candidates by score and ensure not already in this stage
      val validCandidates = rawPool.filter { candidate ->
        candidate.difficultyScore in targetRange &&
          result.none {
            it.id == candidate.id ||
              it.effectiveConceptId.equals(candidate.effectiveConceptId, ignoreCase = true) ||
              (it.primaryAnswer.isNotBlank() && it.primaryAnswer.equals(candidate.primaryAnswer, ignoreCase = true))
          } &&
          QuestionValidator.validateQuestionDifficulty(candidate, stage, qNum)
      }

      // 2. Partition by global player history (unseen concepts first)
      val unseenCandidates = validCandidates.filter { candidate ->
        candidate.id !in seenQuestions && candidate.effectiveConceptId !in seenConcepts
      }

      val candidatePool = if (unseenCandidates.isNotEmpty()) unseenCandidates else validCandidates

      // 3. Score candidates based on:
      // - matches target natural category (+10)
      // - underrepresented category in current stage selection (+6 for 0, +2 for 1)
      // - does not repeat recent answers (+5)
      // Group by priority tier so targetCategory/underrepresented categories are chosen first,
      // with random shuffling within each tier to prevent deterministic ordering bias.
      val sortedCandidates = candidatePool.sortedByDescending { candidate ->
        var score = 0
        if (targetCategory != null && candidate.category.equals(targetCategory, ignoreCase = true)) {
          score += 10
        }
        val currentCategoryCount = result.count { it.category.equals(candidate.category, ignoreCase = true) }
        if (currentCategoryCount == 0) {
          score += 6
        } else if (currentCategoryCount == 1) {
          score += 2
        }
        val isRecentAnswer = historyTracker?.isAnswerRecent(candidate.primaryAnswer, 15) == true
        if (!isRecentAnswer) {
          score += 5
        }
        score
      }

      var selected: Question? = null
      val groupedByScore = sortedCandidates.groupBy { candidate ->
        var s = 0
        if (targetCategory != null && candidate.category.equals(targetCategory, ignoreCase = true)) {
          s += 10
        }
        val count = result.count { it.category.equals(candidate.category, ignoreCase = true) }
        if (count == 0) s += 6 else if (count == 1) s += 2
        val isRecent = historyTracker?.isAnswerRecent(candidate.primaryAnswer, 15) == true
        if (!isRecent) s += 5
        s
      }
      val sortedScoreKeys = groupedByScore.keys.sortedDescending()
      for (scoreKey in sortedScoreKeys) {
        val tierCandidates = (groupedByScore[scoreKey] ?: emptyList()).shuffled(Random(rng.nextLong()))
        for (candidate in tierCandidates) {
          val validation = QuestionValidator.validateQuestion(candidate, stage, qNum, result)
          if (validation.isValid) {
            selected = candidate
            break
          }
        }
        if (selected != null) break
      }

      // 4. Fallback to procedural generator if pool exhausted or no candidate meets criteria
      if (selected == null) {
        val desiredScore = (targetRange.first + targetRange.last) / 2
        val fallbackCategory = targetCategory ?: listOf("mathematics", "science", "space", "technology", "history", "everyday", "geography", "animals")[rng.nextInt(8)]
        var proceduralCandidate = ProceduralQuestionGenerator.generate(
          stage = stage,
          questionNumber = qNum,
          targetScore = desiredScore,
          language = language,
          category = fallbackCategory,
          seed = rng.nextLong()
        )
        // Ensure procedural candidate has never been seen
        var attempt = 0
        while (
          (seenConcepts.contains(proceduralCandidate.effectiveConceptId) ||
            result.any { it.effectiveConceptId.equals(proceduralCandidate.effectiveConceptId, ignoreCase = true) }) &&
          attempt < 50
        ) {
          attempt++
          proceduralCandidate = ProceduralQuestionGenerator.generate(
            stage = stage + attempt * 23,
            questionNumber = qNum,
            targetScore = desiredScore,
            language = language,
            category = "mathematics",
            seed = rng.nextLong() + attempt * 10007L
          )
        }
        selected = proceduralCandidate
      }

      result.add(selected)
    }

    // Record stage questions into player history
    historyTracker?.recordStage(result, stage)

    return result
  }

  fun getTargetDifficultyRange(stage: Int, questionNumber: Int): IntRange {
    return when {
      stage in 1..5 -> when (questionNumber) {
        1 -> 1..15    // Very Easy
        2 -> 3..18    // Very Easy
        3 -> 5..20    // Very Easy
        4 -> 21..32   // Easy
        5 -> 22..34   // Easy
        6 -> 24..36   // Easy
        7 -> 25..38   // Easy
        8 -> 26..40   // Easy
        9 -> 30..48   // Easy / Medium
        10 -> 41..58  // Medium
        else -> 10..30
      }
      stage in 6..10 -> when (questionNumber) {
        1, 2 -> 15..35   // Easy
        3, 4 -> 22..40   // Easy
        5, 6 -> 35..50   // Medium
        7, 8 -> 40..55   // Medium
        9, 10 -> 45..60  // Medium
        else -> 30..50
      }
      stage in 11..20 -> when (questionNumber) {
        1, 2 -> 25..45   // Easy/Medium
        3, 4, 5 -> 40..58 // Medium
        6, 7 -> 45..60   // Medium
        8 -> 52..65     // Medium
        9, 10 -> 61..75  // Hard
        else -> 40..60
      }
      stage in 21..50 -> when (questionNumber) {
        1, 2 -> 35..55   // Medium
        3, 4 -> 45..65   // Medium
        5, 6, 7 -> 55..75   // Medium/Hard
        8, 9, 10 -> 65..85 // Hard
        else -> 50..75
      }
      else -> when (questionNumber) {
        1, 2 -> 50..70
        3, 4, 5 -> 65..85
        6, 7, 8 -> 75..92
        9, 10 -> 85..100
        else -> 60..90
      }
    }
  }

  /**
   * Generates a dynamic, non-rigid category plan for each stage and player.
   */
  fun getTargetCategoryOrder(stage: Int, rng: Random): List<String> {
    val allCategories = listOf(
      "animals", "food", "science", "geography", "mathematics",
      "technology", "sports", "everyday", "language", "art", "space", "history"
    )
    val shuffled = allCategories.shuffled(Random(rng.nextLong()))

    val doubledCat1 = shuffled[0]
    val doubledCat2 = shuffled[1]

    val list = mutableListOf<String>()
    list.add(doubledCat1)
    list.add(doubledCat1) // 2 of first category
    list.add(doubledCat2)
    list.add(doubledCat2) // 2 of second category
    // remaining 6 single categories
    for (i in 2..7) {
      list.add(shuffled[i])
    }
    return list.shuffled(Random(rng.nextLong()))
  }

  // ==========================================
  // METRICS & REPORTING HELPERS
  // ==========================================

  fun getAllQuestions(): List<Question> {
    return curatedQuestions.values.flatten().distinctBy { it.id }
  }

  fun getTotalAvailableQuestions(): Int {
    return getAllQuestions().size
  }

  fun getTotalUniqueConcepts(): Int {
    return getAllQuestions().map { it.effectiveConceptId.lowercase() }.distinct().size
  }

  fun getQuestionsCountByLanguage(): Map<String, Int> {
    return curatedQuestions.mapValues { it.value.size }
  }

  fun getQuestionsCountByTier(): Map<DifficultyTier, Int> {
    return getAllQuestions().groupBy { it.difficultyTier }.mapValues { it.value.size }
  }

  fun getQuestionsCountByCategory(): Map<String, Int> {
    return getAllQuestions().groupBy { it.category.lowercase() }.mapValues { it.value.size }
  }
}
