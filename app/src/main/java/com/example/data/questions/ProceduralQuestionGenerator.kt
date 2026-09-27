package com.example.data.questions

import com.example.data.model.AnswerType
import com.example.data.model.AppLanguage
import com.example.data.model.Question
import kotlin.random.Random

object ProceduralQuestionGenerator {

  /**
   * Generates a rich, verified question for a requested category and difficulty score,
   * with a semantic concept ID to prevent any duplicate concepts.
   */
  fun generate(
    stage: Int,
    questionNumber: Int,
    targetScore: Int,
    language: AppLanguage,
    category: String,
    seed: Long
  ): Question {
    val rng = Random(seed + stage * 1009L + questionNumber * 73L)

    return when (category.lowercase()) {
      "mathematics" -> generateMathQuestion(stage, questionNumber, targetScore, language, rng)
      "science" -> generateScienceQuestion(stage, questionNumber, targetScore, language, rng)
      "everyday" -> generateEverydayQuestion(stage, questionNumber, targetScore, language, rng)
      "geography" -> generateGeographyQuestion(stage, questionNumber, targetScore, language, rng)
      "animals" -> generateAnimalQuestion(stage, questionNumber, targetScore, language, rng)
      "language" -> generateLanguageQuestion(stage, questionNumber, targetScore, language, rng)
      "sports" -> generateSportsQuestion(stage, questionNumber, targetScore, language, rng)
      "food" -> generateFoodQuestion(stage, questionNumber, targetScore, language, rng)
      else -> generateMathQuestion(stage, questionNumber, targetScore, language, rng)
    }
  }

