package com.example.data.validation

import com.example.data.model.Question
import java.text.Normalizer
import java.util.Locale
import kotlin.math.abs
import kotlin.math.min

data class ValidationResult(
  val isCorrect: Boolean,
  val xpEarned: Int,
  val message: String
)

object AnswerValidator {

  fun evaluate(userAnswer: String, question: Question): ValidationResult {
    val rawInput = userAnswer.trim()
    if (rawInput.isBlank()) {
      return ValidationResult(isCorrect = false, xpEarned = 0, message = "Empty answer")
    }

    // 1. Numerical check if applicable
    if (question.correctNumericValue != null) {
      val parsedNum = parseNumber(rawInput)
      if (parsedNum != null) {
        val target = question.correctNumericValue
        val xp = calculateNumericAccuracyXp(parsedNum, target, question.tolerance)
        if (xp > 0) {
          return ValidationResult(
            isCorrect = true,
            xpEarned = xp.coerceIn(0, 100),
            message = if (xp == 100) "Perfect accuracy!" else "Accurate answer ($xp XP)"
          )
        }
      }
    }

    // 2. Textual matching against acceptedAnswers
    val cleanInput = safeNormalize(rawInput)
    val arabicInput = arabicOrthographyNormalize(cleanInput)
    val noPunctInput = stripPunctuation(cleanInput)

    for (accepted in question.acceptedAnswers) {
      val cleanTarget = safeNormalize(accepted)
      val arabicTarget = arabicOrthographyNormalize(cleanTarget)
      val noPunctTarget = stripPunctuation(cleanTarget)

      // Exact normalized match
      if (cleanInput == cleanTarget) {
        return ValidationResult(
          isCorrect = true,
          xpEarned = 100,
          message = "Correct! +100 XP"
        )
      }

      // Minor punctuation / symbol variance
      if (noPunctInput == noPunctTarget && noPunctInput.isNotEmpty()) {
        return ValidationResult(
          isCorrect = true,
          xpEarned = 95,
          message = "Correct! +95 XP"
        )
      }

      // Arabic orthography normalization (Alef variations, Teh Marbuta, Alif Maqsura)
      if (arabicInput == arabicTarget && arabicInput.isNotEmpty()) {
        return ValidationResult(
          isCorrect = true,
          xpEarned = 95,
          message = "Correct! +95 XP"
        )
      }

      // Typo tolerance: strictly for textual words of length >= 5.
      // NEVER allow typo tolerance on purely numeric strings or dates/years!
      val isNumericTarget = cleanTarget.all { it.isDigit() || it == '.' || it == '-' }
      val isNumericInput = cleanInput.all { it.isDigit() || it == '.' || it == '-' }

      if (!isNumericTarget && !isNumericInput && cleanTarget.length >= 5) {
        val dist = levenshteinDistance(cleanInput, cleanTarget)
        val maxAllowed = if (cleanTarget.length >= 8) 2 else 1
        if (dist <= maxAllowed && (dist.toDouble() / cleanTarget.length) <= 0.22) {
          val xp = if (dist == 1) 85 else 70
          return ValidationResult(
            isCorrect = true,
            xpEarned = xp,
            message = "Accepted with minor typo! +$xp XP"
          )
        }
      }
    }

    // Completely wrong answer
    return ValidationResult(
      isCorrect = false,
      xpEarned = 0,
      message = "Incorrect answer"
    )
  }

  fun isCorrect(userAnswer: String, acceptedAnswers: List<String>): Boolean {
    val cleanInput = safeNormalize(userAnswer)
    if (cleanInput.isEmpty()) return false

    val arabicInput = arabicOrthographyNormalize(cleanInput)
    val noPunctInput = stripPunctuation(cleanInput)

    for (accepted in acceptedAnswers) {
      val cleanTarget = safeNormalize(accepted)
      if (cleanInput == cleanTarget) return true

      val noPunctTarget = stripPunctuation(cleanTarget)
      if (noPunctInput == noPunctTarget && noPunctInput.isNotEmpty()) return true

      val arabicTarget = arabicOrthographyNormalize(cleanTarget)
      if (arabicInput == arabicTarget && arabicInput.isNotEmpty()) return true

      val isNumericTarget = cleanTarget.all { it.isDigit() || it == '.' || it == '-' }
      val isNumericInput = cleanInput.all { it.isDigit() || it == '.' || it == '-' }

      if (!isNumericTarget && !isNumericInput && cleanTarget.length >= 5) {
        val dist = levenshteinDistance(cleanInput, cleanTarget)
        val maxAllowed = if (cleanTarget.length >= 8) 2 else 1
        if (dist <= maxAllowed && (dist.toDouble() / cleanTarget.length) <= 0.22) {
          return true
        }
      }
    }
    return false
  }

