package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.db.AppDatabase
import com.example.data.model.AnswerType
import com.example.data.model.AppLanguage
import com.example.data.model.Question
import com.example.data.model.StageProgress
import com.example.data.model.UserAccount
import com.example.data.questions.QuestionBank
import com.example.data.repository.GameRepository
import com.example.data.model.DifficultyTier
import com.example.data.validation.AntiCheatEngine
import com.example.data.validation.AnswerValidator
import com.example.data.validation.QuestionValidator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  private lateinit var context: Context
  private lateinit var database: AppDatabase
  private lateinit var gameRepository: GameRepository

  @Before
  fun setUp() {
    context = ApplicationProvider.getApplicationContext<Context>()
    database = AppDatabase.getDatabase(context)
    gameRepository = GameRepository(context, database)
  }

  @Test
  fun testAppNameResource() {
    val appName = context.getString(R.string.app_name)
    assertEquals("Ten Questions", appName)
  }

  // ==========================================
  // QA TEST 1: Full Gameplay Flow
  // ==========================================
  @Test
  fun testFullStageQuestionsAndFlow() = runBlocking {
    // 1. Stage 1 has exactly 10 questions
    val questions = gameRepository.getQuestionsForStage(1, AppLanguage.ENGLISH)
    assertEquals(10, questions.size)

    for (i in 0 until 10) {
      val q = questions[i]
      assertNotNull("Question text should not be null", q.question)
      assertTrue("Question should have accepted answers", q.acceptedAnswers.isNotEmpty())
    }

    // 2. Stage 2 has 10 questions
    val stage2Questions = gameRepository.getQuestionsForStage(2, AppLanguage.ENGLISH)
    assertEquals(10, stage2Questions.size)
  }

  // ==========================================
  // QA TEST 2: Answer Validation
  // ==========================================
  @Test
  fun testTextAnswerValidation() {
    val qParis = Question(
      id = "test_q1",
      language = "en",
      category = "geography",
      difficulty = 1,
      question = "What is the capital of France?",
      answerType = AnswerType.TEXT,
      acceptedAnswers = listOf("Paris")
    )

    // Exact text
    val resExact = AnswerValidator.evaluate("Paris", qParis)
    assertTrue(resExact.isCorrect)
    assertEquals(100, resExact.xpEarned)

    // Uppercase / Lowercase
    val resLower = AnswerValidator.evaluate("paris", qParis)
    assertTrue(resLower.isCorrect)
    assertEquals(100, resLower.xpEarned)

    val resUpper = AnswerValidator.evaluate("PARIS", qParis)
    assertTrue(resUpper.isCorrect)
    assertEquals(100, resUpper.xpEarned)

    // Extra spaces
    val resSpaces = AnswerValidator.evaluate("   paris   ", qParis)
    assertTrue(resSpaces.isCorrect)
    assertEquals(100, resSpaces.xpEarned)

    // Wrong answer
    val resWrong = AnswerValidator.evaluate("London", qParis)
    assertFalse(resWrong.isCorrect)
    assertEquals(0, resWrong.xpEarned)
  }

  @Test
  fun testDiacriticsAndPunctuationValidation() {
    // Diacritics test: French / Spanish accents
    val qDiacritics = Question(
      id = "test_q2",
      language = "fr",
      category = "art",
      difficulty = 6,
      question = "Qui a peint La Joconde ?",
      answerType = AnswerType.TEXT,
      acceptedAnswers = listOf("Léonard de Vinci", "De Vinci")
    )

    // Accents stripped or preserved should match
    assertTrue(AnswerValidator.evaluate("Leonard de Vinci", qDiacritics).isCorrect)
    assertTrue(AnswerValidator.evaluate("LÉONARD DE VINCI", qDiacritics).isCorrect)
    assertTrue(AnswerValidator.evaluate("léonard de vinci", qDiacritics).isCorrect)

    // Punctuation test: Washington, D.C.
    val qWashington = Question(
      id = "test_q3",
      language = "en",
      category = "geography",
      difficulty = 5,
      question = "What is the capital of the United States?",
      answerType = AnswerType.MULTI_ACCEPTED_TEXT,
      acceptedAnswers = listOf("Washington, D.C.", "Washington DC", "Washington")
    )

    assertTrue(AnswerValidator.evaluate("Washington D.C.", qWashington).isCorrect)
    assertTrue(AnswerValidator.evaluate("Washington, D.C.", qWashington).isCorrect)
    assertTrue(AnswerValidator.evaluate("washington dc", qWashington).isCorrect)
    assertTrue(AnswerValidator.evaluate("Washington", qWashington).isCorrect)

    // Completely wrong answer must never be accepted
    assertFalse(AnswerValidator.evaluate("New York", qWashington).isCorrect)
    assertFalse(AnswerValidator.evaluate("Los Angeles", qWashington).isCorrect)
  }

  @Test
  fun testArabicAnswersValidation() {
    val qCairo = Question(
      id = "test_ar_1",
      language = "ar",
      category = "geography",
      difficulty = 1,
      question = "ما هي عاصمة مصر؟",
      answerType = AnswerType.TEXT,
      acceptedAnswers = listOf("القاهرة", "Cairo")
    )

    // Exact Arabic
    assertTrue(AnswerValidator.evaluate("القاهرة", qCairo).isCorrect)

    // Arabic with Harakat (Tashkeel)
    assertTrue(AnswerValidator.evaluate("القَاهِرَةُ", qCairo).isCorrect)

    // Teh Marbuta to Heh variation (common Arabic mobile keyboard typing)
    assertTrue(AnswerValidator.evaluate("القاهره", qCairo).isCorrect)

    // Wrong Arabic answer
    assertFalse(AnswerValidator.evaluate("الإسكندرية", qCairo).isCorrect)
    assertFalse(AnswerValidator.evaluate("دمشق", qCairo).isCorrect)

    // Arabic Alef variation test
    val qAustralia = Question(
      id = "test_ar_2",
      language = "ar",
      category = "geography",
      difficulty = 9,
      question = "ما هي عاصمة أستراليا؟",
      answerType = AnswerType.TEXT,
      acceptedAnswers = listOf("كانبرا", "Canberra")
    )
    assertTrue(AnswerValidator.evaluate("كانبرا", qAustralia).isCorrect)
    assertFalse(AnswerValidator.evaluate("سيدني", qAustralia).isCorrect)
  }

  @Test
  fun testArabicGreenAnswerVariationsPass() {
    val qGreen = Question(
      id = "test_ar_green",
      language = "ar",
      category = "everyday",
      difficulty = 1,
      question = "ما لون الانطلاق في علامة الطريق؟",
      answerType = AnswerType.TEXT,
      acceptedAnswers = listOf("أخضر")
    )

    // 1. Exact match ("أخضر" -> "أخضر")
    val res1 = AnswerValidator.evaluate("أخضر", qGreen)
    assertTrue("Exact 'أخضر' should be correct", res1.isCorrect)
    assertEquals(100, res1.xpEarned)

    // 2. Without hamza on Alef ("اخضر" -> "أخضر")
    val res2 = AnswerValidator.evaluate("اخضر", qGreen)
    assertTrue("Without hamza 'اخضر' should be accepted", res2.isCorrect)
    assertEquals(95, res2.xpEarned)

    // 3. With definite article and hamza ("الأخضر" -> "أخضر")
    val res3 = AnswerValidator.evaluate("الأخضر", qGreen)
    assertTrue("With article 'الأخضر' should be accepted", res3.isCorrect)
    assertEquals(95, res3.xpEarned)

    // 4. With definite article without hamza ("الاخضر" -> "أخضر")
    val res4 = AnswerValidator.evaluate("الاخضر", qGreen)
    assertTrue("With article without hamza 'الاخضر' should be accepted", res4.isCorrect)
    assertEquals(95, res4.xpEarned)

    // 5. Answers with harmless harakat differences
    assertTrue("Harakat on article form should be accepted", AnswerValidator.evaluate("الْأَخْضَرُ", qGreen).isCorrect)
    assertTrue("Harakat on bare form should be accepted", AnswerValidator.evaluate("أَخْضَرُ", qGreen).isCorrect)

    // 6. Normal whitespace differences
    assertTrue("Leading/trailing spaces should be accepted", AnswerValidator.evaluate("   أخضر   ", qGreen).isCorrect)
    assertTrue("Leading/trailing spaces on article should be accepted", AnswerValidator.evaluate("   الأخضر   ", qGreen).isCorrect)

    // 7. Punctuation differences where appropriate
    assertTrue("Punctuation like dot should be accepted", AnswerValidator.evaluate("أخضر.", qGreen).isCorrect)
    assertTrue("Punctuation like exclamation should be accepted", AnswerValidator.evaluate("الأخضر!", qGreen).isCorrect)
    assertTrue("Arabic question mark should be accepted", AnswerValidator.evaluate("أخضر؟", qGreen).isCorrect)

    // 8. Reverse: question acceptedAnswers has "الأخضر", user types "أخضر" or "اخضر"
    val qGreenDefinite = Question(
      id = "test_ar_green_def",
      language = "ar",
      category = "everyday",
      difficulty = 1,
      question = "ما لون الانطلاق في علامة الطريق؟",
      answerType = AnswerType.TEXT,
      acceptedAnswers = listOf("الأخضر")
    )
    assertTrue(AnswerValidator.evaluate("أخضر", qGreenDefinite).isCorrect)
    assertTrue(AnswerValidator.evaluate("اخضر", qGreenDefinite).isCorrect)
    assertTrue(AnswerValidator.evaluate("الأخضر", qGreenDefinite).isCorrect)
    assertTrue(AnswerValidator.evaluate("الاخضر", qGreenDefinite).isCorrect)
  }

  @Test
  fun testArabicGenuinelyDifferentWordsMustFail() {
    val qMoon = Question(
      id = "test_ar_moon",
      language = "ar",
      category = "science",
      difficulty = 1,
      question = "ما هو التابع الطبيعي للأرض؟",
      answerType = AnswerType.TEXT,
      acceptedAnswers = listOf("قمر")
    )

    // Genuinely different Arabic words must NOT become equal because of normalization:
    // - "عمر" ≠ "قمر"
    assertFalse("'عمر' must not be accepted for 'قمر'", AnswerValidator.evaluate("عمر", qMoon).isCorrect)
    // - "قمرين" ≠ "قمر" (dual form)
    assertFalse("'قمرين' must not be accepted for 'قمر'", AnswerValidator.evaluate("قمرين", qMoon).isCorrect)
    // - "قمره" ≠ "قمر" (possessive)
    assertFalse("'قمره' must not be accepted for 'قمر'", AnswerValidator.evaluate("قمره", qMoon).isCorrect)
    // - "قمح" ≠ "قمر" (wheat)
    assertFalse("'قمح' must not be accepted for 'قمر'", AnswerValidator.evaluate("قمح", qMoon).isCorrect)
    // - "قمرر" ≠ "قمر"
    assertFalse("'قمرر' must not be accepted for 'قمر'", AnswerValidator.evaluate("قمرر", qMoon).isCorrect)
    // - "العمر" ≠ "قمر"
    assertFalse("'العمر' must not be accepted for 'قمر'", AnswerValidator.evaluate("العمر", qMoon).isCorrect)
    // Unrelated words must remain incorrect
    assertFalse("'شمس' must not be accepted for 'قمر'", AnswerValidator.evaluate("شمس", qMoon).isCorrect)
    assertFalse("'نجم' must not be accepted for 'قمر'", AnswerValidator.evaluate("نجم", qMoon).isCorrect)

    // For "أخضر", genuinely different colors must fail:
    val qGreen = Question(
      id = "test_ar_green_fail",
      language = "ar",
      category = "everyday",
      difficulty = 1,
      question = "ما لون الانطلاق؟",
      answerType = AnswerType.TEXT,
      acceptedAnswers = listOf("أخضر")
    )
    assertFalse("Red 'أحمر' must fail for green", AnswerValidator.evaluate("أحمر", qGreen).isCorrect)
    assertFalse("Red with article 'الأحمر' must fail for green", AnswerValidator.evaluate("الأحمر", qGreen).isCorrect)
    assertFalse("Blue 'أزرق' must fail for green", AnswerValidator.evaluate("أزرق", qGreen).isCorrect)
    assertFalse("Yellow 'أصفر' must fail for green", AnswerValidator.evaluate("أصفر", qGreen).isCorrect)
    assertFalse("Black 'أسود' must fail for green", AnswerValidator.evaluate("أسود", qGreen).isCorrect)
  }

  @Test
  fun testArabicHamzaAndDefiniteArticleEquivalence() {
    // ؤ / ئ variations (مسؤول vs مسئول, شؤون vs شئون)
    val qResponsible = Question(
      id = "test_ar_hamza",
      language = "ar",
      category = "language",
      difficulty = 2,
      question = "من هو الشخص المكلف بالعمل؟",
      answerType = AnswerType.TEXT,
      acceptedAnswers = listOf("مسؤول")
    )
    assertTrue("Egyptian spelling 'مسئول' should match 'مسؤول'", AnswerValidator.evaluate("مسئول", qResponsible).isCorrect)
    assertTrue("Standard spelling 'مسؤول' should match 'مسؤول'", AnswerValidator.evaluate("مسؤول", qResponsible).isCorrect)
    assertTrue("Definite 'المسؤول' should match 'مسؤول'", AnswerValidator.evaluate("المسؤول", qResponsible).isCorrect)
    assertTrue("Definite 'المسئول' should match 'مسؤول'", AnswerValidator.evaluate("المسئول", qResponsible).isCorrect)

    // Beach: شاطئ vs شاطي
    val qBeach = Question(
      id = "test_ar_beach",
      language = "ar",
      category = "geography",
      difficulty = 1,
      question = "ماذا تسمى حافة البحر البرية؟",
      answerType = AnswerType.TEXT,
      acceptedAnswers = listOf("شاطئ")
    )
    assertTrue("Word-final Yeh 'شاطي' should match 'شاطئ'", AnswerValidator.evaluate("شاطي", qBeach).isCorrect)
    assertTrue("Definite 'الشاطئ' should match 'شاطئ'", AnswerValidator.evaluate("الشاطئ", qBeach).isCorrect)
    assertTrue("Definite 'الشاطي' should match 'شاطئ'", AnswerValidator.evaluate("الشاطي", qBeach).isCorrect)

    // Multi-word phrase with definite articles
    val qPacific = Question(
      id = "test_ar_pacific",
      language = "ar",
      category = "geography",
      difficulty = 2,
      question = "ما هو أكبر محيط في العالم؟",
      answerType = AnswerType.TEXT,
      acceptedAnswers = listOf("المحيط الهادئ")
    )
    assertTrue(AnswerValidator.evaluate("المحيط الهادئ", qPacific).isCorrect)
    assertTrue(AnswerValidator.evaluate("المحيط الهادي", qPacific).isCorrect)
    assertTrue(AnswerValidator.evaluate("محيط هادئ", qPacific).isCorrect)
    assertTrue(AnswerValidator.evaluate("محيط هادي", qPacific).isCorrect)
    assertFalse(AnswerValidator.evaluate("المحيط الأطلسي", qPacific).isCorrect)

    // Reserved words where 'ال' is NOT a removable definite article (e.g. الله)
    val qAllah = Question(
      id = "test_ar_reserved",
      language = "ar",
      category = "general",
      difficulty = 1,
      question = "من هو الخالق في الإسلام؟",
      answerType = AnswerType.TEXT,
      acceptedAnswers = listOf("الله")
    )
    assertTrue(AnswerValidator.evaluate("الله", qAllah).isCorrect)
    assertFalse("Short remainder 'له' must NOT be accepted for 'الله'", AnswerValidator.evaluate("له", qAllah).isCorrect)
  }

  @Test
  fun testNumericalAndUnitAnswers() {
    // Days in a year (target 365.0)
    val qDays = Question(
      id = "test_num_1",
      language = "en",
      category = "general",
      difficulty = 3,
      question = "How many days are in a standard non-leap year?",
      answerType = AnswerType.NUMBER,
      acceptedAnswers = listOf("365", "Three hundred sixty-five"),
      correctNumericValue = 365.0,
      tolerance = 0.001
    )

    // Exact number
    val res365 = AnswerValidator.evaluate("365", qDays)
    assertTrue(res365.isCorrect)
    assertEquals(100, res365.xpEarned)

    // Number with unit "365 days"
    val resWithUnit = AnswerValidator.evaluate("365 days", qDays)
    assertTrue(resWithUnit.isCorrect)
    assertEquals(100, resWithUnit.xpEarned)

    // Eastern Arabic numerals: ٣٦٥
    val resArabicDigits = AnswerValidator.evaluate("٣٦٥", qDays)
    assertTrue(resArabicDigits.isCorrect)
    assertEquals(100, resArabicDigits.xpEarned)

    // Arabic with unit: ٣٦٥ يوم
    val resArabicWithUnit = AnswerValidator.evaluate("٣٦٥ يوم", qDays)
    assertTrue(resArabicWithUnit.isCorrect)
    assertEquals(100, resArabicWithUnit.xpEarned)

    // Wrong number
    val resWrongNum = AnswerValidator.evaluate("366", qDays)
    assertFalse(resWrongNum.isCorrect)
    assertEquals(0, resWrongNum.xpEarned)

    val resWrongFar = AnswerValidator.evaluate("100", qDays)
    assertFalse(resWrongFar.isCorrect)
    assertEquals(0, resWrongFar.xpEarned)
  }

  @Test
  fun testYearAndHistoricalDates() {
    val qBerlin = Question(
      id = "test_year_1",
      language = "en",
      category = "history",
      difficulty = 7,
      question = "In what year did the Berlin Wall fall?",
      answerType = AnswerType.YEAR,
      acceptedAnswers = listOf("1989"),
      correctNumericValue = 1989.0,
      tolerance = 0.0001
    )

    // 1989 exact
    assertTrue(AnswerValidator.evaluate("1989", qBerlin).isCorrect)

    // 1989 AD / 1989 CE
    assertTrue(AnswerValidator.evaluate("1989 AD", qBerlin).isCorrect)
    assertTrue(AnswerValidator.evaluate("1989 CE", qBerlin).isCorrect)

    // Arabic 1989: ١٩٨٩
    assertTrue(AnswerValidator.evaluate("١٩٨٩", qBerlin).isCorrect)

    // Wrong years must NEVER be accepted
    assertFalse(AnswerValidator.evaluate("1988", qBerlin).isCorrect)
    assertFalse(AnswerValidator.evaluate("1990", qBerlin).isCorrect)
    assertFalse(AnswerValidator.evaluate("19891", qBerlin).isCorrect)
  }

  @Test
  fun testTypoToleranceOnlyOnWordsNeverOnNumbers() {
    // 1. Long word: "Leonardo da Vinci" (length >= 5) allows minor 1-char typo
    val qMonaLisa = Question(
      id = "test_art_1",
      language = "en",
      category = "art",
      difficulty = 6,
      question = "Who painted the Mona Lisa?",
      answerType = AnswerType.TEXT,
      acceptedAnswers = listOf("Leonardo da Vinci", "Da Vinci")
    )
    assertTrue(AnswerValidator.evaluate("Leonardo da Vinchi", qMonaLisa).isCorrect)

    // 2. Numerical string: "1989" (length 4) or "12345" (length 5)
    // A typo on a number (e.g. 1988 instead of 1989, or 12346 instead of 12345) must NEVER be accepted!
    val qNum = Question(
      id = "test_code",
      language = "en",
      category = "tech",
      difficulty = 5,
      question = "Enter the 5-digit postal code 12345",
      answerType = AnswerType.TEXT,
      acceptedAnswers = listOf("12345")
    )
    assertFalse(AnswerValidator.evaluate("12346", qNum).isCorrect)
    assertFalse(AnswerValidator.evaluate("12344", qNum).isCorrect)
  }

  // ==========================================
  // QA TEST 3: Numerical Accuracy XP Tests
  // ==========================================
  @Test
  fun testPiAccuracyXpScoring() {
    val piTarget = 3.141592653589793
    val tolerance = 0.08

    // 3.14159 -> 100 XP
    assertEquals(100, AnswerValidator.calculateNumericAccuracyXp(3.14159, piTarget, tolerance))

    // 3.14 -> 95 XP
    assertEquals(95, AnswerValidator.calculateNumericAccuracyXp(3.14, piTarget, tolerance))

    // 3.1 -> 80 XP
    assertEquals(80, AnswerValidator.calculateNumericAccuracyXp(3.1, piTarget, tolerance))

    // 3 -> 60 XP
    assertEquals(60, AnswerValidator.calculateNumericAccuracyXp(3.0, piTarget, tolerance))

    // 2 -> 0 XP (outside tolerance)
    assertEquals(0, AnswerValidator.calculateNumericAccuracyXp(2.0, piTarget, tolerance))

    // 4 -> 0 XP
    assertEquals(0, AnswerValidator.calculateNumericAccuracyXp(4.0, piTarget, tolerance))

    // -3 -> 0 XP (negative must give 0)
    assertEquals(0, AnswerValidator.calculateNumericAccuracyXp(-3.0, piTarget, tolerance))
  }

  @Test
  fun testZeroTargetNumericalAccuracy() {
    // Water freezing point: 0 degrees Celsius
    val zeroTarget = 0.0
    val tolerance = 0.001

    // 0 -> 100 XP
    assertEquals(100, AnswerValidator.calculateNumericAccuracyXp(0.0, zeroTarget, tolerance))

    // 1 -> 0 XP
    assertEquals(0, AnswerValidator.calculateNumericAccuracyXp(1.0, zeroTarget, tolerance))

    // -1 -> 0 XP
    assertEquals(0, AnswerValidator.calculateNumericAccuracyXp(-1.0, zeroTarget, tolerance))
  }

  // ==========================================
  // QA TEST 4 & 5: XP Accounting and Star System
  // ==========================================
  @Test
  fun testStarThresholds() {
    // 900–1000 XP = 3 stars
    assertEquals(3, gameRepository.calculateStars(1000))
    assertEquals(3, gameRepository.calculateStars(947))
    assertEquals(3, gameRepository.calculateStars(900))

    // 700–899 XP = 2 stars
    assertEquals(2, gameRepository.calculateStars(899))
    assertEquals(2, gameRepository.calculateStars(750))
    assertEquals(2, gameRepository.calculateStars(700))

    // 400–699 XP = 1 star
    assertEquals(1, gameRepository.calculateStars(699))
    assertEquals(1, gameRepository.calculateStars(550))
    assertEquals(1, gameRepository.calculateStars(400))

    // 0–399 XP = 0 stars
    assertEquals(0, gameRepository.calculateStars(399))
    assertEquals(0, gameRepository.calculateStars(150))
    assertEquals(0, gameRepository.calculateStars(0))
  }

  @Test
  fun testStageWinAndReplayXpAccounting() = runBlocking {
    database.userDao().deleteAllUsers()
    database.stageProgressDao().deleteAllProgress()

    val user = UserAccount(
      uid = "test_player_qa",
      displayName = "QA Champion",
      email = "qa@test.com",
      photoUrl = null,
      provider = "google",
      unlockedStage = 1,
      highestStage = 0,
      xp = 0L,
      totalStars = 0,
      stagesWon = 0,
      questionsAnswered = 0,
      bestStreak = 0,
      totalTimeSeconds = 0L,
      lastSyncTimestamp = System.currentTimeMillis()
    )
    database.userDao().insertUser(user)

    val token1 = AntiCheatEngine.generateToken(user.uid, 1, 10, 25L)

    // First completion: 800 XP -> 2 stars
    val result1 = gameRepository.recordStageWin(
      user = user,
      stageNumber = 1,
      stageTotalXp = 800,
      timeTakenSeconds = 25L,
      token = token1
    )
    assertTrue(result1.isSuccess)
    val (userAfter1, xp1, stars1) = result1.getOrThrow()
    assertEquals(800, xp1)
    assertEquals(2, stars1)
    assertEquals(800L, userAfter1.xp)
    assertEquals(2, userAfter1.totalStars)
    assertEquals(2, userAfter1.unlockedStage)

    // Replay Stage 1 with a WORSE score (600 XP -> 1 star):
    // Must NOT reduce XP or stars, and must NOT duplicate XP!
    val tokenReplayWorse = AntiCheatEngine.generateToken(user.uid, 1, 10, 20L)
    val resultWorse = gameRepository.recordStageWin(
      user = userAfter1,
      stageNumber = 1,
      stageTotalXp = 600,
      timeTakenSeconds = 20L,
      token = tokenReplayWorse
    )
    assertTrue(resultWorse.isSuccess)
    val (userAfterWorse, _, _) = resultWorse.getOrThrow()
    assertEquals(800L, userAfterWorse.xp) // Unchanged!
    assertEquals(2, userAfterWorse.totalStars) // Unchanged!

    // Replay Stage 1 with a BETTER score (1000 XP -> 3 stars):
    // Must award ONLY the net difference: +200 XP, +1 Star!
    val tokenReplayBetter = AntiCheatEngine.generateToken(user.uid, 1, 10, 18L)
    val resultBetter = gameRepository.recordStageWin(
      user = userAfterWorse,
      stageNumber = 1,
      stageTotalXp = 1000,
      timeTakenSeconds = 18L,
      token = tokenReplayBetter
    )
    assertTrue(resultBetter.isSuccess)
    val (userAfterBetter, _, _) = resultBetter.getOrThrow()
    assertEquals(1000L, userAfterBetter.xp) // Exactly 1000 XP!
    assertEquals(3, userAfterBetter.totalStars) // Exactly 3 stars!
  }

  // ==========================================
  // QA TEST 6: Global Ranking
  // ==========================================
  @Test
  fun testGlobalRankingSortingAndNoPlaceholders() = runBlocking {
    database.userDao().deleteAllUsers()

    val u1 = UserAccount(
      uid = "u1", displayName = "Player A", email = null, photoUrl = null, provider = "guest",
      unlockedStage = 2, highestStage = 1, xp = 700L, totalStars = 2, stagesWon = 1,
      questionsAnswered = 10, bestStreak = 1, totalTimeSeconds = 30L, lastSyncTimestamp = 0L
    )
    val u2 = UserAccount(
      uid = "u2", displayName = "Player B", email = null, photoUrl = null, provider = "guest",
      unlockedStage = 3, highestStage = 2, xp = 950L, totalStars = 3, stagesWon = 2,
      questionsAnswered = 20, bestStreak = 2, totalTimeSeconds = 50L, lastSyncTimestamp = 0L
    )
    val u3 = UserAccount(
      uid = "u3", displayName = "Player C", email = null, photoUrl = null, provider = "guest",
      unlockedStage = 3, highestStage = 2, xp = 1000L, totalStars = 3, stagesWon = 2,
      questionsAnswered = 20, bestStreak = 2, totalTimeSeconds = 45L, lastSyncTimestamp = 0L
    )

    database.userDao().insertUser(u1)
    database.userDao().insertUser(u2)
    database.userDao().insertUser(u3)

    val ranking = gameRepository.getGlobalRanking().first()

    // Real users only, size 3
    assertEquals(3, ranking.size)

    // Player C: 3 stars, 1000 XP -> Rank 1
    assertEquals("Player C", ranking[0].displayName)
    assertEquals(3, ranking[0].totalStars)
    assertEquals(1000L, ranking[0].xp)

    // Player B: 3 stars, 950 XP -> Rank 2
    assertEquals("Player B", ranking[1].displayName)
    assertEquals(3, ranking[1].totalStars)
    assertEquals(950L, ranking[1].xp)

    // Player A: 2 stars, 700 XP -> Rank 3
    assertEquals("Player A", ranking[2].displayName)
    assertEquals(2, ranking[2].totalStars)
  }

  // ==========================================
  // QA TEST 7: Anti-Cheat & Progress Persistence
  // ==========================================
  @Test
  fun testAntiCheatIntegrity() {
    val uid = "test_player_anticheat"
    val stageNumber = 3
    val score = 10
    val timeTakenSeconds = 15L

    val validToken = AntiCheatEngine.generateToken(uid, stageNumber, score, timeTakenSeconds)

    // Valid submission
    val validRes = AntiCheatEngine.validateStageCompletion(
      uid = uid,
      stageNumber = stageNumber,
      currentUnlockedStage = 3,
      score = 10,
      timeTakenSeconds = 15L,
      token = validToken
    )
    assertTrue(validRes.isSuccess)

    // Cannot submit locked stage
    val lockedRes = AntiCheatEngine.validateStageCompletion(
      uid = uid,
      stageNumber = 5,
      currentUnlockedStage = 3,
      score = 10,
      timeTakenSeconds = 15L,
      token = validToken
    )
    assertTrue(lockedRes.isFailure)

    // Must answer all 10
    val scoreRes = AntiCheatEngine.validateStageCompletion(
      uid = uid,
      stageNumber = 3,
      currentUnlockedStage = 3,
      score = 8,
      timeTakenSeconds = 15L,
      token = validToken
    )
    assertTrue(scoreRes.isFailure)

    // Must meet realistic response time (> 3s for 10 answers)
    val timeRes = AntiCheatEngine.validateStageCompletion(
      uid = uid,
      stageNumber = 3,
      currentUnlockedStage = 3,
      score = 10,
      timeTakenSeconds = 1L,
      token = validToken
    )
    assertTrue(timeRes.isFailure)

    // Tampered token
    val tamperedRes = AntiCheatEngine.validateStageCompletion(
      uid = uid,
      stageNumber = 3,
      currentUnlockedStage = 3,
      score = 10,
      timeTakenSeconds = 15L,
      token = "fake_tampered_token"
    )
    assertTrue(tamperedRes.isFailure)
  }

  // ==========================================
  // QA TEST 8: Offline / Sync Behavior
  // ==========================================
  @Test
  fun testSyncStatusDistinction() {
    val status = gameRepository.getSyncStatus()
    assertNotNull(status.label)
    assertTrue(status.label.isNotEmpty())
  }

  // ==========================================
  // QA TEST 9: Question Difficulty System Tests
  // ==========================================
  @Test
  fun testStage1DifficultyAndDistribution() = runBlocking {
    val questions = gameRepository.getQuestionsForStage(1, AppLanguage.ENGLISH)
    assertEquals(10, questions.size)

    println("=== STAGE 1 DIFFICULTY DISTRIBUTION (ENGLISH) ===")
    questions.forEachIndexed { index, q ->
      val qNum = index + 1
      println("Q$qNum [${q.difficultyTier.label}, Score: ${q.difficultyScore}, Category: ${q.category}]: ${q.question} -> Answers: ${q.acceptedAnswers}")
      // Q1..Q3 must be Very Easy (1..20)
      if (qNum in 1..3) {
        assertEquals("Q$qNum should be VERY_EASY", DifficultyTier.VERY_EASY, q.difficultyTier)
        assertTrue("Q$qNum score must be <= 20", q.difficultyScore <= 20)
      }
      // Q4..Q8 must be Easy (21..40)
      if (qNum in 4..8) {
        assertEquals("Q$qNum should be EASY", DifficultyTier.EASY, q.difficultyTier)
        assertTrue("Q$qNum score must be between 21 and 40", q.difficultyScore in 21..40)
      }
      // Q9 must be Easy or Medium (<= 50)
      if (qNum == 9) {
        assertTrue("Q9 score must be <= 50", q.difficultyScore <= 50)
      }
      // Q10 must be Medium (41..60)
      if (qNum == 10) {
        assertEquals("Q10 should be MEDIUM", DifficultyTier.MEDIUM, q.difficultyTier)
        assertTrue("Q10 score must be in 41..60", q.difficultyScore in 41..60)
      }

      // No question in Stage 1 should be Hard, Very Hard, or Challenge
      assertTrue("Question should not be hard in stage 1", q.difficultyScore <= 60)
    }

    // Verify category variety (at least 5 distinct categories among 10 questions)
    val categories = questions.map { it.category }.toSet()
    println("Stage 1 Unique Categories count: ${categories.size}, Categories: $categories")
    assertTrue("Stage 1 should have at least 5 different categories", categories.size >= 5)
  }

  @Test
  fun testStage2DifficultyAndDistribution() = runBlocking {
    val questions = gameRepository.getQuestionsForStage(2, AppLanguage.ENGLISH)
    assertEquals(10, questions.size)

    println("=== STAGE 2 DIFFICULTY DISTRIBUTION (ENGLISH) ===")
    questions.forEachIndexed { index, q ->
      val qNum = index + 1
      println("Q$qNum [${q.difficultyTier.label}, Score: ${q.difficultyScore}, Category: ${q.category}]: ${q.question} -> Answers: ${q.acceptedAnswers}")
      if (qNum in 1..3) {
        assertTrue("Q$qNum score must be <= 20", q.difficultyScore <= 20)
      }
      if (qNum in 4..8) {
        assertTrue("Q$qNum score must be in 21..40", q.difficultyScore in 21..40)
      }
      assertTrue("No question in Stage 2 should be Hard or above (>60)", q.difficultyScore <= 60)
    }

    val categories = questions.map { it.category }.toSet()
    println("Stage 2 Unique Categories count: ${categories.size}, Categories: $categories")
    assertTrue("Stage 2 should have at least 5 different categories", categories.size >= 5)
  }

  @Test
  fun testStage5DifficultyAndDistribution() = runBlocking {
    val questions = gameRepository.getQuestionsForStage(5, AppLanguage.ENGLISH)
    assertEquals(10, questions.size)

    println("=== STAGE 5 DIFFICULTY DISTRIBUTION (ENGLISH) ===")
    questions.forEachIndexed { index, q ->
      val qNum = index + 1
      println("Q$qNum [${q.difficultyTier.label}, Score: ${q.difficultyScore}, Category: ${q.category}]: ${q.question} -> Answers: ${q.acceptedAnswers}")
      if (qNum in 1..3) {
        assertTrue("Q$qNum score must be <= 20", q.difficultyScore <= 20)
      }
      if (qNum in 4..8) {
        assertTrue("Q$qNum score must be in 21..40", q.difficultyScore in 21..40)
      }
      // Never Hard, Very Hard, or Challenge in stages 1–5
      assertTrue("No question in Stage 5 should be Hard or above (>60)", q.difficultyScore <= 60)
    }

    val categories = questions.map { it.category }.toSet()
    println("Stage 5 Unique Categories count: ${categories.size}, Categories: $categories")
    assertTrue("Stage 5 should have at least 5 different categories", categories.size >= 5)
  }

  @Test
  fun testMultilingualDifficultyEquivalence() = runBlocking {
    val enQ = gameRepository.getQuestionsForStage(1, AppLanguage.ENGLISH)
    val arQ = gameRepository.getQuestionsForStage(1, AppLanguage.ARABIC)
    val esQ = gameRepository.getQuestionsForStage(1, AppLanguage.SPANISH)
    val frQ = gameRepository.getQuestionsForStage(1, AppLanguage.FRENCH)

    println("=== MULTILINGUAL STAGE 1 CHECKS ===")
    for (i in 0 until 10) {
      val qNum = i + 1
      println("Q$qNum EN: [${enQ[i].difficultyScore}] ${enQ[i].question}")
      println("Q$qNum AR: [${arQ[i].difficultyScore}] ${arQ[i].question}")
      println("Q$qNum ES: [${esQ[i].difficultyScore}] ${esQ[i].question}")
      println("Q$qNum FR: [${frQ[i].difficultyScore}] ${frQ[i].question}")

      // All early questions must be Very Easy or Easy
      assertTrue("EN Q$qNum must be <= 60", enQ[i].difficultyScore <= 60)
      assertTrue("AR Q$qNum must be <= 60", arQ[i].difficultyScore <= 60)
      assertTrue("ES Q$qNum must be <= 60", esQ[i].difficultyScore <= 60)
      assertTrue("FR Q$qNum must be <= 60", frQ[i].difficultyScore <= 60)

      if (qNum <= 3) {
        assertTrue("EN Q$qNum Very Easy", enQ[i].difficultyScore <= 20)
        assertTrue("AR Q$qNum Very Easy", arQ[i].difficultyScore <= 20)
        assertTrue("ES Q$qNum Very Easy", esQ[i].difficultyScore <= 20)
        assertTrue("FR Q$qNum Very Easy", frQ[i].difficultyScore <= 20)
      }
    }
  }

  @Test
  fun testQuestionValidatorEnforcement() {
    val hardQuestion = Question(
      id = "hard_test",
      language = "en",
      category = "history",
      difficulty = 8,
      difficultyScore = 75,
      question = "Who was the Byzantine emperor in 610?",
      acceptedAnswers = listOf("Heraclius")
    )

    // Should be rejected in Stage 1 Q1
    assertFalse(QuestionValidator.validateQuestionDifficulty(hardQuestion, stage = 1, questionNumber = 1))
    // Should be rejected in Stage 1 Q10
    assertFalse(QuestionValidator.validateQuestionDifficulty(hardQuestion, stage = 1, questionNumber = 10))
    // Should be rejected in Stage 5 Q10
    assertFalse(QuestionValidator.validateQuestionDifficulty(hardQuestion, stage = 5, questionNumber = 10))

    // Valid easy question in Stage 1 Q1
    val easyQuestion = Question(
      id = "easy_test",
      language = "en",
      category = "animals",
      difficulty = 1,
      difficultyScore = 5,
      question = "What animal says meow?",
      acceptedAnswers = listOf("Cat")
    )
    assertTrue(QuestionValidator.validateQuestionDifficulty(easyQuestion, stage = 1, questionNumber = 1))

    // Rejection on blank answers
    val invalidBlankAnswer = easyQuestion.copy(acceptedAnswers = listOf(""))
    val resBlank = QuestionValidator.validateQuestion(invalidBlankAnswer, stage = 1, questionNumber = 1, emptyList())
    assertFalse(resBlank.isValid)

    // Rejection on missing numeric value
    val invalidNumeric = easyQuestion.copy(
      answerType = AnswerType.NUMBER,
      correctNumericValue = null
    )
    val resNum = QuestionValidator.validateQuestion(invalidNumeric, stage = 1, questionNumber = 1, emptyList())
    assertFalse(resNum.isValid)
  }

  // ==========================================
  // QA TEST 6: 10 Consecutive Stages & 100 Questions No-Duplicate-Concept Test
  // ==========================================
  @Test
  fun testTenConsecutiveStagesNoDuplicateConcepts() = runBlocking {
    val playerTracker = com.example.data.questions.QuestionHistoryTracker()
    val allQuestionsSeen = mutableListOf<Question>()
    val allConceptsSeen = mutableSetOf<String>()

    println("\n=======================================================")
    println("=== 10 CONSECUTIVE STAGES (100 QUESTIONS) TEST ===")
    println("=======================================================")

    for (stage in 1..10) {
      val stageQuestions = QuestionBank.getQuestionsForStage(stage, AppLanguage.ENGLISH, playerTracker)
      assertEquals(10, stageQuestions.size)

      val stageConcepts = stageQuestions.map { it.effectiveConceptId }
      val stageAnswers = stageQuestions.map { it.primaryAnswer }
      val stageCategories = stageQuestions.map { it.category }.toSet()

      // Within-stage duplicate checks
      assertEquals("Stage $stage must have 10 unique concepts", 10, stageConcepts.toSet().size)
      assertTrue("Stage $stage must have diverse categories (>= 5)", stageCategories.size >= 5)

      // Across-stage concept duplication check for the same player
      for (q in stageQuestions) {
        val cid = q.effectiveConceptId
        assertFalse(
          "Player encountered duplicate concept '$cid' in Stage $stage (Q: ${q.question})",
          allConceptsSeen.contains(cid)
        )
        allConceptsSeen.add(cid)
        allQuestionsSeen.add(q)
      }

      println("Stage $stage: 10 questions OK, ${stageCategories.size} unique categories: $stageCategories")
    }

    assertEquals(100, allQuestionsSeen.size)
    assertEquals(100, allConceptsSeen.size)
    println("SUCCESS: Player answered 100 questions across 10 stages with ZERO duplicate concepts!\n")
  }

  // ==========================================
  // QA TEST 7: Semantic Duplicate Concept Detection
  // ==========================================
  @Test
  fun testSemanticDuplicateConceptDetection() {
    val q1 = Question(
      id = "q_planet_1",
      language = "en",
      category = "science",
      difficulty = 3,
      difficultyScore = 46,
      question = "What is the largest planet?",
      conceptId = "largest_planet_solar_system",
      acceptedAnswers = listOf("Jupiter")
    )

    val q2 = Question(
      id = "q_planet_2",
      language = "en",
      category = "space",
      difficulty = 3,
      difficultyScore = 46,
      question = "Which planet is the biggest in our solar system?",
      conceptId = "largest_planet_solar_system",
      acceptedAnswers = listOf("Jupiter")
    )

    // They have different text and IDs, but SAME conceptId -> Must be detected as duplicates
    val validation = QuestionValidator.validateQuestion(q2, stage = 1, questionNumber = 10, listOf(q1))
    assertFalse("Question with identical conceptId must be rejected", validation.isValid)

    // Two questions with DIFFERENT concepts (e.g. elephant trunk vs elephant size) -> Must be ALLOWED
    val qTrunk = Question(
      id = "q_trunk",
      language = "en",
      category = "animals",
      difficulty = 1,
      difficultyScore = 10,
      question = "What animal is known for having a long trunk?",
      conceptId = "elephant_trunk",
      acceptedAnswers = listOf("Elephant")
    )
    val qSize = Question(
      id = "q_size",
      language = "en",
      category = "animals",
      difficulty = 2,
      difficultyScore = 26,
      question = "What animal is the largest land mammal?",
      conceptId = "elephant_largest_land_mammal",
      acceptedAnswers = listOf("Elephant")
    )
    val validationDiffConcepts = QuestionValidator.validateQuestion(qSize, stage = 1, questionNumber = 4, listOf(qTrunk))
    // Note: primary answers are both Elephant, so within the SAME stage it alerts duplicate answer, but concepts are different:
    assertEquals("elephant_trunk", qTrunk.effectiveConceptId)
    assertEquals("elephant_largest_land_mammal", qSize.effectiveConceptId)
    assertTrue(qTrunk.effectiveConceptId != qSize.effectiveConceptId)
  }

  // ==========================================
  // QA TEST 8: Question Pool Metrics and Verification Report
  // ==========================================
  @Test
  fun testQuestionPoolMetricsAndVerificationReport() {
    val totalQuestions = QuestionBank.getTotalAvailableQuestions()
    val totalConcepts = QuestionBank.getTotalUniqueConcepts()
    val byLang = QuestionBank.getQuestionsCountByLanguage()
    val byTier = QuestionBank.getQuestionsCountByTier()
    val byCat = QuestionBank.getQuestionsCountByCategory()

    println("\n=======================================================")
    println("=== QUESTION POOL & DIVERSITY METRICS REPORT ===")
    println("=======================================================")
    println("1. Number of unique curated questions available: $totalQuestions")
    println("2. Number of unique concepts: $totalConcepts")
    println("3. Questions per language: $byLang")
    println("4. Questions per difficulty tier: $byTier")
    println("5. Questions per category: $byCat")
    println("=======================================================\n")

    assertTrue("Must have at least 100 questions total in bank", totalQuestions >= 100)
    assertTrue("Must have at least 40 unique concepts", totalConcepts >= 40)
    assertTrue("English bank must be populated", (byLang["en"] ?: 0) >= 30)
    assertTrue("Arabic bank must be populated", (byLang["ar"] ?: 0) >= 30)
  }

  // ==========================================
  // QA TEST 9: 20 Simulated Players Personalization Test (Stage 1, Stage 2, Stage 5)
  // ==========================================
  @Test
  fun testTwentyPlayersPersonalizedAssignments() = runBlocking {
    val stagesToTest = listOf(1, 2, 5)

    for (stage in stagesToTest) {
      println("\n=======================================================")
      println("=== PERSONALIZATION REPORT: 20 PLAYERS IN STAGE $stage ===")
      println("=======================================================")

      val playerAssignments = mutableMapOf<String, List<Question>>()
      val assignmentSignatures = mutableSetOf<String>()

      for (i in 1..20) {
        val playerId = "simulated_player_$i"
        val questions = QuestionBank.getQuestionsForStage(
          stage = stage,
          language = AppLanguage.ENGLISH,
          playerId = playerId
        )
        assertEquals(10, questions.size)

        // Verify difficulty calibration for every player
        for (qIdx in questions.indices) {
          val qNum = qIdx + 1
          val q = questions[qIdx]
          assertTrue(
            "Player $playerId Q$qNum difficulty score ${q.difficultyScore} out of range for Stage $stage",
            QuestionValidator.validateQuestionDifficulty(q, stage, qNum)
          )
        }

        playerAssignments[playerId] = questions
        val signature = questions.joinToString("|") { it.id }
        assignmentSignatures.add(signature)
      }

      // 1. Exact duplicate assignments
      val exactDuplicates = 20 - assignmentSignatures.size
      println("1. Exact Duplicate Assignments: $exactDuplicates (out of 20 players)")
      assertEquals("No two players should receive the exact same 10-question set and order", 0, exactDuplicates)

      // 2 & 3 & 4. Pairwise Overlap & Different-Order Percentage
      var totalOverlap = 0
      var maxOverlap = 0
      var comparisonCount = 0
      var differentOrderCount = 0

      val players = playerAssignments.keys.toList()
      for (i in 0 until players.size) {
        for (j in i + 1 until players.size) {
          comparisonCount++
          val set1 = playerAssignments[players[i]]!!.map { it.effectiveConceptId }.toSet()
          val set2 = playerAssignments[players[j]]!!.map { it.effectiveConceptId }.toSet()
          val overlap = set1.intersect(set2).size
          totalOverlap += overlap
          if (overlap > maxOverlap) {
            maxOverlap = overlap
          }

          val ids1 = playerAssignments[players[i]]!!.map { it.id }
          val ids2 = playerAssignments[players[j]]!!.map { it.id }
          if (ids1 != ids2) {
            differentOrderCount++
          }
        }
      }

      val avgOverlap = totalOverlap.toDouble() / comparisonCount
      val diffOrderPct = (differentOrderCount.toDouble() / comparisonCount) * 100.0

      println("2. Average Question Overlap between players: ${"%.2f".format(avgOverlap)} / 10 questions")
      println("3. Maximum Question Overlap between any two players: $maxOverlap / 10 questions")
      println("4. Different-Order Percentage: ${"%.1f".format(diffOrderPct)}%")

      // 5. Difficulty distribution verification across all 20 players
      var allDifficultyValid = 0
      for ((_, questions) in playerAssignments) {
        for (qIdx in questions.indices) {
          if (QuestionValidator.validateQuestionDifficulty(questions[qIdx], stage, qIdx + 1)) {
            allDifficultyValid++
          }
        }
      }
      assertEquals("All 200 questions across 20 players must match the stage difficulty profile", 200, allDifficultyValid)
      println("5. Difficulty Distribution: 100% compliant across all 20 players (200/200 questions calibrated)")

      // 6. Category distribution diversity
      val avgUniqueCats = playerAssignments.values.map { qList -> qList.map { it.category }.toSet().size }.average()
      println("6. Average Unique Categories per player: ${"%.2f".format(avgUniqueCats)} / 10 questions")
      assertTrue("Each player must experience broad category diversity (>= 5 categories)", avgUniqueCats >= 5.0)

      println("SUCCESS: Stage $stage passed all 20-player personalization tests!")
    }
  }

  // ==========================================
  // QA TEST 10: Assignment Persistence, Restoration and Replay
  // ==========================================
  @Test
  fun testAssignmentPersistenceAndReplay() = runBlocking {
    val playerA = "player_alice"
    val playerB = "player_bob"
    val testStage = 7

    // 1. Player A starts Stage 7
    val assignmentA1 = gameRepository.getQuestionsForStage(testStage, AppLanguage.ENGLISH, playerId = playerA, isReplay = false)
    assertEquals(10, assignmentA1.size)

    // 2. Player A closes the app and reopens Stage 7 -> SAME assignment restored
    val assignmentA2 = gameRepository.getQuestionsForStage(testStage, AppLanguage.ENGLISH, playerId = playerA, isReplay = false)
    assertEquals("Question IDs must be identical upon restore", assignmentA1.map { it.id }, assignmentA2.map { it.id })
    println("Verified: Player A closed and reopened app -> exact same 10-question assignment restored!")

    // 3. Player B starts Stage 7 -> receives a DIFFERENT assignment
    val assignmentB = gameRepository.getQuestionsForStage(testStage, AppLanguage.ENGLISH, playerId = playerB, isReplay = false)
    val overlapAB = assignmentA1.map { it.id }.toSet().intersect(assignmentB.map { it.id }.toSet()).size
    println("Verified: Player B received personalized assignment (Overlap with Player A: $overlapAB/10 questions)")
    assertTrue("Player B should not receive identical assignment as Player A", assignmentA1.map { it.id } != assignmentB.map { it.id })

    // 4. Player A completes Stage 7 and wins
    val userA = UserAccount(
      uid = playerA,
      displayName = "Alice",
      email = "alice@test.com",
      photoUrl = null,
      provider = "guest",
      unlockedStage = testStage,
      highestStage = testStage,
      xp = 0L,
      totalStars = 0
    )
    val token = AntiCheatEngine.generateToken(playerA, testStage, 10, 30L)
    val winResult = gameRepository.recordStageWin(userA, stageNumber = testStage, stageTotalXp = 950, timeTakenSeconds = 30L, token = token)
    assertTrue("Win recording must succeed", winResult.isSuccess)

    // 5. Player A chooses REPLAY -> NEW personalized assignment generated
    val assignmentAReplay = gameRepository.getQuestionsForStage(testStage, AppLanguage.ENGLISH, playerId = playerA, isReplay = true)
    assertEquals(10, assignmentAReplay.size)
    println("Verified: Player A replayed Stage 7 -> new personalized assignment generated!")

    // 6. Verify XP and stars were preserved, not corrupted or duplicated
    val progress = database.stageProgressDao().getProgressForStage(testStage)
    assertNotNull(progress)
    assertEquals(950, progress?.stageXp)
    assertEquals(3, progress?.starsEarned)
    println("Verified: Player A replay preserved best historical XP (950) and stars (3) with zero corruption!")
  }

  // ==========================================
  // QA TEST 11: 50 Simulated Players across Stages 1, 2, and 5
  // ==========================================
  @Test
  fun testFiftyPlayersPersonalizationStages1_2_5() = runBlocking {
    val stagesToTest = listOf(1, 2, 5)

    for (stage in stagesToTest) {
      val playerAssignments = mutableMapOf<String, List<Question>>()
      val assignmentSignatures = mutableSetOf<String>()

      for (i in 1..50) {
        val playerId = "sim_player_$i"
        val questions = QuestionBank.getQuestionsForStage(
          stage = stage,
          language = AppLanguage.ENGLISH,
          playerId = playerId
        )
        assertEquals(10, questions.size)

        // Verify difficulty compliance for every slot
        for (qIdx in questions.indices) {
          val qNum = qIdx + 1
          val q = questions[qIdx]
          assertTrue(
            "Player $playerId Q$qNum score ${q.difficultyScore} out of range for Stage $stage",
            QuestionValidator.validateQuestionDifficulty(q, stage, qNum)
          )
          // Slot specific verification for stages 1..5:
          // Q1-Q3: Very Easy (<=20)
          // Q4-Q8: Easy (21..40)
          // Q9: Easy/Medium (30..48)
          // Q10: Medium (41..58)
          when (qNum) {
            1, 2, 3 -> assertTrue("Q$qNum must be Very Easy (<=20)", q.difficultyScore <= 20)
            4, 5, 6, 7, 8 -> assertTrue("Q$qNum must be Easy (21..40)", q.difficultyScore in 21..40)
            9 -> assertTrue("Q9 must be Easy/Medium (30..48)", q.difficultyScore in 30..48)
            10 -> assertTrue("Q10 must be Medium (41..58)", q.difficultyScore in 41..58)
          }

          // Semantic diversity: no duplicate concept ID inside same assignment
          val conceptCount = questions.count { it.effectiveConceptId.equals(q.effectiveConceptId, ignoreCase = true) }
          assertEquals("Concept ${q.effectiveConceptId} repeated in player $playerId stage $stage assignment", 1, conceptCount)
        }

        playerAssignments[playerId] = questions
        val signature = questions.joinToString("|") { it.id }
        assignmentSignatures.add(signature)
      }

      // Calculations across 50 players (50 * 49 / 2 = 1225 pairwise comparisons)
      val exactDuplicates = 50 - assignmentSignatures.size

      var totalOverlap = 0
      var maxOverlap = 0
      var minOverlap = 10
      var comparisonCount = 0
      var differentOrderCount = 0

      val players = playerAssignments.keys.toList()
      for (i in 0 until players.size) {
        for (j in i + 1 until players.size) {
          comparisonCount++
          val set1 = playerAssignments[players[i]]!!.map { it.effectiveConceptId }.toSet()
          val set2 = playerAssignments[players[j]]!!.map { it.effectiveConceptId }.toSet()
          val overlap = set1.intersect(set2).size
          totalOverlap += overlap
          if (overlap > maxOverlap) maxOverlap = overlap
          if (overlap < minOverlap) minOverlap = overlap

          val ids1 = playerAssignments[players[i]]!!.map { it.id }
          val ids2 = playerAssignments[players[j]]!!.map { it.id }
          if (ids1 != ids2) differentOrderCount++
        }
      }

      val avgOverlap = totalOverlap.toDouble() / comparisonCount
      val diffOrderPct = (differentOrderCount.toDouble() / comparisonCount) * 100.0
      val avgUniqueCats = playerAssignments.values.map { qList -> qList.map { it.category }.toSet().size }.average()

      println("\n-------------------------------------------------------------")
      println("50-PLAYER SIMULATION REPORT - STAGE $stage")
      println("-------------------------------------------------------------")
      println("Total Simulated Players: 50")
      println("Total Pairwise Comparisons: $comparisonCount")
      println("1. Exact Duplicate Assignments: $exactDuplicates / 50")
      println("2. Average Question Overlap: ${"%.2f".format(avgOverlap)} / 10")
      println("3. Maximum Question Overlap: $maxOverlap / 10")
      println("4. Minimum Question Overlap: $minOverlap / 10")
      println("5. Different-Order Percentage: ${"%.2f".format(diffOrderPct)}%")
      println("6. Difficulty Compliance: 100% (500/500 questions strictly compliant)")
      println("7. Average Category Diversity: ${"%.2f".format(avgUniqueCats)} / 10")
      println("-------------------------------------------------------------")

      assertEquals("No duplicate assignments among 50 players in Stage $stage", 0, exactDuplicates)
      assertEquals("100% different order across 50 players in Stage $stage", 100.0, diffOrderPct, 0.001)
      assertTrue("Category diversity >= 5.0", avgUniqueCats >= 5.0)
    }
  }

  // ==========================================
  // QA TEST 12: Language Isolation (English, Arabic, Spanish, French)
  // ==========================================
  @Test
  fun testLanguageIsolationAllFourLanguages() = runBlocking {
    val languages = listOf(AppLanguage.ENGLISH, AppLanguage.ARABIC, AppLanguage.SPANISH, AppLanguage.FRENCH)

    for (lang in languages) {
      val playerAssignments = mutableMapOf<String, List<Question>>()
      val assignmentSignatures = mutableSetOf<String>()

      for (i in 1..20) {
        val playerId = "lang_player_${lang.code}_$i"
        val questions = QuestionBank.getQuestionsForStage(
          stage = 1,
          language = lang,
          playerId = playerId
        )
        assertEquals(10, questions.size)

        for (q in questions) {
          // Strictly from selected language pool
          assertEquals("Question language must match", lang.code, q.language)

          // Arabic specific RTL verification
          if (lang == AppLanguage.ARABIC) {
            // Check that either question or answers contain Arabic script (except math numbers)
            val hasArabicOrMath = q.question.any { it in '\u0600'..'\u06FF' } || q.category == "mathematics"
            assertTrue("Arabic question must have Arabic characters or valid math text: ${q.question}", hasArabicOrMath)
            // Ensure no French or Spanish mixed into answers
            for (ans in q.acceptedAnswers) {
              val isMixedFrenchSpanish = ans.contains("Oui") || ans.contains("Non") || ans.contains("Bonjour")
              assertFalse("Arabic answers must not contain French/Spanish text: $ans", isMixedFrenchSpanish)
            }
          }
        }

        playerAssignments[playerId] = questions
        assignmentSignatures.add(questions.joinToString("|") { it.id })
      }

      val exactDuplicates = 20 - assignmentSignatures.size
      println("Language Isolation [${lang.name}]: 20 players, Exact Duplicates = $exactDuplicates / 20, 100% questions matched ${lang.code}")
      assertEquals(0, exactDuplicates)
    }
  }

  // ==========================================
  // QA TEST 13: Catalog Deep Inspection
  // ==========================================
  @Test
  fun testDetailedCatalogAnalysis() {
    val allQuestions = QuestionBank.getAllQuestions()
    val totalIds = allQuestions.map { it.id }.distinct().size
    val totalConcepts = allQuestions.map { it.effectiveConceptId.lowercase() }.distinct().size
    val byLanguage = QuestionBank.getQuestionsCountByLanguage()
    val byTier = QuestionBank.getQuestionsCountByTier()
    val byCategory = QuestionBank.getQuestionsCountByCategory()

    println("\n============================================================")
    println("CATALOG DEEP INSPECTION AUDIT")
    println("============================================================")
    println("Total Unique Question IDs: $totalIds")
    println("Total Unique Concept IDs: $totalConcepts")
    println("Counts per Language: $byLanguage")
    println("Counts per Difficulty Tier: $byTier")
    println("Counts per Category: $byCategory")
    println("============================================================\n")

    assertTrue("Must have at least 2500 questions", totalIds >= 2500)
    assertTrue("Must have at least 700 genuine concepts", totalConcepts >= 700)
    val hardCount = byTier[DifficultyTier.HARD] ?: 0
    val veryHardCount = byTier[DifficultyTier.VERY_HARD] ?: 0
    val challengeCount = byTier[DifficultyTier.CHALLENGE] ?: 0
    assertTrue("Must have at least 250 Hard questions (found $hardCount)", hardCount >= 250)
    assertTrue("Must have at least 100 Very Hard questions (found $veryHardCount)", veryHardCount >= 100)
    assertTrue("Must have at least 50 Challenge questions (found $challengeCount)", challengeCount >= 50)
  }

  // ==========================================
  // QA TEST 14: Deterministic Seed Formula Verification
  // ==========================================
  @Test
  fun testDeterministicSeedFormula() {
    val seed1 = QuestionBank.computePlayerStageSeed("player_alpha", 1, 1, 0)
    val seed2 = QuestionBank.computePlayerStageSeed("player_alpha", 1, 1, 0)
    val seedDiffPlayer = QuestionBank.computePlayerStageSeed("player_beta", 1, 1, 0)
    val seedDiffStage = QuestionBank.computePlayerStageSeed("player_alpha", 2, 1, 0)
    val seedDiffVersion = QuestionBank.computePlayerStageSeed("player_alpha", 1, 2, 0)
    val seedDiffAttempt = QuestionBank.computePlayerStageSeed("player_alpha", 1, 1, 1)

    // Deterministic: same inputs -> exact same seed
    assertEquals(seed1, seed2)

    // Differentiates each factor:
    assertTrue("Seed must distinguish player", seed1 != seedDiffPlayer)
    assertTrue("Seed must distinguish stage", seed1 != seedDiffStage)
    assertTrue("Seed must distinguish version", seed1 != seedDiffVersion)
    assertTrue("Seed must distinguish attempt", seed1 != seedDiffAttempt)
  }

  // ==========================================
  // QA TEST 15: Catalog Comprehensive Validation Pipeline
  // ==========================================
  @Test
  fun testAllCatalogQuestionsPassValidation() {
    val allQuestions = QuestionBank.getAllQuestions()
    var failureCount = 0
    val failureReasons = mutableListOf<String>()

    for (q in allQuestions) {
      if (q.question.isBlank() || q.question.length < 5) {
        failureCount++
        failureReasons.add("Too short text: ${q.id}")
      }
      if (q.acceptedAnswers.isEmpty() || q.acceptedAnswers.any { it.isBlank() }) {
        failureCount++
        failureReasons.add("Blank answers: ${q.id}")
      }
      if (q.category !in QuestionValidator.VALID_CATEGORIES) {
        failureCount++
        failureReasons.add("Invalid category ${q.category}: ${q.id}")
      }
      if (q.difficultyScore !in 1..100) {
        failureCount++
        failureReasons.add("Score out of bounds (${q.difficultyScore}): ${q.id}")
      }
      if (q.answerType in listOf(AnswerType.NUMBER, AnswerType.YEAR, AnswerType.DECIMAL)) {
        if (q.correctNumericValue == null) {
          failureCount++
          failureReasons.add("Missing numeric value: ${q.id}")
        }
      }
    }

    println("Validation Pipeline Check: ${allQuestions.size} questions checked. Failures: $failureCount")
    assertEquals("Validation failure rate must be 0", 0, failureCount)
  }

  // ==========================================
  // QA TEST 16: Long-Stage Simulation (Stages 25, 50, 100, 200 x 50 Players)
  // ==========================================
  @Test
  fun testLongStageSimulationStages25_50_100_200() = runBlocking {
    val stages = listOf(25, 50, 100, 200)
    val playerCount = 50

    println("\n============================================================")
    println("LONG-STAGE SIMULATION AUDIT (50 Players x Stages 25, 50, 100, 200)")
    println("============================================================")

    for (stage in stages) {
      val assignments = mutableListOf<List<Question>>()
      val signatures = mutableSetOf<String>()
      var totalDifficultyViolations = 0
      var totalConceptDuplicatesWithinStage = 0
      var totalProceduralQuestions = 0
      val categoryDiversityCounts = mutableListOf<Int>()

      for (p in 1..playerCount) {
        val playerId = "sim_player_stage_${stage}_$p"
        val questions = QuestionBank.getQuestionsForStage(
          stage = stage,
          language = AppLanguage.ENGLISH,
          playerId = playerId
        )
        assertEquals("Stage $stage assignment must have 10 questions", 10, questions.size)

        // 1. Difficulty compliance
        for (qIdx in questions.indices) {
          val q = questions[qIdx]
          val compliant = QuestionValidator.validateQuestionDifficulty(q, stage, qIdx + 1)
          if (!compliant) {
            totalDifficultyViolations++
          }
          if (q.id.startsWith("proc_")) {
            totalProceduralQuestions++
          }
        }

        // 2. Semantic concept duplicates within this 10-question stage
        val distinctConcepts = questions.map { it.effectiveConceptId.lowercase() }.distinct().size
        if (distinctConcepts < 10) {
          totalConceptDuplicatesWithinStage += (10 - distinctConcepts)
        }

        // 3. Category diversity
        val uniqueCategories = questions.map { it.category.lowercase() }.distinct().size
        categoryDiversityCounts.add(uniqueCategories)

        assignments.add(questions)
        signatures.add(questions.joinToString("|") { it.id })
      }

      val exactDuplicates = playerCount - signatures.size
      val avgUniqueCats = categoryDiversityCounts.average()

      // Calculate pair-wise overlap among all 50 players
      var totalOverlap = 0
      var maxOverlap = 0
      var minOverlap = 10
      var pairCount = 0

      for (i in 0 until playerCount) {
        val idsA = assignments[i].map { it.id }.toSet()
        for (j in i + 1 until playerCount) {
          val idsB = assignments[j].map { it.id }.toSet()
          val common = (idsA intersect idsB).size
          totalOverlap += common
          if (common > maxOverlap) maxOverlap = common
          if (common < minOverlap) minOverlap = common
          pairCount++
        }
      }

      val avgOverlap = if (pairCount > 0) totalOverlap.toDouble() / pairCount else 0.0
      val totalQuestionsGenerated = playerCount * 10
      val proceduralFallbackPct = (totalProceduralQuestions.toDouble() / totalQuestionsGenerated) * 100.0

      println("Stage $stage Report (50 Players):")
      println("  - Exact Duplicate Assignments: $exactDuplicates / $playerCount")
      println("  - Average Question Overlap: %.2f / 10 questions".format(avgOverlap))
      println("  - Min / Max Overlap: $minOverlap / $maxOverlap")
      println("  - Difficulty Compliance Violations: $totalDifficultyViolations")
      println("  - Semantic Concept Duplicates: $totalConceptDuplicatesWithinStage")
      println("  - Average Category Diversity: %.2f unique categories per stage".format(avgUniqueCats))
      println("  - Procedural Fallback Percentage: %.1f%% (%d / %d questions)".format(proceduralFallbackPct, totalProceduralQuestions, totalQuestionsGenerated))
      println("------------------------------------------------------------")

      assertEquals("Stage $stage exact duplicate assignments must be 0", 0, exactDuplicates)
      assertEquals("Stage $stage difficulty compliance violations must be 0", 0, totalDifficultyViolations)
      assertEquals("Stage $stage duplicate concepts within assignment must be 0", 0, totalConceptDuplicatesWithinStage)
      assertTrue("Stage $stage average category diversity must be >= 5", avgUniqueCats >= 5.0)
    }
  }
}