  private fun generateMathQuestion(
    stage: Int,
    qNum: Int,
    targetScore: Int,
    lang: AppLanguage,
    rng: Random
  ): Question {
    return when {
      targetScore <= 20 -> {
        // Very Easy addition (e.g. 3 + 4, 1 + 5, 4 + 4)
        val a = rng.nextInt(1, 6)
        val b = rng.nextInt(1, 6)
        val sum = a + b
        val qText = when (lang) {
          AppLanguage.ARABIC -> "كم حاصل جمع $a + $b؟"
          AppLanguage.SPANISH -> "¿Cuánto es $a + $b?"
          AppLanguage.FRENCH -> "Combien font $a + $b ?"
          else -> "What is $a + $b?"
        }
        val exp = when (lang) {
          AppLanguage.ARABIC -> "$a + $b = $sum."
          else -> "$a + $b = $sum."
        }
        Question(
          id = "proc_math_add_${a}_${b}_${lang.code}",
          language = lang.code,
          category = "mathematics",
          difficulty = 1,
          question = qText,
          answerType = AnswerType.NUMBER,
          acceptedAnswers = listOf(sum.toString()),
          correctNumericValue = sum.toDouble(),
          tolerance = 0.001,
          explanation = exp,
          difficultyScore = (4 + (a + b)).coerceIn(3, 18),
          conceptId = "math_add_${minOf(a, b)}_${maxOf(a, b)}"
        )
      }
      targetScore <= 40 -> {
        // Easy multiplication or subtraction
        if (rng.nextBoolean()) {
          val a = rng.nextInt(3, 9)
          val b = rng.nextInt(2, 6)
          val prod = a * b
          val qText = when (lang) {
            AppLanguage.ARABIC -> "كم حاصل ضرب $a في $b؟"
            AppLanguage.SPANISH -> "¿Cuánto es $a por $b?"
            AppLanguage.FRENCH -> "Combien font $a fois $b ?"
            else -> "What is $a times $b?"
          }
          Question(
            id = "proc_math_mul_${a}_${b}_${lang.code}",
            language = lang.code,
            category = "mathematics",
            difficulty = 2,
            question = qText,
            answerType = AnswerType.NUMBER,
            acceptedAnswers = listOf(prod.toString()),
            correctNumericValue = prod.toDouble(),
            tolerance = 0.001,
            explanation = "$a × $b = $prod.",
            difficultyScore = 22 + (a + b),
            conceptId = "math_mul_${minOf(a, b)}_${maxOf(a, b)}"
          )
        } else {
          val a = rng.nextInt(12, 25)
          val b = rng.nextInt(3, 10)
          val diff = a - b
          val qText = when (lang) {
            AppLanguage.ARABIC -> "كم حاصل طرح $a - $b؟"
            AppLanguage.SPANISH -> "¿Cuánto es $a menos $b?"
            AppLanguage.FRENCH -> "Combien font $a moins $b ?"
            else -> "What is $a minus $b?"
          }
          Question(
            id = "proc_math_sub_${a}_${b}_${lang.code}",
            language = lang.code,
            category = "mathematics",
            difficulty = 2,
            question = qText,
            answerType = AnswerType.NUMBER,
            acceptedAnswers = listOf(diff.toString()),
            correctNumericValue = diff.toDouble(),
            tolerance = 0.001,
            explanation = "$a - $b = $diff.",
            difficultyScore = 24 + (b % 10),
            conceptId = "math_sub_${a}_${b}"
          )
        }
      }
      targetScore <= 60 -> {
        // Medium: geometry or powers
        val sidesOptions = listOf(
          Triple("pentagon", "خماسي الأضلاع (المخمس)", 5),
          Triple("hexagon", "سداسي الأضلاع (المسدس)", 6),
          Triple("heptagon", "سباعي الأضلاع", 7),
          Triple("octagon", "ثماني الأضلاع (المثمن)", 8),
          Triple("nonagon", "تساعي الأضلاع", 9),
          Triple("decagon", "عشاري الأضلاع", 10),
          Triple("dodecagon", "اثنا عشري الأضلاع", 12)
        )
        val choice = sidesOptions[rng.nextInt(sidesOptions.size)]
        val qText = when (lang) {
          AppLanguage.ARABIC -> "كم عدد أضلاع الشكل الهندسي ${choice.second}؟"
          AppLanguage.SPANISH -> "¿Cuántos lados tiene un ${choice.first} regular?"
          AppLanguage.FRENCH -> "Combien de côtés possède un ${choice.first} régulier ?"
          else -> "How many sides does a regular ${choice.first} have?"
        }
        Question(
          id = "proc_math_sides_${choice.first}_${choice.third}_s${stage}_q${qNum}_${lang.code}",
          language = lang.code,
          category = "mathematics",
          difficulty = 3,
          question = qText,
          answerType = AnswerType.NUMBER,
          acceptedAnswers = listOf(choice.third.toString()),
          correctNumericValue = choice.third.toDouble(),
          tolerance = 0.001,
          explanation = "A ${choice.first} has ${choice.third} sides.",
          difficultyScore = targetScore,
          conceptId = "proc_geometry_sides_${choice.first}_${choice.third}"
        )
      }
      targetScore <= 80 -> {
        // Hard: Polygon interior angles sum ((n-2)*180) or Powers of 2
        if (rng.nextBoolean()) {
          val nOptions = listOf(
            Triple(5, 540, "pentagon (خماسي الأضلاع)"),
            Triple(6, 720, "hexagon (سداسي الأضلاع)"),
            Triple(7, 900, "heptagon (سباعي الأضلاع)"),
            Triple(8, 1080, "octagon (ثماني الأضلاع)"),
            Triple(9, 1260, "nonagon (تساعي الأضلاع)"),
            Triple(10, 1440, "decagon (عشاري الأضلاع)"),
            Triple(12, 1800, "dodecagon (اثنا عشري الأضلاع)")
          )
          val item = nOptions[rng.nextInt(nOptions.size)]
          val qText = when (lang) {
            AppLanguage.ARABIC -> "كم مجموع قياسات الزوايا الداخلية لمضلع منتظم ذي ${item.first} أضلاع (${item.third}) بالدرجات؟"
            AppLanguage.SPANISH -> "¿Cuál es la suma de los ángulos interiores de un polígono de ${item.first} lados en grados?"
            AppLanguage.FRENCH -> "Quelle est la somme des angles intérieurs d'un polygone à ${item.first} côtés en degrés ?"
            else -> "What is the sum of interior angles in degrees for a regular ${item.first}-sided polygon?"
          }
          Question(
            id = "proc_math_angles_${item.first}_s${stage}_q${qNum}_${lang.code}",
            language = lang.code,
            category = "mathematics",
            difficulty = 4,
            question = qText,
            answerType = AnswerType.NUMBER,
            acceptedAnswers = listOf(item.second.toString()),
            correctNumericValue = item.second.toDouble(),
            tolerance = 0.001,
            explanation = "Sum of interior angles = (n - 2) * 180.",
            difficultyScore = targetScore,
            conceptId = "proc_math_angles_sum_${item.first}_sides"
          )
        } else {
          val pOptions = listOf(Pair(6, 64), Pair(7, 128), Pair(8, 256), Pair(9, 512), Pair(10, 1024), Pair(11, 2048))
          val item = pOptions[rng.nextInt(pOptions.size)]
          val qText = when (lang) {
            AppLanguage.ARABIC -> "ما هي القيمة العددية لـ 2 مرفوعاً للأس ${item.first} (2^${item.first})؟"
            AppLanguage.SPANISH -> "¿Cuánto es 2 elevado a la potencia ${item.first} (2^${item.first})?"
            AppLanguage.FRENCH -> "Combien vaut 2 à la puissance ${item.first} (2^${item.first}) ?"
            else -> "What is the value of 2 raised to the power of ${item.first} (2^${item.first})?"
          }
          Question(
            id = "proc_math_pow2_${item.first}_s${stage}_q${qNum}_${lang.code}",
            language = lang.code,
            category = "mathematics",
            difficulty = 4,
            question = qText,
            answerType = AnswerType.NUMBER,
            acceptedAnswers = listOf(item.second.toString()),
            correctNumericValue = item.second.toDouble(),
            tolerance = 0.001,
            explanation = "2^${item.first} = ${item.second}.",
            difficultyScore = targetScore,
            conceptId = "proc_math_pow2_${item.first}"
          )
        }
      }
      targetScore <= 95 -> {
        // Very Hard: Binary to decimal conversion or integer squares
        if (rng.nextBoolean()) {
          val binOptions = listOf(
            Pair("1011", 11), Pair("1101", 13), Pair("1111", 15), Pair("10011", 19),
            Pair("10101", 21), Pair("10110", 22), Pair("11001", 25), Pair("11011", 27),
            Pair("11101", 29), Pair("11110", 30)
          )
          val item = binOptions[rng.nextInt(binOptions.size)]
          val qText = when (lang) {
            AppLanguage.ARABIC -> "ما هي القيمة العشرية المكافئة للعدد الثنائي (${item.first})؟"
            AppLanguage.SPANISH -> "¿Cuál es el valor decimal equivalente del número binario ${item.first}?"
            AppLanguage.FRENCH -> "Quelle est la valeur décimale du nombre binaire ${item.first} ?"
            else -> "What is the decimal equivalent of the binary number ${item.first}?"
          }
          Question(
            id = "proc_math_bin_${item.first}_s${stage}_q${qNum}_${lang.code}",
            language = lang.code,
            category = "mathematics",
            difficulty = 5,
            question = qText,
            answerType = AnswerType.NUMBER,
            acceptedAnswers = listOf(item.second.toString()),
            correctNumericValue = item.second.toDouble(),
            tolerance = 0.001,
            explanation = "Binary ${item.first} in base 10 is ${item.second}.",
            difficultyScore = targetScore,
            conceptId = "proc_math_binary_${item.first}"
          )
        } else {
          val sqOptions = listOf(Pair(13, 169), Pair(14, 196), Pair(15, 225), Pair(16, 256), Pair(17, 289), Pair(18, 324), Pair(19, 361), Pair(21, 441))
          val item = sqOptions[rng.nextInt(sqOptions.size)]
          val qText = when (lang) {
            AppLanguage.ARABIC -> "ما هو مربع العدد ${item.first} (${item.first} × ${item.first})؟"
            AppLanguage.SPANISH -> "¿Cuál es el cuadrado de ${item.first} (${item.first} x ${item.first})?"
            AppLanguage.FRENCH -> "Quel est le carré de ${item.first} (${item.first} x ${item.first}) ?"
            else -> "What is the square of ${item.first} (${item.first} x ${item.first})?"
          }
          Question(
            id = "proc_math_sq_${item.first}_s${stage}_q${qNum}_${lang.code}",
            language = lang.code,
            category = "mathematics",
            difficulty = 5,
            question = qText,
            answerType = AnswerType.NUMBER,
            acceptedAnswers = listOf(item.second.toString()),
            correctNumericValue = item.second.toDouble(),
            tolerance = 0.001,
            explanation = "${item.first}^2 = ${item.second}.",
            difficultyScore = targetScore,
            conceptId = "proc_math_square_${item.first}"
          )
        }
      }
      else -> {
        // Challenge (96..100): Sum of first N odd numbers (N^2) or Combinations C(n,2)
        val nOdd = listOf(Pair(11, 121), Pair(12, 144), Pair(13, 169), Pair(14, 196), Pair(15, 225), Pair(16, 256), Pair(20, 400))
        val item = nOdd[rng.nextInt(nOdd.size)]
        val qText = when (lang) {
          AppLanguage.ARABIC -> "ما هو مجموع أول ${item.first} من الأعداد الفردية الموجبة (1 + 3 + 5 + ... )؟"
          AppLanguage.SPANISH -> "¿Cuál es la suma de los primeros ${item.first} números impares positivos?"
          AppLanguage.FRENCH -> "Quelle est la somme des ${item.first} premiers entiers impairs positifs ?"
          else -> "What is the sum of the first ${item.first} positive odd integers (1 + 3 + 5 + ...)?"
        }
        Question(
          id = "proc_math_odd_sum_${item.first}_s${stage}_q${qNum}_${lang.code}",
          language = lang.code,
          category = "mathematics",
          difficulty = 6,
          question = qText,
          answerType = AnswerType.NUMBER,
          acceptedAnswers = listOf(item.second.toString()),
          correctNumericValue = item.second.toDouble(),
          tolerance = 0.001,
          explanation = "The sum of the first N odd numbers is N^2 = ${item.first}^2 = ${item.second}.",
          difficultyScore = targetScore,
          conceptId = "proc_math_odd_sum_${item.first}"
        )
      }
    }
  }

