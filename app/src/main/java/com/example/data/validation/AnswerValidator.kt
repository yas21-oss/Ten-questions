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

  private val RESERVED_ARABIC_AL_WORDS = setOf(
    "الله", "اله", "الذي", "التي", "الذين", "اللذان", "اللتان", "اللواتي", "اللاتي", "الان"
  )

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
    val noPunctInput = stripPunctuation(cleanInput)
    val inputIsArabic = containsArabic(cleanInput)
    val inputArabicForms = if (inputIsArabic) getArabicEquivalenceForms(cleanInput) else emptySet()

    for (accepted in question.acceptedAnswers) {
      val cleanTarget = safeNormalize(accepted)
      val noPunctTarget = stripPunctuation(cleanTarget)
      val targetIsArabic = containsArabic(cleanTarget)

      // Step 2a: Exact normalized match
      if (cleanInput == cleanTarget) {
        return ValidationResult(
          isCorrect = true,
          xpEarned = 100,
          message = "Correct! +100 XP"
        )
      }

      // Step 2b: Minor punctuation / symbol variance
      if (noPunctInput == noPunctTarget && noPunctInput.isNotEmpty()) {
        return ValidationResult(
          isCorrect = true,
          xpEarned = 95,
          message = "Correct! +95 XP"
        )
      }

      // Step 2c: Arabic orthographic & grammatical normalization
      if (inputIsArabic || targetIsArabic) {
        val targetArabicForms = getArabicEquivalenceForms(cleanTarget)
        if (inputArabicForms.intersect(targetArabicForms).isNotEmpty()) {
          return ValidationResult(
            isCorrect = true,
            xpEarned = 95,
            message = "Correct! +95 XP"
          )
        }
      }

      // Step 2d: Typo tolerance: strictly for non-Arabic textual words of length >= 5.
      // NEVER allow typo tolerance on purely numeric strings, dates/years, or Arabic words!
      val isNumericTarget = cleanTarget.all { it.isDigit() || it == '.' || it == '-' }
      val isNumericInput = cleanInput.all { it.isDigit() || it == '.' || it == '-' }

      if (!inputIsArabic && !targetIsArabic && !isNumericTarget && !isNumericInput && cleanTarget.length >= 5) {
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

    val noPunctInput = stripPunctuation(cleanInput)
    val inputIsArabic = containsArabic(cleanInput)
    val inputArabicForms = if (inputIsArabic) getArabicEquivalenceForms(cleanInput) else emptySet()

    for (accepted in acceptedAnswers) {
      val cleanTarget = safeNormalize(accepted)
      if (cleanInput == cleanTarget) return true

      val noPunctTarget = stripPunctuation(cleanTarget)
      if (noPunctInput == noPunctTarget && noPunctInput.isNotEmpty()) return true

      val targetIsArabic = containsArabic(cleanTarget)
      if (inputIsArabic || targetIsArabic) {
        val targetArabicForms = getArabicEquivalenceForms(cleanTarget)
        if (inputArabicForms.intersect(targetArabicForms).isNotEmpty()) return true
      }

      val isNumericTarget = cleanTarget.all { it.isDigit() || it == '.' || it == '-' }
      val isNumericInput = cleanInput.all { it.isDigit() || it == '.' || it == '-' }

      if (!inputIsArabic && !targetIsArabic && !isNumericTarget && !isNumericInput && cleanTarget.length >= 5) {
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
      .replace(Regex("(°|ق\\.م|سنة|عام|يوم|يوما|يوماً|أيام|درجة|درجات|أوتار)"), "")
      .replace(" ", "")
      .trim()

    return cleaned.toDoubleOrNull()
  }

  /**
   * Safe normalization:
   * Normalizes casing, accidental whitespace, Latin diacritics, and Arabic harakat.
   * Preserves canonical Unicode form (NFC).
   */
  fun safeNormalize(raw: String): String {
    if (raw.isBlank()) return ""

    // 1. NFKC normalization: resolves ligatures and presentation forms to canonical Arabic letters
    val nfkc = Normalizer.normalize(raw.trim(), Normalizer.Form.NFKC)

    // 2. Decompose Latin accents and strip combining diacritical marks (\u0300-\u036F)
    val decomposed = Normalizer.normalize(nfkc, Normalizer.Form.NFD)
    val noLatinMarks = decomposed.replace("[\\u0300-\\u036F]+".toRegex(), "")
    var text = Normalizer.normalize(noLatinMarks, Normalizer.Form.NFC)

    // 3. Remove Arabic harakat (tashkeel), tatweel, Quranic marks
    text = removeArabicDiacritics(text)

    // 4. Normalize digits
    text = normalizeDigits(text)

    // 5. Lowercase for case-insensitive matching
    text = text.lowercase(Locale.ROOT)

    // 6. Collapse consecutive whitespace into single space
    return text.replace("\\s+".toRegex(), " ").trim()
  }

  /**
   * Removes Arabic harakat (tashkeel), tanween, shadda, sukun, tatweel/kashida,
   * Quranic annotation signs, and invisible directional markers.
   */
  fun removeArabicDiacritics(text: String): String {
    return text.replace("[\u0610-\u061A\u064B-\u065F\u0670\u06D6-\u06ED\u0640\u200B-\u200F\uFEFF]".toRegex(), "")
  }

  /**
   * Normalizes standard Arabic orthographic keyboard variations:
   * - Alef variants: أ, إ, آ, ٱ, ٲ, ٳ, ٵ -> ا
   * - Teh Marbuta -> Heh: ة, ۃ -> ه
   * - Alif Maqsura -> Yeh: ى, ی, ے -> ي
   */
  fun arabicOrthographyNormalize(text: String): String {
    var s = removeArabicDiacritics(text)
    s = s.replace("[أإآٱٲٳٵ]".toRegex(), "ا")
    s = s.replace("[ةۃ]".toRegex(), "ه")
    s = s.replace("[ىیے]".toRegex(), "ي")
    return s
  }

  /**
   * Strips the Arabic definite article "ال" from a single word if:
   * 1. Word is not a reserved non-article word (e.g. الله, الذي, التي, الان)
   * 2. Word starts with "ال" and has at least 2 remaining Arabic root letters
   */
  fun stripArabicDefiniteArticleFromWord(word: String): String {
    if (word in RESERVED_ARABIC_AL_WORDS) return word

    if (word.startsWith("ال") && word.length >= 4) {
      val remainder = word.substring(2)
      val arabicLetterCount = remainder.count { it in '\u0620'..'\u064A' }
      if (arabicLetterCount >= 2) {
        return remainder
      }
    }
    return word
  }

  /**
   * Strips the Arabic definite article "ال" across words in an answer phrase.
   */
  fun stripArabicDefiniteArticle(phrase: String): String {
    val words = phrase.split("\\s+".toRegex())
    return words.joinToString(" ") { stripArabicDefiniteArticleFromWord(it) }
  }

  /**
   * Generates equivalence forms for an Arabic answer candidate.
   * Handles:
   * - Base orthography normalization (Alef, Teh Marbuta, Alif Maqsura)
   * - Hamza carrier unification (ؤ, ئ -> ء)
   * - Phonological vowel representation (ئ -> ي, ؤ -> و)
   * - Optional Arabic definite article "ال"
   */
  fun getArabicEquivalenceForms(text: String): Set<String> {
    if (text.isBlank()) return emptySet()
    val clean = stripPunctuation(removeArabicDiacritics(text))
    if (clean.isBlank()) return emptySet()

    val base = arabicOrthographyNormalize(clean)
    if (base.isBlank()) return emptySet()

    val forms = mutableSetOf<String>()
    forms.add(base)

    // Form 1: Unified hamza carrier (ؤ, ئ -> ء) e.g. مسؤول / مسئول -> مسءول
    val hamzaUnified = base.replace("[ؤئ]".toRegex(), "ء")
    forms.add(hamzaUnified)

    // Form 2: Phonological vowel representation (ئ -> ي, ؤ -> و) e.g. شاطئ / شاطي, تفاؤل / تفاول
    val vowelUnified = base.replace("ئ", "ي").replace("ؤ", "و")
    forms.add(vowelUnified)

    // Generate definite article "ال" stripped forms for all variants
    val strippedForms = forms.map { stripArabicDefiniteArticle(it) }.filter { it.isNotBlank() }
    forms.addAll(strippedForms)

    return forms
  }

  fun containsArabic(text: String): Boolean {
    return text.any { ch ->
      ch in '\u0600'..'\u06FF' ||
      ch in '\u0750'..'\u077F' ||
      ch in '\u08A0'..'\u08FF' ||
      ch in '\uFB50'..'\uFDFF' ||
      ch in '\uFE70'..'\uFEFF'
    }
  }

  fun stripPunctuation(text: String): String {
    return text.replace("[.,/#!$%^&*;:{}=\\-_`~()\"'“”‘’\\[\\]?¿!¡\u060C\u061B\u061F]".toRegex(), "")
      .replace("\\s+".toRegex(), " ")
      .trim()
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