  /**
   * Reusable numerical error/distance scoring algorithm.
   * Tolerates precision differences with graceful degradation:
   * e.g. for Pi (3.14159265...):
   * 3.14159 -> 100 XP
   * 3.14    -> 95 XP
   * 3.1     -> 80 XP
   * 3       -> 60 XP
   * 2       -> 0 XP
   */
  fun calculateNumericAccuracyXp(
    submitted: Double,
    target: Double,
    customTolerance: Double?
  ): Int {
    val diff = abs(submitted - target)
    val denom = if (abs(target) > 1e-9) abs(target) else 1.0
    val relativeError = diff / denom

    val maxAllowedError = customTolerance ?: 0.08

    if (relativeError > maxAllowedError) {
      return 0
    }

    val errorRatio = (relativeError / maxAllowedError).coerceIn(0.0, 1.0)
    val xp = when {
      errorRatio <= 0.001 -> 100
      errorRatio <= 0.015 -> 95
      errorRatio <= 0.20 -> 80
      errorRatio <= 0.65 -> 60
      errorRatio <= 1.00 -> 50
      else -> 0
    }
    return xp.coerceIn(0, 100)
  }

  /**
   * Normalizes Eastern Arabic-Indic digits (٠-٩) and Persian digits (۰-۹)
   * and decimal symbols into standard ASCII numerals.
   */
  fun normalizeDigits(text: String): String {
    val sb = StringBuilder(text.length)
    for (ch in text) {
      when (ch) {
        in '\u0660'..'\u0669' -> sb.append((ch.code - 0x0660 + '0'.code).toChar()) // Eastern Arabic
        in '\u06F0'..'\u06F9' -> sb.append((ch.code - 0x06F0 + '0'.code).toChar()) // Persian/Urdu
        '\u066B', '\u060C', ',' -> sb.append('.') // Decimal separators
        '\u066C' -> {} // Thousands separator (ignore)
        else -> sb.append(ch)
      }
    }
    return sb.toString()
  }

  fun parseNumber(text: String): Double? {
    // 1. Convert any Arabic-Indic or Persian digits
    val digitNormalized = normalizeDigits(text.trim())

    // 2. Strip common unit/date notations without removing minus signs or decimal points
    val cleaned = digitNormalized
      .replace(Regex("(?i)\\b(ad|bc|bce|ce|year|years|days|jours|dias|días|strings|degrees|deg|celsius|c|m|km|kg|cm|percent|%)\\b"), "")
      .replace(Regex("[°م|ق\\.م|سنة|عام|يوم|يوما|يوماً|أيام|درجة|درجات|أوتار]"), "")
      .replace(" ", "")
      .trim()

    return cleaned.toDoubleOrNull()
  }

  /**
   * Safe normalization:
   * Only normalizes casing, accidental whitespace, and diacritics.
   * NEVER strips arbitrary letters, syllables, or words.
   */
  fun safeNormalize(raw: String): String {
    // 1. Strip standard Unicode diacritics
    val decomposed = Normalizer.normalize(raw.trim(), Normalizer.Form.NFD)
    var text = decomposed.replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")

    // 2. Remove Arabic harakat (tashkeel) and tatweel without removing words or roots
    text = text.replace("[\u064B-\u0652\u0670\u0640]".toRegex(), "")

    // 3. Lowercase
    text = text.lowercase(Locale.ROOT)

    // 4. Collapse consecutive whitespace into single space
    return text.replace("\\s+".toRegex(), " ").trim()
  }

  /**
   * Arabic orthography standard normalization:
   * Handles common keyboard typing variations in Arabic:
   * - Alef with Hamza / Madda (أ, إ, آ, ٱ -> ا)
   * - Teh Marbuta to Heh (ة -> ه)
   * - Alif Maqsura to Yaa (ى -> ي)
   */
  fun arabicOrthographyNormalize(text: String): String {
    return text
      .replace("[أإآٱ]".toRegex(), "ا")
      .replace("ة", "ه")
      .replace("ى", "ي")
  }

  private fun stripPunctuation(text: String): String {
    return text.replace("[.,/#!$%^&*;:{}=\\-_`~()\"'“”‘’\\[\\]?¿!¡\u060C\u061B\u061F]".toRegex(), "").trim()
  }

  private fun levenshteinDistance(a: String, b: String): Int {
    val dp = Array(a.length + 1) { IntArray(b.length + 1) }

    for (i in 0..a.length) dp[i][0] = i
    for (j in 0..b.length) dp[0][j] = j

    for (i in 1..a.length) {
      for (j in 1..b.length) {
        val cost = if (a[i - 1] == b[j - 1]) 0 else 1
        dp[i][j] = min(
          min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
          dp[i - 1][j - 1] + cost
        )
      }
    }

    return dp[a.length][b.length]
  }
}