  private fun generateEverydayQuestion(
    stage: Int,
    qNum: Int,
    targetScore: Int,
    lang: AppLanguage,
    rng: Random
  ): Question {
    val pool = listOf(
      Quadruple(
        "seconds_in_minute",
        "How many seconds are in one full minute?",
        "كم ثانية في الدقيقة الواحدة؟",
        listOf("60"),
        22
      ),
      Quadruple(
        "centimeters_in_meter",
        "How many centimeters are in one standard meter?",
        "كم سنتيمتراً في المتر الواحد؟",
        listOf("100"),
        26
      ),
      Quadruple(
        "grams_in_kilogram",
        "How many grams are in one kilogram?",
        "كم غراماً في الكيلوغرام الواحد؟",
        listOf("1000"),
        28
      ),
      Quadruple(
        "hours_in_two_days",
        "How many hours are in 2 full days?",
        "كم ساعة في يومين كاملين؟",
        listOf("48"),
        25
      ),
      Quadruple(
        "millimeters_in_centimeter",
        "How many millimeters are in one centimeter?",
        "كم مليمتراً في السنتيمتر الواحد؟",
        listOf("10"),
        24
      ),
      Quadruple(
        "sides_in_cube",
        "How many faces does a standard six-sided die or cube have?",
        "كم وجهاً لمكعب النرد التقليدي؟",
        listOf("6"),
        23
      ),
      Quadruple(
        "dozen_count",
        "How many items are in a standard dozen?",
        "كم حبة في الدستة (الدرزن) الواحدة؟",
        listOf("12"),
        26
      )
    )

    val item = pool[rng.nextInt(pool.size)]
    val qText = when (lang) {
      AppLanguage.ARABIC -> item.arabicQ
      AppLanguage.SPANISH -> item.englishQ
      AppLanguage.FRENCH -> item.englishQ
      else -> item.englishQ
    }
    return Question(
      id = "proc_everyday_${item.concept}_${lang.code}",
      language = lang.code,
      category = "everyday",
      difficulty = 2,
      question = qText,
      answerType = AnswerType.NUMBER,
      acceptedAnswers = item.answers,
      correctNumericValue = item.answers.first().toDoubleOrNull(),
      tolerance = 0.001,
      explanation = "Standard unit measure.",
      difficultyScore = item.score,
      conceptId = item.concept
    )
  }

