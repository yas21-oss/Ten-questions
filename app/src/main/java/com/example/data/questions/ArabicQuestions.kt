package com.example.data.questions

import com.example.data.model.AnswerType
import com.example.data.model.Question

object ArabicQuestions {

  val list: List<Question> by lazy {
    listOf(
      // ==========================================
      // VERY EASY (Score 1–20) - Stages 1–5 (Q1–Q3)
      // ==========================================
      Question("ar_ve_1", "ar", "everyday", 1, "ما هو لون العشب الطبيعي؟", AnswerType.TEXT, listOf("أخضر", "اخضر"), explanation = "لون العشب الطبيعي هو الأخضر.", difficultyScore = 5, conceptId = "grass_natural_color"),
      Question("ar_ve_2", "ar", "everyday", 1, "كم يوماً في الأسبوع الواحد؟", AnswerType.NUMBER, listOf("7", "سبعة", "سبع", "٧"), correctNumericValue = 7.0, tolerance = 0.001, explanation = "يتكون الأسبوع من سبعة أيام.", difficultyScore = 5, conceptId = "days_in_week"),
      Question("ar_ve_3", "ar", "animals", 1, "ما هو الحيوان الأليف الذي يصدر صوت المواء؟", AnswerType.TEXT, listOf("القط", "القطة", "قطة", "قط"), explanation = "القطط تصدر صوت المواء.", difficultyScore = 5, conceptId = "cat_meow_sound"),
      Question("ar_ve_4", "ar", "mathematics", 1, "كم حاصل جمع 2 + 2؟", AnswerType.NUMBER, listOf("4", "أربعة", "اربعة", "٤"), correctNumericValue = 4.0, tolerance = 0.001, explanation = "2 + 2 = 4.", difficultyScore = 3, conceptId = "math_2_plus_2"),
      Question("ar_ve_5", "ar", "animals", 1, "كم رجلاً لدى الكلب العادي؟", AnswerType.NUMBER, listOf("4", "أربعة", "اربعة", "٤"), correctNumericValue = 4.0, tolerance = 0.001, explanation = "للكلب أربع أرجل.", difficultyScore = 5, conceptId = "dog_legs_count"),
      Question("ar_ve_6", "ar", "language", 1, "ما هو عكس كلمة حار؟", AnswerType.TEXT, listOf("بارد"), explanation = "بارد هو عكس حار.", difficultyScore = 8, conceptId = "opposite_of_hot"),
      Question("ar_ve_7", "ar", "science", 1, "ما هو اسم الكوكب الذي نعيش عليه؟", AnswerType.TEXT, listOf("الأرض", "الارض", "كوكب الأرض"), explanation = "يعيش البشر على كوكب الأرض.", difficultyScore = 6, conceptId = "planet_we_live_on"),
      Question("ar_ve_8", "ar", "mathematics", 1, "ما هو الرقم الذي يأتي بعد 9 مباشرة؟", AnswerType.NUMBER, listOf("10", "عشرة", "١٠"), correctNumericValue = 10.0, tolerance = 0.001, explanation = "الرقم 10 يأتي بعد الرقم 9.", difficultyScore = 4, conceptId = "number_after_9"),
      Question("ar_ve_9", "ar", "everyday", 1, "ما هو لون السماء الصافية في النهار؟", AnswerType.TEXT, listOf("أزرق", "ازرق"), explanation = "تبدو السماء الصافية باللون الأزرق.", difficultyScore = 6, conceptId = "sky_color_daytime"),
      Question("ar_ve_10", "ar", "everyday", 1, "كم شهراً في السنة الميلادية؟", AnswerType.NUMBER, listOf("12", "اثنا عشر", "١٢"), correctNumericValue = 12.0, tolerance = 0.001, explanation = "تتكون السنة من 12 شهراً.", difficultyScore = 8, conceptId = "months_in_year"),
      Question("ar_ve_11", "ar", "science", 1, "ماذا يُسمى الماء عندما يتجمد؟", AnswerType.TEXT, listOf("ثلج", "جليد"), explanation = "يتحول الماء المتجمد إلى ثلج أو جليد.", difficultyScore = 8, conceptId = "water_frozen_ice"),
      Question("ar_ve_12", "ar", "animals", 1, "ما هو الحيوان الذي ينبح؟", AnswerType.TEXT, listOf("الكلب", "كلب"), explanation = "الكلب يصدر صوت النباح.", difficultyScore = 5, conceptId = "dog_bark_sound"),
      Question("ar_ve_13", "ar", "food", 1, "ما هو الغذاء الحلو اللذيذ الذي ينتجه النحل؟", AnswerType.TEXT, listOf("العسل", "عسل"), explanation = "ينتج النحل عسل النحل.", difficultyScore = 10, conceptId = "honey_produced_by_bees"),
      Question("ar_ve_14", "ar", "language", 1, "ما هو عكس كلمة كبير؟", AnswerType.TEXT, listOf("صغير"), explanation = "صغير هو عكس كبير.", difficultyScore = 8, conceptId = "opposite_of_big"),
      Question("ar_ve_15", "ar", "everyday", 1, "كم عجلة للدراجة الهوائية العادية؟", AnswerType.NUMBER, listOf("2", "اثنان", "٢"), correctNumericValue = 2.0, tolerance = 0.001, explanation = "للدراجة الهوائية عجلتان.", difficultyScore = 6, conceptId = "bicycle_wheels_count"),
      Question("ar_ve_16", "ar", "mathematics", 1, "ما هو الشكل الهندسي الذي له ثلاثة أضلاع؟", AnswerType.TEXT, listOf("المثلث", "مثلث"), explanation = "المثلث يتكون من ثلاثة أضلاع.", difficultyScore = 10, conceptId = "triangle_sides_count"),
      Question("ar_ve_17", "ar", "science", 1, "ما هو النجم الذي يضيء للأرض في النهار؟", AnswerType.TEXT, listOf("الشمس"), explanation = "الشمس هي النجم المضيء لكوكبنا.", difficultyScore = 8, conceptId = "sun_lights_earth"),
      Question("ar_ve_18", "ar", "mathematics", 1, "كم حاصل جمع 5 + 5؟", AnswerType.NUMBER, listOf("10", "عشرة", "١٠"), correctNumericValue = 10.0, tolerance = 0.001, explanation = "5 + 5 = 10.", difficultyScore = 5, conceptId = "math_5_plus_5"),
      Question("ar_ve_19", "ar", "food", 1, "ما هو لون الموز الناضج عادة؟", AnswerType.TEXT, listOf("أصفر", "اصفر"), explanation = "الموز الناضج يتميز بلونه الأصفر.", difficultyScore = 7, conceptId = "banana_ripe_yellow"),
      Question("ar_ve_20", "ar", "language", 1, "ما هو عكس كلمة النهار؟", AnswerType.TEXT, listOf("الليل", "ليل"), explanation = "الليل هو عكس النهار.", difficultyScore = 7, conceptId = "opposite_of_day"),
      Question("ar_ve_21", "ar", "everyday", 1, "كم إصبعاً في يد الإنسان الواحدة عادة؟", AnswerType.NUMBER, listOf("5", "خمسة", "٥"), correctNumericValue = 5.0, tolerance = 0.001, explanation = "في اليد الواحدة 5 أصابع.", difficultyScore = 5, conceptId = "fingers_on_hand"),
      Question("ar_ve_22", "ar", "food", 1, "ما هو المشروب الأبيض الذي تنتجه الأبقار للإنسان؟", AnswerType.TEXT, listOf("الحليب", "اللبن", "حليب"), explanation = "الأبقار تمدنا بالحليب الطازج.", difficultyScore = 6, conceptId = "cows_produce_milk"),
      Question("ar_ve_23", "ar", "animals", 1, "ما هو الطائر الذي يبيض البيض الذي نتناوله في وجبة الإفطار؟", AnswerType.TEXT, listOf("الدجاجة", "دجاجة", "الدجاج"), explanation = "الدجاج يضع البيض.", difficultyScore = 6, conceptId = "chicken_breakfast_eggs"),
      Question("ar_ve_24", "ar", "science", 1, "ما هو الجرم السماوي الذي يضيء ليلاً ويتغير شكله خلال الشهر؟", AnswerType.TEXT, listOf("القمر", "قمر"), explanation = "القمر يدور حول الأرض وتتغير أطواره.", difficultyScore = 7, conceptId = "moon_night_phases"),
      Question("ar_ve_25", "ar", "mathematics", 1, "كم حاصل طرح 10 - 3؟", AnswerType.NUMBER, listOf("7", "سبعة", "٧"), correctNumericValue = 7.0, tolerance = 0.001, explanation = "10 - 3 = 7.", difficultyScore = 6, conceptId = "math_10_minus_3"),
      Question("ar_ve_26", "ar", "language", 1, "ما هو عكس كلمة سريع؟", AnswerType.TEXT, listOf("بطيء", "بطي"), explanation = "بطيء هو عكس سريع.", difficultyScore = 8, conceptId = "opposite_of_fast"),
      Question("ar_ve_27", "ar", "everyday", 1, "ما هو لون إشارة المرور الذي يعني قف؟", AnswerType.TEXT, listOf("أحمر", "احمر"), explanation = "اللون الأحمر يعني التوقف التام.", difficultyScore = 5, conceptId = "traffic_light_stop_red"),
      Question("ar_ve_28", "ar", "everyday", 1, "ما هو لون إشارة المرور الذي يعني انطلق أو سر؟", AnswerType.TEXT, listOf("أخضر", "اخضر"), explanation = "اللون الأخضر يعني السماح بالمرور.", difficultyScore = 5, conceptId = "traffic_light_go_green"),
      Question("ar_ve_29", "ar", "animals", 1, "ما هو الطائر الذي لا يطير ويعيش في المناطق الجليدية القطبية؟", AnswerType.TEXT, listOf("البطريق", "بطريق"), explanation = "طائر البطريق سباح ماهر ولا يطير.", difficultyScore = 12, conceptId = "penguin_flightless_antarctic"),
      Question("ar_ve_30", "ar", "food", 1, "ما هي الثمرة الحمراء المستديرة التي يُصنع منها الكاتشب وصلصة الطهي؟", AnswerType.TEXT, listOf("الطماطم", "طماطم", "البندورة"), explanation = "الطماطم هي أساس صلصة الكاتشب.", difficultyScore = 9, conceptId = "tomato_ketchup_ingredient"),

      // ==========================================
      // EASY (Score 21–40) - Stages 1–10 (Q4–Q8)
      // ==========================================
      Question("ar_ea_1", "ar", "geography", 2, "كم عدد قارات كوكب الأرض؟", AnswerType.NUMBER, listOf("7", "سبع", "سبعة", "٧"), correctNumericValue = 7.0, tolerance = 0.001, explanation = "توجد 7 قارات على كوكب الأرض.", difficultyScore = 25, conceptId = "continents_count_earth"),
      Question("ar_ea_2", "ar", "everyday", 2, "ما هو اللون الناتج عن مزج اللونين الأزرق والأصفر؟", AnswerType.TEXT, listOf("أخضر", "اخضر"), explanation = "مزج الأزرق والأصفر ينتج اللون الأخضر.", difficultyScore = 22, conceptId = "mixing_blue_yellow_green"),
      Question("ar_ea_3", "ar", "science", 2, "ما هو الكوكب المعروف بالكوكب الأحمر؟", AnswerType.TEXT, listOf("المريخ"), explanation = "المريخ يُلقب بالكوكب الأحمر.", difficultyScore = 24, conceptId = "mars_red_planet"),
      Question("ar_ea_4", "ar", "geography", 2, "ما هي عاصمة فرنسا؟", AnswerType.TEXT, listOf("باريس"), explanation = "باريس هي عاصمة فرنسا.", difficultyScore = 22, conceptId = "capital_of_france"),
      Question("ar_ea_5", "ar", "animals", 2, "ما هو أسرع حيوان بري على وجه الأرض؟", AnswerType.TEXT, listOf("الفهد", "فهد"), explanation = "الفهد هو أسرع الثدييات البرية.", difficultyScore = 28, conceptId = "cheetah_fastest_land_animal"),
      Question("ar_ea_6", "ar", "science", 2, "عند أي درجة مئوية يتجمد الماء النقي عند مستوى سطح البحر؟", AnswerType.NUMBER, listOf("0", "صفر", "٠"), correctNumericValue = 0.0, tolerance = 0.001, explanation = "يتجمد الماء عند 0 درجة مئوية.", difficultyScore = 24, conceptId = "water_freezing_point_celsius"),
      Question("ar_ea_7", "ar", "science", 2, "عند أي درجة مئوية يغلي الماء عند مستوى سطح البحر؟", AnswerType.NUMBER, listOf("100", "مائة", "مئة", "١٠٠"), correctNumericValue = 100.0, tolerance = 0.001, explanation = "يغلي الماء عند 100 درجة مئوية.", difficultyScore = 26, conceptId = "water_boiling_point_celsius"),
      Question("ar_ea_8", "ar", "sports", 2, "كم عدد لاعبي فريق كرة القدم داخل الملعب؟", AnswerType.NUMBER, listOf("11", "أحد عشر", "١١"), correctNumericValue = 11.0, tolerance = 0.001, explanation = "يتكون الفريق من 11 لاعباً في الملعب.", difficultyScore = 28, conceptId = "football_players_on_pitch"),
      Question("ar_ea_9", "ar", "animals", 2, "ما هو الحيوان الملقب بملك الغابة؟", AnswerType.TEXT, listOf("الأسد", "اسد"), explanation = "الأسد يُلقب بملك الغابة.", difficultyScore = 23, conceptId = "lion_king_of_jungle"),
      Question("ar_ea_10", "ar", "geography", 2, "ما هي عاصمة جمهورية مصر العربية؟", AnswerType.TEXT, listOf("القاهرة", "القاهره"), explanation = "القاهرة هي عاصمة مصر.", difficultyScore = 22, conceptId = "capital_of_egypt"),
      Question("ar_ea_11", "ar", "geography", 2, "ما هي عاصمة المملكة العربية السعودية؟", AnswerType.TEXT, listOf("الرياض"), explanation = "الرياض هي عاصمة المملكة العربية السعودية.", difficultyScore = 22, conceptId = "capital_of_saudi_arabia"),
      Question("ar_ea_12", "ar", "science", 2, "ما هو الغاز الضروري لتنفس الكائنات الحية للإبقاء على حياتها؟", AnswerType.TEXT, listOf("الأكسجين", "الاكسجين", "أكسجين"), explanation = "الأكسجين غاز أساسي للتنفس.", difficultyScore = 27, conceptId = "oxygen_human_survival"),
      Question("ar_ea_13", "ar", "geography", 2, "في أي دولة يقع برج إيفل؟", AnswerType.TEXT, listOf("فرنسا"), explanation = "يقع برج إيفل في العاصمة الفرنسية باريس.", difficultyScore = 23, conceptId = "eiffel_tower_country"),
      Question("ar_ea_14", "ar", "animals", 2, "ما هو أضخم حيوان بري على اليابسة يعيش حالياً؟", AnswerType.TEXT, listOf("الفيل", "الفيل الإفريقي", "فيل"), explanation = "الفيل هو أضخم الحيوانات البرية على اليابسة.", difficultyScore = 26, conceptId = "elephant_largest_land_mammal"),
      Question("ar_ea_15", "ar", "mathematics", 2, "ما حاصل ضرب 10 في 10؟", AnswerType.NUMBER, listOf("100", "مائة", "١٠٠"), correctNumericValue = 100.0, tolerance = 0.001, explanation = "10 × 10 = 100.", difficultyScore = 25, conceptId = "math_10_times_10"),
      Question("ar_ea_16", "ar", "everyday", 2, "كم ساعة في اليوم الواحد؟", AnswerType.NUMBER, listOf("24", "أربعة وعشرون", "٢٤"), correctNumericValue = 24.0, tolerance = 0.001, explanation = "اليوم الكامل يتكون من 24 ساعة.", difficultyScore = 21, conceptId = "hours_in_day"),
      Question("ar_ea_17", "ar", "geography", 2, "ما هو أكبر محيطات العالم مساحة؟", AnswerType.TEXT, listOf("المحيط الهادئ", "الهادئ", "المحيط الهادي"), explanation = "المحيط الهادئ هو الأكبر مساحة.", difficultyScore = 30, conceptId = "pacific_ocean_largest"),
      Question("ar_ea_18", "ar", "geography", 2, "ما هي عاصمة إيطاليا؟", AnswerType.TEXT, listOf("روما"), explanation = "روما هي عاصمة إيطاليا.", difficultyScore = 22, conceptId = "capital_of_italy"),
      Question("ar_ea_19", "ar", "technology", 2, "ما هي أداة التحكم اليدوية المستخدمة لتحريك مؤشر الفأرة على شاشة الحاسوب؟", AnswerType.TEXT, listOf("الفأرة", "الماوس", "فأرة"), explanation = "فأرة الحاسوب هي أداة التأشير الأساسية.", difficultyScore = 23, conceptId = "computer_mouse_device"),
      Question("ar_ea_20", "ar", "geography", 2, "في أي قارة تقع الصحراء الكبرى؟", AnswerType.TEXT, listOf("إفريقيا", "افريقيا"), explanation = "تقع الصحراء الكبرى في شمال إفريقيا.", difficultyScore = 26, conceptId = "sahara_desert_continent"),
      Question("ar_ea_21", "ar", "animals", 2, "ما هو الكائن الحي البحري الضخم الذي يُعتبر أكبر مخلوق على وجه الأرض؟", AnswerType.TEXT, listOf("الحوت الأزرق", "حوت ازرق"), explanation = "الحوت الأزرق هو الأضخم في تاريخ الأرض.", difficultyScore = 29, conceptId = "blue_whale_largest_creature"),
      Question("ar_ea_22", "ar", "geography", 2, "ما هي عاصمة المملكة المتحدة (بريطانيا)؟", AnswerType.TEXT, listOf("لندن"), explanation = "لندن هي عاصمة بريطانيا.", difficultyScore = 21, conceptId = "capital_of_uk"),
      Question("ar_ea_23", "ar", "science", 2, "كم عدد العظام في جسم الإنسان البالغ تقريباً؟", AnswerType.NUMBER, listOf("206", "٢٠٦"), correctNumericValue = 206.0, tolerance = 0.001, explanation = "يحتوي الهيكل العظمي البالغ على 206 عظمات.", difficultyScore = 35, conceptId = "human_adult_bones_count"),
      Question("ar_ea_24", "ar", "everyday", 2, "كم دقيقة في الساعة الواحدة؟", AnswerType.NUMBER, listOf("60", "ستون", "٦٠"), correctNumericValue = 60.0, tolerance = 0.001, explanation = "تضم الساعة 60 دقيقة.", difficultyScore = 22, conceptId = "minutes_in_hour"),
      Question("ar_ea_25", "ar", "food", 2, "ما هي الفاكهة الحمضية الشهيرة بلونها البرتقالي وغناها بفيتامين ج؟", AnswerType.TEXT, listOf("البرتقال", "برتقال"), explanation = "البرتقال مصدر ممتاز لفيتامين ج.", difficultyScore = 23, conceptId = "orange_citrus_fruit"),

      // ==========================================
      // MEDIUM (Score 41–60) - Stages 1–20 (Q9–Q10)
      // ==========================================
      Question("ar_me_1", "ar", "geography", 3, "ما هو أطول نهر في العالم؟", AnswerType.TEXT, listOf("نهر النيل", "النيل"), explanation = "نهر النيل في إفريقيا هو أطول نهر.", difficultyScore = 45, conceptId = "nile_longest_river"),
      Question("ar_me_2", "ar", "art", 3, "من هو الرسام الذي رسم لوحة الموناليزا الشهيرة؟", AnswerType.TEXT, listOf("ليوناردو دا فينشي", "دا فينشي", "ليوناردو دافنشي"), explanation = "رسمها ليوناردو دا فينشي.", difficultyScore = 48, conceptId = "mona_lisa_painter"),
      Question("ar_me_3", "ar", "geography", 3, "ما هي عاصمة الولايات المتحدة الأمريكية؟", AnswerType.MULTI_ACCEPTED_TEXT, listOf("واشنطن", "واشنطن العاصمة", "واشنطن دي سي"), explanation = "واشنطن العاصمة هي المقر الفيدرالي لأمريكا.", difficultyScore = 45, conceptId = "capital_of_usa"),
      Question("ar_me_4", "ar", "art", 3, "كم وتراً لآلة الكمان الموسيقية التقليدية؟", AnswerType.NUMBER, listOf("4", "أربعة", "٤"), correctNumericValue = 4.0, tolerance = 0.001, explanation = "لآلة الكمان 4 أوتار.", difficultyScore = 48, conceptId = "violin_strings_count"),
      Question("ar_me_5", "ar", "everyday", 3, "كم يوماً في السنة الميلادية العادية غير الكبيسة؟", AnswerType.NUMBER, listOf("365", "٣٦٥"), correctNumericValue = 365.0, tolerance = 0.001, explanation = "السنة العادية تضم 365 يوماً.", difficultyScore = 44, conceptId = "days_in_standard_year"),
      Question("ar_me_6", "ar", "science", 3, "ما هي الصيغة الكيميائية لجزيء الماء؟", AnswerType.TEXT, listOf("H2O", "H20"), explanation = "الماء يتكون من ذرتي هيدروجين وذرة أكسجين (H2O).", difficultyScore = 42, conceptId = "water_chemical_formula"),
      Question("ar_me_7", "ar", "geography", 3, "ما هي عاصمة اليابان؟", AnswerType.TEXT, listOf("طوكيو"), explanation = "طوكيو هي عاصمة اليابان.", difficultyScore = 43, conceptId = "capital_of_japan"),
      Question("ar_me_8", "ar", "mathematics", 3, "ما هي القيمة التقريبية للثابت الرياضي باي (π) برقمين عشريين؟", AnswerType.DECIMAL, listOf("3.14", "٣.١٤"), correctNumericValue = 3.141592653589793, tolerance = 0.08, explanation = "قيمة باي تقريباً 3.14.", difficultyScore = 48, conceptId = "pi_value_two_decimals"),
      Question("ar_me_9", "ar", "space", 3, "ما هو أكبر كواكب مجموعتنا الشمسية حجماً؟", AnswerType.TEXT, listOf("المشتري"), explanation = "كوكب المشتري هو الأكبر حجماً في المجموعة الشمسية.", difficultyScore = 46, conceptId = "jupiter_largest_planet"),
      Question("ar_me_10", "ar", "history", 3, "في أي عام هبط أول إنسان على سطح القمر؟", AnswerType.YEAR, listOf("1969", "١٩٦٩"), correctNumericValue = 1969.0, tolerance = 0.0001, explanation = "هبطت أبولو 11 على القمر عام 1969.", difficultyScore = 58, conceptId = "moon_landing_year"),
      Question("ar_me_11", "ar", "geography", 3, "ما هي أعلى قمة جبلية في العالم فوق مستوى سطح البحر؟", AnswerType.TEXT, listOf("إفرست", "قمة إفرست", "ايفرست"), explanation = "قمة إفرست في جبال الهيمالايا هي الأعلى.", difficultyScore = 45, conceptId = "everest_tallest_mountain"),
      Question("ar_me_12", "ar", "science", 3, "ما هو أصلب معدن طبيعي معروف على وجه الأرض؟", AnswerType.TEXT, listOf("الألماس", "الماس"), explanation = "الألماس يحتل أعلى درجات الصلابة.", difficultyScore = 47, conceptId = "diamond_hardest_mineral"),
      Question("ar_me_13", "ar", "geography", 3, "ما هي عاصمة كندا؟", AnswerType.TEXT, listOf("أوتاوا", "اوتاوا"), explanation = "أوتاوا هي العاصمة الوطنية لكندا.", difficultyScore = 52, conceptId = "capital_of_canada"),
      Question("ar_me_14", "ar", "technology", 3, "ما هي الشركة التي أطلقت هاتف آيفون الذكي في عام 2007؟", AnswerType.TEXT, listOf("آبل", "ابل", "شركة آبل"), explanation = "أطلقت شركة آبل هاتف آيفون بقيادة ستيف جوبز.", difficultyScore = 44, conceptId = "apple_created_iphone"),
      Question("ar_me_15", "ar", "history", 3, "ما هو التمثال التاريخي في مصر الذي يملك رأس إنسان وجسم أسد؟", AnswerType.TEXT, listOf("أبو الهول", "ابو الهول"), explanation = "تمثال أبو الهول يقف شامخاً بالجيزة.", difficultyScore = 48, conceptId = "great_sphinx_giza"),
      Question("ar_me_16", "ar", "science", 3, "ما هو الكوكب الأقرب إلى الشمس في المجموعة الشمسية؟", AnswerType.TEXT, listOf("عطارد"), explanation = "عطارد هو الكوكب الأقرب للشمس.", difficultyScore = 46, conceptId = "mercury_closest_to_sun"),
      Question("ar_me_17", "ar", "sports", 3, "كم عدد الحلقات المتشابكة في العلم الأولمبي الرسمي؟", AnswerType.NUMBER, listOf("5", "خمسة", "٥"), correctNumericValue = 5.0, tolerance = 0.001, explanation = "تمثل الحلقات الخمس قارات العالم المأهولة.", difficultyScore = 49, conceptId = "olympic_rings_count"),
      Question("ar_me_18", "ar", "geography", 3, "ما هي عاصمة البرازيل الرسمية؟", AnswerType.TEXT, listOf("برازيليا"), explanation = "برازيليا هي العاصمة الفيدرالية للبرازيل.", difficultyScore = 55, conceptId = "capital_of_brazil"),

      // ==========================================
      // HARD (Score 61–80) - Stages 11+
      // ==========================================
      Question("ar_ha_1", "ar", "history", 4, "في أي عام غرقت سفينة تايتانيك الشهيرة؟", AnswerType.YEAR, listOf("1912", "١٩١٢"), correctNumericValue = 1912.0, tolerance = 0.0001, explanation = "غرقت تايتانيك عام 1912.", difficultyScore = 65, conceptId = "titanic_sinking_year"),
      Question("ar_ha_2", "ar", "science", 4, "ما هو الغاز الأكثر وفرة في الغلاف الجوي للأرض؟", AnswerType.TEXT, listOf("النيتروجين", "نيتروجين", "غاز النيتروجين"), explanation = "النيتروجين يشكل حوالي 78% من هواء الأرض.", difficultyScore = 68, conceptId = "nitrogen_most_abundant_gas"),
      Question("ar_ha_3", "ar", "geography", 4, "ما هي عاصمة أستراليا الفيدرالية الرسمية؟", AnswerType.TEXT, listOf("كانبرا"), explanation = "كانبرا هي العاصمة الرسمية لأستراليا.", difficultyScore = 72, conceptId = "capital_of_australia"),
      Question("ar_ha_4", "ar", "technology", 4, "من اخترع شبكة الويب العالمية (World Wide Web) في عام 1989؟", AnswerType.TEXT, listOf("تيم بيرنرز لي", "تيم بيرنرز-لي", "بيرنرز لي"), explanation = "اخترعها السير تيم بيرنرز لي.", difficultyScore = 75, conceptId = "tim_berners_lee_www"),
      Question("ar_ha_5", "ar", "history", 4, "في أي عام انتهت الحرب العالمية الثانية؟", AnswerType.YEAR, listOf("1945", "١٩٤٥"), correctNumericValue = 1945.0, tolerance = 0.0001, explanation = "انتهت الحرب عام 1945.", difficultyScore = 62, conceptId = "ww2_end_year_1945")
    )
  }
}