  private fun generateScienceQuestion(
    stage: Int,
    qNum: Int,
    targetScore: Int,
    lang: AppLanguage,
    rng: Random
  ): Question {
    val items = listOf(
      Quadruple(
        "heart_pumps_blood",
        "What muscular organ pumps blood throughout the human body?",
        "ما هو العضو العضلي الذي يضخ الدم في جميع أنحاء جسم الإنسان؟",
        listOf("Heart", "The heart", "القلب", "قلب", "Corazón", "Cœur"),
        25
      ),
      Quadruple(
        "planets_in_solar_system",
        "How many official recognized planets are in our solar system?",
        "كم عدد الكواكب المعترف بها رسمياً في مجموعتنا الشمسية؟",
        listOf("8", "Eight", "ثمانية", "ثمان", "٨", "Ocho", "Huit"),
        28
      ),
      Quadruple(
        "third_planet_sun",
        "What is the third planet from the Sun in our solar system?",
        "ما هو الكوكب الثالث بعداً عن الشمس في مجموعتنا الشمسية؟",
        listOf("Earth", "الأرض", "الارض", "Tierra", "Terre"),
        26
      ),
      Quadruple(
        "lungs_respiratory_organ",
        "Which pair of organs inside the human chest is used for breathing?",
        "ما هما العضوان في صدر الإنسان المسؤولان عن التنفس وتبادل الهواء؟",
        listOf("Lungs", "The lungs", "الرئتان", "الرئتين", "رئتان", "Pulmones", "Poumons"),
        32
      ),
      Quadruple(
        "stomach_digestion_organ",
        "Which major digestive organ breaks down food using gastric acid?",
        "ما هو العضو الرئيسي في الجهاز الهضمي الذي يفرز العصارة الهضمية لهضم الطعام؟",
        listOf("Stomach", "The stomach", "المعدة", "معدة", "Estómago", "Estomac"),
        30
      ),
      Quadruple(
        "co2_chemical_formula",
        "What is the chemical formula for carbon dioxide?",
        "ما هي الصيغة الكيميائية لغاز ثاني أكسيد الكربون؟",
        listOf("CO2", "C02"),
        42
      )
    )

    val item = items[rng.nextInt(items.size)]
    val qText = when (lang) {
      AppLanguage.ARABIC -> item.arabicQ
      else -> item.englishQ
    }
    return Question(
      id = "proc_science_${item.concept}_${lang.code}",
      language = lang.code,
      category = "science",
      difficulty = 2,
      question = qText,
      answerType = AnswerType.TEXT,
      acceptedAnswers = item.answers,
      explanation = "Scientific fact.",
      difficultyScore = item.score,
      conceptId = item.concept
    )
  }

  private fun generateGeographyQuestion(
    stage: Int,
    qNum: Int,
    targetScore: Int,
    lang: AppLanguage,
    rng: Random
  ): Question {
    val capitals = listOf(
      Triple("Germany", "ألمانيا", listOf("Berlin", "برلين")),
      Triple("Canada", "كندا", listOf("Ottawa", "أوتاوا", "اوتاوا")),
      Triple("Brazil", "البرازيل", listOf("Brasilia", "برازيليا")),
      Triple("Japan", "اليابان", listOf("Tokyo", "طوكيو")),
      Triple("Egypt", "مصر", listOf("Cairo", "القاهرة", "القاهره")),
      Triple("Saudi Arabia", "المملكة العربية السعودية", listOf("Riyadh", "الرياض")),
      Triple("Italy", "إيطاليا", listOf("Rome", "روما")),
      Triple("Spain", "إسبانيا", listOf("Madrid", "مدريد")),
      Triple("France", "فرنسا", listOf("Paris", "باريس")),
      Triple("China", "الصين", listOf("Beijing", "بكين"))
    )

    val item = capitals[rng.nextInt(capitals.size)]
    val qText = when (lang) {
      AppLanguage.ARABIC -> "ما هي عاصمة دولة ${item.second}؟"
      AppLanguage.SPANISH -> "¿Cuál es la capital de ${item.first}?"
      AppLanguage.FRENCH -> "Quelle est la capitale de ${item.first} ?"
      else -> "What is the capital city of ${item.first}?"
    }
    return Question(
      id = "proc_geo_cap_${item.first.lowercase().replace(" ", "_")}_${lang.code}",
      language = lang.code,
      category = "geography",
      difficulty = 2,
      question = qText,
      answerType = AnswerType.TEXT,
      acceptedAnswers = item.third,
      explanation = "Capital city geography.",
      difficultyScore = (22 + (item.first.length % 15)).coerceIn(21, 55),
      conceptId = "capital_of_${item.first.lowercase().replace(" ", "_")}"
    )
  }

  private fun generateAnimalQuestion(
    stage: Int,
    qNum: Int,
    targetScore: Int,
    lang: AppLanguage,
    rng: Random
  ): Question {
    val facts = listOf(
      Quadruple(
        "lion_female_lioness",
        "What is a female lion called?",
        "ماذا تُسمى أنثى الأسد في اللغة العربية؟",
        listOf("Lioness", "لبؤة", "اللبؤة", "Leona", "Lionne"),
        26
      ),
      Quadruple(
        "horse_sound_neigh",
        "What sound does a horse make?",
        "ماذا يُسمى صوت الحصان (الفرس)؟",
        listOf("Neigh", "صهيل", "الصهيل", "Relincho", "Hennissement"),
        32
      ),
      Quadruple(
        "sheep_baby_lamb",
        "What is a baby sheep called?",
        "ماذا يُسمى صغير الخروف؟",
        listOf("Lamb", "حمل", "الحمل", "Cordero", "Agneau"),
        28
      ),
      Quadruple(
        "camel_ship_of_desert",
        "Which animal is famously nicknamed the ship of the desert?",
        "أي حيوان يُلقب بسفينة الصحراء لقدرته العالية على تحمل العطش؟",
        listOf("Camel", "The camel", "الجمل", "جمل", "Camello", "Chameau"),
        22
      )
    )

    val item = facts[rng.nextInt(facts.size)]
    val qText = when (lang) {
      AppLanguage.ARABIC -> item.arabicQ
      else -> item.englishQ
    }
    return Question(
      id = "proc_animal_${item.concept}_${lang.code}",
      language = lang.code,
      category = "animals",
      difficulty = 2,
      question = qText,
      answerType = AnswerType.TEXT,
      acceptedAnswers = item.answers,
      explanation = "Animal knowledge.",
      difficultyScore = item.score,
      conceptId = item.concept
    )
  }

  private fun generateLanguageQuestion(
    stage: Int,
    qNum: Int,
    targetScore: Int,
    lang: AppLanguage,
    rng: Random
  ): Question {
    val pairs = listOf(
      Quadruple("opposite_happy", "What is the opposite of happy?", "ما هو عكس كلمة سعيد؟", listOf("Sad", "Unhappy", "حزين"), 10),
      Quadruple("opposite_light", "What is the opposite of light (as in brightness)?", "ما هو عكس كلمة مضيء أو نور؟", listOf("Dark", "Darkness", "ظلام", "مظلم"), 12),
      Quadruple("opposite_easy", "What is the opposite of easy?", "ما هو عكس كلمة سهل؟", listOf("Hard", "Difficult", "صعب"), 10),
      Quadruple("opposite_true", "What is the opposite of true?", "ما هو عكس كلمة صحيح؟", listOf("False", "Wrong", "خطأ", "خاطئ"), 10)
    )
    val item = pairs[rng.nextInt(pairs.size)]
    val qText = when (lang) {
      AppLanguage.ARABIC -> item.arabicQ
      else -> item.englishQ
    }
    return Question(
      id = "proc_lang_${item.concept}_${lang.code}",
      language = lang.code,
      category = "language",
      difficulty = 1,
      question = qText,
      answerType = AnswerType.TEXT,
      acceptedAnswers = item.answers,
      explanation = "Vocabulary antonyms.",
      difficultyScore = item.score,
      conceptId = item.concept
    )
  }

  private fun generateSportsQuestion(
    stage: Int,
    qNum: Int,
    targetScore: Int,
    lang: AppLanguage,
    rng: Random
  ): Question {
    val sports = listOf(
      Quadruple(
        "basketball_players_count",
        "How many players are on the court for one basketball team?",
        "كم عدد لاعبي فريق كرة السلة داخل أرض الملعب؟",
        listOf("5", "Five", "خمسة", "٥"),
        28
      ),
      Quadruple(
        "golf_holes_standard",
        "How many holes are in a standard complete round of golf?",
        "كم عدد الحفر في جولة الغولف القياسية الكاملة؟",
        listOf("18", "Eighteen", "ثمانية عشر", "١٨"),
        36
      ),
      Quadruple(
        "tennis_zero_score_word",
        "In tennis scoring, what English word represents zero points?",
        "في لعبة التنس، ما هي الكلمة الشهيرة التي تعني صفر نقطة؟",
        listOf("Love", "لاف"),
        48
      )
    )
    val item = sports[rng.nextInt(sports.size)]
    val qText = when (lang) {
      AppLanguage.ARABIC -> item.arabicQ
      else -> item.englishQ
    }
    return Question(
      id = "proc_sports_${item.concept}_${lang.code}",
      language = lang.code,
      category = "sports",
      difficulty = 2,
      question = qText,
      answerType = AnswerType.TEXT,
      acceptedAnswers = item.answers,
      explanation = "Sports rule knowledge.",
      difficultyScore = item.score,
      conceptId = item.concept
    )
  }

  private fun generateFoodQuestion(
    stage: Int,
    qNum: Int,
    targetScore: Int,
    lang: AppLanguage,
    rng: Random
  ): Question {
    val foods = listOf(
      Quadruple(
        "bread_primary_ingredient",
        "What is the primary grain flour used to bake traditional white bread?",
        "ما هو دقيق الحبوب الأساسي المستخدم في خبز الخبز الأبيض التقليدي؟",
        listOf("Wheat", "Wheat flour", "القمح", "قمح", "طحين القمح"),
        26
      ),
      Quadruple(
        "apple_pie_fruit",
        "What popular crisp red or green orchard fruit is used in apple pie?",
        "ما هي الفاكهة الشهيرة التي يُصنع منها فطيرة التفاح الكلاسيكية؟",
        listOf("Apple", "Apples", "التفاح", "تفاح"),
        15
      ),
      Quadruple(
        "wine_fruit_base",
        "What fruit is crushed and fermented to make wine?",
        "ما هي ثمرة الفاكهة التي تُعصر وتُخمر لصنع الخمر الطبيعي والعصير؟",
        listOf("Grapes", "Grape", "العنب", "عنب"),
        30
      )
    )
    val item = foods[rng.nextInt(foods.size)]
    val qText = when (lang) {
      AppLanguage.ARABIC -> item.arabicQ
      else -> item.englishQ
    }
    return Question(
      id = "proc_food_${item.concept}_${lang.code}",
      language = lang.code,
      category = "food",
      difficulty = 2,
      question = qText,
      answerType = AnswerType.TEXT,
      acceptedAnswers = item.answers,
      explanation = "Food knowledge.",
      difficultyScore = item.score,
      conceptId = item.concept
    )
  }

  private data class Quadruple(
    val concept: String,
    val englishQ: String,
    val arabicQ: String,
    val answers: List<String>,
    val score: Int
  )
}
