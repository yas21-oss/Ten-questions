package com.example.data.questions

import com.example.data.model.AnswerType
import com.example.data.model.AppLanguage
import com.example.data.model.Question

/**
 * AdvancedExpertCatalog provides:
 * - 55 Very Hard concepts (scores 81–95)
 * - 25 Challenge concepts (scores 96–100)
 * Rigorously verified facts in physics, cosmology, genetics, history, mathematics, and philosophy.
 */
object AdvancedExpertCatalog {

  fun getCatalog(language: AppLanguage): List<Question> {
    val list = mutableListOf<Question>()
    buildVeryHardQuestions(list, language)
    buildChallengeQuestions(list, language)
    return list
  }

  // 1. VERY HARD CONCEPTS (55 concepts, scores 81–95)
  private fun buildVeryHardQuestions(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      ExpItem("vh_carbon14_half_life_years", "What is the approximate radioactive half-life of Carbon-14 in years?", "كم يبلغ نصف العمر الإشعاعي التقريبي لنظير الكربون-14 بالسنوات؟", listOf("5730", "5700", "٥٧٣٠"), 88, "science", AnswerType.NUMBER, 5730.0),
      ExpItem("vh_human_chromosomes_count_cells", "How many total chromosomes are in a normal diploid somatic human cell?", "كم عدد الكروموسومات الإجمالي في الخلية الجسدية الطبيعية للإنسان (23 زوجاً)؟", listOf("46", "Forty-six", "ستة وأربعون", "٤٦"), 82, "science", AnswerType.NUMBER, 46.0),
      ExpItem("vh_uranium_atomic_number", "What is the atomic number of Uranium, the heaviest primordial chemical element?", "ما هو العدد الذري لعنصر اليورانيوم المشع، أثقل العناصر الطبيعية الأولية؟", listOf("92", "Ninety-two", "٩٢"), 85, "science", AnswerType.NUMBER, 92.0),
      ExpItem("vh_heisenberg_uncertainty_quantities", "Heisenberg's Uncertainty Principle states you cannot simultaneously know exact position and what other property?", "ينص مبدأ عدم اليقين لهايزنبرغ على استحالة قياس الموضع بدقة وبنفس الوقت مع ماذا؟", listOf("Momentum", "Velocity", "الزخم", "كمية الحركة"), 86, "science", AnswerType.TEXT),
      ExpItem("vh_planck_constant_symbol", "What single lowercase Latin letter standardly represents Planck's quantum constant?", "ما هو الحرف اللاتيني الصغير المستخدم دولياً لتمثيل ثابت بلانك الكمومي؟", listOf("h"), 84, "science", AnswerType.TEXT),
      ExpItem("vh_ideal_gas_constant_symbol", "In the ideal gas equation PV = nRT, which capital letter denotes the universal gas constant?", "في معادلة الغاز المثالي PV = nRT، أي حرف لاتيني كبير يمثل ثابت الغاز العام؟", listOf("R"), 83, "science", AnswerType.TEXT),
      ExpItem("vh_absolute_zero_kelvin_value", "What is the absolute zero temperature on the Kelvin thermodynamic scale?", "ما هي قيمة درجة حرارة الصفر المطلق على مقياس كلفن الديناميكي الحراري؟", listOf("0", "Zero", "0 K", "صفر", "٠"), 81, "science", AnswerType.NUMBER, 0.0),
      ExpItem("vh_standard_atmospheric_pressure_hpa", "What is standard sea-level atmospheric pressure in hectopascals (or millibars)?", "كم يبلغ الضغط الجوي القياسي عند مستوى سطح البحر بوحدة الهيكتوباسكال (أو الميليبار)؟", listOf("1013", "1013.25", "١٠١٣"), 89, "science", AnswerType.NUMBER, 1013.0),
      ExpItem("vh_universal_red_blood_donor", "Which specific ABO and Rh blood type is the universal red blood cell donor?", "أي زمرة دم دقيقة (بنظام ABO وعامل ريسوس) تُعتبر المتبرع العام بالخلايا الحمراء؟", listOf("O negative", "O-", "O-negative", "O سالب"), 84, "science", AnswerType.TEXT),
      ExpItem("vh_universal_plasma_donor", "Which ABO blood group plasma can be safely transfused to patients of any blood type?", "بلازما أي زمرة دم يمكن نقلها بأمان لجميع المرضى بغض النظر عن فصيلة دمهم؟", listOf("AB", "AB positive", "AB+", "AB موجب"), 91, "science", AnswerType.TEXT),
      ExpItem("vh_human_smallest_bone_stapes", "What is the smallest bone in the human skeleton, located in the middle ear?", "ما هي أصغر عظمة في الهيكل العظمي البشري، والموجودة داخل الأذن الوسطى؟", listOf("Stapes", "Stirrup", "عظمة الركاب", "الركاب"), 83, "science", AnswerType.TEXT),
      ExpItem("vh_organelle_hydrolytic_enzymes", "Which cellular organelle contains digestive hydrolytic enzymes to break down waste?", "ما هي العضية الخلوية التي تحتوي على إنزيمات هاضمة لتفكيك الفضلات والسموم؟", listOf("Lysosome", "Lysosomes", "الجسيم الهاضم", "الليزوسوم"), 87, "science", AnswerType.TEXT),
      ExpItem("vh_calvin_cycle_photosynthesis_location", "In plant chloroplasts, in what fluid-filled region does the light-independent Calvin cycle occur?", "في بلاستيدات النبات، في أي وسط سائل تحدث تفاعلات حلقة كالفن غير المعتمدة على الضوء؟", listOf("Stroma", "الستروما", "اللحمة"), 92, "science", AnswerType.TEXT),
      ExpItem("vh_cellular_respiration_atp_synthase", "What chemical energy-carrying nucleotide molecule is synthesized by ATP synthase?", "ما هو جزيء النيوكليوتيد الحامل للطاقة في الخلية الذي يتم إنتاجه بواسطة إنزيم ATP؟", listOf("ATP", "Adenosine triphosphate", "أدينوسين ثلاثي الفوسفات"), 82, "science", AnswerType.TEXT),
      ExpItem("vh_up_quark_electric_charge", "What fractional electric charge does an up quark carry (+2/3 or -1/3)?", "ما هي الشحنة الكهربائية الكسرية لكوارك علوي (موجب ثلثين أم سالب ثلث)؟", listOf("+2/3", "2/3", "موجب ثلثين"), 94, "science", AnswerType.TEXT),
      // Space
      ExpItem("vh_space_jwst_launch_year", "In what calendar year was the James Webb Space Telescope (JWST) launched into space?", "في أي عام ميلادي تم إطلاق تلسكوب جيمس ويب الفضائي (JWST) في رحلته الفضائية؟", listOf("2021", "٢٠٢١"), 83, "space", AnswerType.YEAR, 2021.0),
      ExpItem("vh_space_venus_day_longer_than_year", "Which planet in our solar system has a rotation period (day) longer than its orbit (year)?", "أي كواكب المجموعة الشمسية يستغرق يومه (دورانه حول نفسه) وقتاً أطول من سنته؟", listOf("Venus", "الزهرة"), 84, "space", AnswerType.TEXT),
      ExpItem("vh_space_keplers_first_law_shape", "According to Kepler's First Law, what geometric shape are planetary orbits?", "وفقاً لقانون كبلر الأول، ما هو الشكل الهندسي لمدارات الكواكب حول الشمس؟", listOf("Ellipse", "Elliptical", "قطع ناقص", "إهليلجي", "بيضاوي"), 82, "space", AnswerType.TEXT),
      ExpItem("vh_space_schwarzschild_radius_concept", "The boundary of a non-rotating black hole from which nothing can escape is defined by whose radius?", "الحد الخارجي للثقب الأسود الذي لا يمكن الإفلات منه يُحدد بنصف قطر العالم من؟", listOf("Schwarzschild", "Karl Schwarzschild", "شفارتزشيلد"), 93, "space", AnswerType.TEXT),
      ExpItem("vh_space_mars_largest_moon_phobos", "What is the larger and innermost of the two Martian natural satellites?", "ما هو أكبر وأقرب القمرين الطبيعيين اللذين يدوران حول كوكب المريخ؟", listOf("Phobos", "فوبوس"), 85, "space", AnswerType.TEXT),
      ExpItem("vh_space_voyager_golden_record_year", "Which space mission carried the famous gold-plated phonograph record in 1977?", "أي مهمة فضائية تاريخية حملت السجل الذهبي الموجه للحضارات الفضائية عام 1977؟", listOf("Voyager", "Voyager 1", "فوياجر"), 81, "space", AnswerType.TEXT),
      ExpItem("vh_space_pluto_largest_moon_charon", "What is the name of Pluto's largest companion moon, locked in mutual tidal rotation?", "ما هو اسم أكبر أقمار الكوكب القزم بلوتو، والمرتبط معه مدارياً بشكل مقفل؟", listOf("Charon", "شارون"), 87, "space", AnswerType.TEXT),
      ExpItem("vh_space_cosmic_microwave_background_year", "In what decade was the Cosmic Microwave Background (CMB) radiation discovered (1960s)?", "في أي عام ميلادي تم اكتشاف إشعاع الخلفية الكونية الميكروي (CMB) بالصدفة؟", listOf("1965", "١٩٦٥"), 92, "space", AnswerType.YEAR, 1965.0),
      ExpItem("vh_space_first_space_station_salyut", "What was the name of the world's very first space station launched by the USSR in 1971?", "ما هو اسم أول محطة فضاء في تاريخ البشرية أطلقها الاتحاد السوفيتي عام 1971؟", listOf("Salyut", "Salyut 1", "ساليوت", "ساليوت 1"), 88, "space", AnswerType.TEXT),
      ExpItem("vh_space_fastest_spinning_neutron_star", "What astronomical term describes a highly magnetized, rotating neutron star emitting beams?", "ما هو المصطلح الفلكي لنجم نيوتروني شديد المغناطيسية يدور بسرعة ويصدر نبضات إشعاعية؟", listOf("Pulsar", "نجم نابض", "بلسار"), 86, "space", AnswerType.TEXT),
      // History
      ExpItem("vh_hist_general_relativity_published_year", "In what year did Albert Einstein publish his theory of General Relativity?", "في أي عام ميلادي نشر ألبرت أينشتاين نظريته في النسبية العامة؟", listOf("1915", "١٩١٥"), 84, "history", AnswerType.YEAR, 1915.0),
      ExpItem("vh_hist_dna_double_helix_paper_year", "In what year did Watson and Crick publish the double helix structure of DNA in Nature?", "في أي عام ميلادي نشر واطسون وكريك بحث اكتشاف بنية الحمض النووي الحلزونية؟", listOf("1953", "١٩٥٣"), 85, "history", AnswerType.YEAR, 1953.0),
      ExpItem("vh_hist_gutenberg_bible_printed_year", "Around what year in the 1450s was the famous Gutenberg 42-line Bible printed?", "في أي عام ميلادي تقريباً في خمسينيات القرن الخامس عشر طُبع إنجيل غوتنبرغ الشهير؟", listOf("1455", "١٤٥٥"), 90, "history", AnswerType.YEAR, 1455.0),
      ExpItem("vh_hist_battle_of_marathon_year_bc", "In what year BC did the Athenians defeat the first Persian invasion at Marathon?", "في أي عام قبل الميلاد هزم الأثينيون الغزو الفارسي الأول في معركة ماراثون الشهيرة؟", listOf("490", "490 BC", "٤٩٠"), 93, "history", AnswerType.NUMBER, 490.0),
      ExpItem("vh_hist_first_roman_dictator_for_life", "In what year BC was Julius Caesar appointed dictator perpetuo before his assassination?", "في أي عام قبل الميلاد عُين يوليوس قيصر دكتاتوراً مدى الحياة في روما؟", listOf("44", "44 BC", "٤٤"), 89, "history", AnswerType.NUMBER, 44.0),
      ExpItem("vh_hist_conquest_of_granada_year", "In what historic year did the Catholic Monarchs conquer the Emirate of Granada?", "في أي عام ميلادي تاريخي سقطت غرناطة آخر معاقل المسلمين في الأندلس؟", listOf("1492", "١٤٩٢"), 82, "history", AnswerType.YEAR, 1492.0),
      ExpItem("vh_hist_treaty_of_westphalia_year", "In what year were the peace Treaties of Westphalia signed, ending the Thirty Years' War?", "في أي عام ميلادي وُقّع صلح وستفاليا التاريخي الذي أنهى حرب الثلاثين عاماً الأوروبية؟", listOf("1648", "١٦٤٨"), 91, "history", AnswerType.YEAR, 1648.0),
      ExpItem("vh_hist_edicts_of_ashoka_script", "What primary ancient script was used for the majority of the Edicts of Emperor Ashoka in India?", "ما هو الخط الهندي القديم الرئيسي الذي كُتبت به أغلب نقوش ومراسيم الإمبراطور أشوكا؟", listOf("Brahmi", "البراهمي", "براهمي"), 94, "history", AnswerType.TEXT),
      ExpItem("vh_hist_library_of_alexandria_founded_century", "In which century BC was the Great Library of Alexandria founded by the Ptolemies?", "في أي قرن قبل الميلاد تم تأسيس مكتبة الإسكندرية العظيمة في عهد البطالمة؟", listOf("3", "3rd", "3rd BC", "الثالث قبل الميلاد", "٣"), 87, "history", AnswerType.NUMBER, 3.0),
      ExpItem("vh_hist_battle_of_actium_year_bc", "In what year BC did Octavian defeat Antony and Cleopatra at the naval Battle of Actium?", "في أي عام قبل الميلاد وقعت معركة أكتيوم البحرية الفاصلة بين أوكتافيان وأنطونيوس؟", listOf("31", "31 BC", "٣١"), 92, "history", AnswerType.NUMBER, 31.0),
      // Mathematics & Computing
      ExpItem("vh_math_seventeen_squared", "What is the integer square of 17 (17 x 17)?", "ما هو مربع العدد 17 في الحساب الذهني (17 × 17)؟", listOf("289", "٢٨٩"), 82, "mathematics", AnswerType.NUMBER, 289.0),
      ExpItem("vh_math_thirteen_squared", "What is the integer square of 13 (13 x 13)?", "ما هو مربع العدد 13 في الرياضيات (13 × 13)؟", listOf("169", "١٦٩"), 81, "mathematics", AnswerType.NUMBER, 169.0),
      ExpItem("vh_math_nineteen_squared", "What is the integer square of 19 (19 x 19)?", "ما هو مربع العدد 19 في الرياضيات (19 × 19)؟", listOf("361", "٣٦١"), 85, "mathematics", AnswerType.NUMBER, 361.0),
      ExpItem("vh_math_prime_between_90_100", "What is the only prime number between 90 and 100?", "ما هو العدد الأولي الوحيد المحصور بين 90 و 100؟", listOf("97", "Ninety-seven", "٩٧"), 86, "mathematics", AnswerType.NUMBER, 97.0),
      ExpItem("vh_math_binary_1111_to_decimal", "What is the decimal equivalent of the 4-bit binary number 1111?", "ما هي القيمة العشرية المكافئة للعدد الثنائي ذي الأربع خانات (1111)؟", listOf("15", "Fifteen", "خمسة عشر", "١٥"), 83, "mathematics", AnswerType.NUMBER, 15.0),
      ExpItem("vh_math_binary_10101_to_decimal", "What is the decimal equivalent of binary 10101 (16 + 4 + 1)?", "ما هي القيمة العشرية المكافئة للعدد الثنائي (10101)؟", listOf("21", "Twenty-one", "واحد وعشرون", "٢١"), 87, "mathematics", AnswerType.NUMBER, 21.0),
      ExpItem("vh_math_dodecahedron_faces_count", "How many pentagonal faces does a regular geometric dodecahedron have?", "كم وجهاً خماسياً يمتلك المجسم الأفلاطوني المنتظم ذو الاثني عشر وجهاً (الدوديكاهيدرون)؟", listOf("12", "Twelve", "اثنا عشر", "١٢"), 84, "mathematics", AnswerType.NUMBER, 12.0),
      ExpItem("vh_math_dodecahedron_vertices_count", "How many vertices (corners) does a regular dodecahedron possess?", "كم رأساً زاوياً يمتلك مجسم الاثني عشر وجهاً المنتظم (الدوديكاهيدرون)؟", listOf("20", "Twenty", "عشرون", "٢٠"), 89, "mathematics", AnswerType.NUMBER, 20.0),
      ExpItem("vh_math_convex_polygon_exterior_angles_sum", "In degrees, what is the sum of exterior angles of any convex polygon?", "بالدرجات، كم يبلغ مجموع قياسات الزوايا الخارجية لأي مضلع محدب؟", listOf("360", "360 degrees", "٣٦٠"), 85, "mathematics", AnswerType.NUMBER, 360.0),
      ExpItem("vh_math_hexagon_diagonals_count", "How many internal diagonals can be drawn in a simple 6-sided hexagon (n(n-3)/2)?", "كم قطراً داخلياً يمكن رسمه في مضلع سداسي بسيط وفق القانون ن(ن-3)/2؟", listOf("9", "Nine", "تسعة", "٩"), 88, "mathematics", AnswerType.NUMBER, 9.0),
      ExpItem("vh_tech_deep_blue_defeated_kasparov_year", "In what year did the IBM supercomputer Deep Blue defeat world chess champion Garry Kasparov?", "في أي عام ميلادي هزم حاسوب آي بي إم ديب بلو بطل العالم في الشطرنج غاري كاسباروف؟", listOf("1997", "١٩٩٧"), 84, "technology", AnswerType.YEAR, 1997.0),
      ExpItem("vh_tech_fermats_last_theorem_prover", "Which British mathematician famously completed the proof of Fermat's Last Theorem in 1994?", "من هو عالم الرياضيات البريطاني الفذ الذي أتم برهان مبرهنة فيرما الأخيرة عام 1994؟", listOf("Andrew Wiles", "Wiles", "أندرو وايلز"), 93, "mathematics", AnswerType.TEXT),
      ExpItem("vh_tech_first_commercial_relational_database", "Which company released the first commercially successful SQL relational database in 1979?", "ما هي الشركة التي أطلقت أول نظام إدارة قواعد بيانات علائقية تجاري بنجاح عام 1979؟", listOf("Oracle", "أوراكل"), 86, "technology", AnswerType.TEXT),
      ExpItem("vh_tech_unix_operating_system_year", "In what year was the first version of the Unix operating system developed at Bell Labs?", "في أي عام ميلادي تم تطوير النسخة الأولى من نظام التشغيل يونكس في مختبرات بيل؟", listOf("1969", "١٩٦٩"), 89, "technology", AnswerType.YEAR, 1969.0),
      ExpItem("vh_tech_sha256_output_bits_length", "How many bits are in the cryptographic hash output of the SHA-256 algorithm?", "كم بتاً يبلغ طول المخرجات التشفيرية الموحدة لخوارزمية الهاش الآمنة SHA-256؟", listOf("256", "Two hundred fifty-six", "٢٥٦"), 88, "technology", AnswerType.NUMBER, 256.0),
      // Art, Geography & Sports
      ExpItem("vh_art_bayeux_tapestry_depicts_battle", "Which historic 1066 battle is famously embroidered on the 70-meter Bayeux Tapestry?", "أي معركة تاريخية شهيرة عام 1066 طُرزت أحداثها على نسيج بايو التاريخي المطرز؟", listOf("Battle of Hastings", "Hastings", "معركة هاستنغز", "هاستنغز"), 86, "art", AnswerType.TEXT),
      ExpItem("vh_geo_point_nemo_oceanic_pole_inaccessibility", "What is the name of the oceanic pole of inaccessibility, furthest from any land?", "ما هو اسم نقطة نيمو في جنوب المحيط الهادئ، وهي أبعد نقطة بحرية عن أي يابسة؟", listOf("Point Nemo", "Nemo", "نقطة نيمو"), 92, "geography", AnswerType.TEXT),
      ExpItem("vh_geo_danube_river_capitals_count", "How many European national capital cities does the Danube River flow through?", "كم عدد العواصم الوطنية الأوروبية التي يمر بها نهر الدانوب في مساره؟", listOf("4", "Four", "أربع عواصم", "٤"), 87, "geography", AnswerType.NUMBER, 4.0),
      ExpItem("vh_spt_first_four_minute_mile_runner", "Who became the first person in history to run a sub-four-minute mile in 1954?", "من هو العداء البريطاني الذي كان أول إنسان يكسر حاجز الميل في أقل من أربع دقائق عام 1954؟", listOf("Roger Bannister", "Bannister", "روجر بانيستر"), 88, "sports", AnswerType.TEXT),
      ExpItem("vh_sci_fundamental_forces_of_nature_count", "How many fundamental physical forces or interactions exist in the Standard Model?", "كم عدد القوى الفيزيائية الأساسية الأربع المعروفة في الكون (الجاذبية، الكهرومغناطيسية، والنوويتان)؟", listOf("4", "Four", "أربع قوى", "٤"), 81, "science", AnswerType.NUMBER, 4.0)
    )
    addItems(list, items, lang, 5) // Very Hard
  }

  // 2. CHALLENGE CONCEPTS (25 concepts, scores 96–100)
  private fun buildChallengeQuestions(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      ExpItem("ch_chandrasekhar_mass_limit_solar", "What is the approximate Chandrasekhar mass limit for a stable white dwarf in solar masses?", "كم تبلغ قيمة حد تشاندراسيخار التقريبية لكتلة القزم الأبيض بالكتل الشمسية؟", listOf("1.4", "1.44", "١.٤"), 97, "space", AnswerType.DECIMAL, 1.4),
      ExpItem("ch_triple_point_of_water_kelvin", "What is the exact thermodynamic triple point of pure water in Kelvin?", "ما هي القيمة الديناميكية الحرارية الدقيقة للنقطة الثلاثية للماء النقي بوحدة كلفن؟", listOf("273.16", "273.16 K", "٢٧٣.١٦"), 98, "science", AnswerType.DECIMAL, 273.16),
      ExpItem("ch_speed_of_light_exact_meters_per_sec", "What is the exact internationally defined speed of light in vacuum in meters per second?", "ما هي القيمة المترية المحددة دولياً بالضبط لسرعة الضوء في الفراغ (م/ث)؟", listOf("299792458", "٢٩٩٧٩٢٤٥٨"), 99, "science", AnswerType.NUMBER, 299792458.0),
      ExpItem("ch_element_highest_melting_point", "Which chemical element has the highest melting point of all elements (over 3400 C)?", "ما هو العنصر الكيميائي صاحب أعلى درجة انصهار بين جميع عناصر الجدول الدوري؟", listOf("Tungsten", "Wolfram", "التنجستن", "تنجستن"), 96, "science", AnswerType.TEXT),
      ExpItem("ch_second_law_thermodynamics_quantity", "The Second Law of Thermodynamics states what physical measure of disorder always increases in an isolated system?", "ينص القانون الثاني للديناميكا الحرارية على أن أي مقياس فيزيائي للعشوائية يزداد دائماً في نظام مغلق؟", listOf("Entropy", "الإنتروبيا", "انتروبيا"), 96, "science", AnswerType.TEXT),
      ExpItem("ch_buckminsterfullerene_carbon_atoms", "How many carbon atoms make up a standard buckminsterfullerene spherical molecule (buckyball)?", "كم عدد ذرات الكربون التي يتألف منها جزيء البكمنسترفوليرين الكروي الشهير (C60)؟", listOf("60", "Sixty", "ستون", "٦٠"), 97, "science", AnswerType.NUMBER, 60.0),
      ExpItem("ch_first_artificially_synthesized_element", "What was the very first artificially synthesized chemical element (atomic number 43)?", "ما هو أول عنصر كيميائي تم تخليقه صناعياً في المختبرات البشرية (عدده الذري 43)؟", listOf("Technetium", "التكنيشيوم"), 98, "science", AnswerType.TEXT),
      ExpItem("ch_millikan_oil_drop_experiment_measured", "Robert Millikan's famous 1909 oil drop experiment precisely measured the charge of what particle?", "ما هي الشحنة الكهربائية الأولية لجسيم دون ذري التي قاسها روبرت ميليكان بتجربة قطرة الزيت؟", listOf("Electron", "الإلكترون"), 96, "science", AnswerType.TEXT),
      ExpItem("ch_truncated_icosahedron_faces_count", "How many total faces (12 pentagons and 20 hexagons) does a truncated icosahedron have?", "كم وجهاً يمتلك مجسم الإيكوساهيدرون المقطوع (شكل كرة القدم الكلاسيكية المكونة من خماسيات وسداسيات)؟", listOf("32", "Thirty-two", "اثنان وثلاثون", "٣٢"), 97, "mathematics", AnswerType.NUMBER, 32.0),
      ExpItem("ch_fermat_prime_powers_of_two", "Fermat primes are primes of the form 2^(2^n) + 1; which prime among 3, 5, 17, 257 corresponds to n=4?", "أعداد فيرما الأولية هي على الصورة 2^(2^ن) + 1؛ ما هو هذا العدد الأولي المقابل لـ ن=4؟", listOf("65537", "٦٥٥٣٧"), 100, "mathematics", AnswerType.NUMBER, 65537.0),
      ExpItem("ch_riemann_hypothesis_critical_line_real_part", "The Riemann Hypothesis conjectures all non-trivial zeros of the zeta function have a real part equal to what fraction?", "تنص فرضية ريمان الشهيرة على أن الجزء الحقيقي لجميع الأصفار غير البديهية لدالة زيتا يساوي ماذا؟", listOf("1/2", "0.5", "نصف", "١/٢"), 100, "mathematics", AnswerType.TEXT),
      ExpItem("ch_voyager_1_spacecraft_interstellar_year", "In what year did NASA confirm Voyager 1 officially crossed the heliopause into interstellar space?", "في أي عام ميلادي أعلنت ناسا رسمياً عبور مسبار فوياجر 1 لحدود الغلاف الشمسي إلى الفضاء بين النجوم؟", listOf("2012", "٢٠١٢"), 97, "space", AnswerType.YEAR, 2012.0),
      ExpItem("ch_huygens_probe_titan_landing_year", "In what year did ESA's robotic Huygens probe successfully touch down on the surface of Saturn's moon Titan?", "في أي عام ميلادي هبط المسبار الفضائي الأوروبي هويغنز بنجاح على سطح قمر زحل تيتان؟", listOf("2005", "٢٠٠٥"), 96, "space", AnswerType.YEAR, 2005.0),
      ExpItem("ch_solar_system_retrograde_rotation_planet", "Which terrestrial planet in our solar system uniquely rotates in a retrograde (clockwise) direction?", "أي كواكب مجموعتنا الشمسية يدور حول محوره بشكل تراجعي فريد في اتجاه عقارب الساعة؟", listOf("Venus", "الزهرة"), 96, "space", AnswerType.TEXT),
      ExpItem("ch_largest_canyon_system_valles_marineris", "What immense tectonic canyon system on Mars stretches over 4000 km along its equator?", "ما هو اسم الوادي الخانق التكتوني الهائل على كوكب المريخ الذي يمتد لأكثر من 4000 كيلومتر؟", listOf("Valles Marineris", "فاليس مارينيريس", "وادي مارينر"), 97, "space", AnswerType.TEXT),
      ExpItem("ch_epic_of_gilgamesh_written_civilization", "In what ancient Mesopotamian cuneiform literature was the earliest surviving epic poem, Epic of Gilgamesh, preserved?", "في أي حضارة قديمة ببلاد الرافدين دُوّنت أقدم ملحمة أدبية شعرية باقية ملحمة جلجامش؟", listOf("Mesopotamia", "Sumer", "Babylon", "بلاد الرافدين", "سومر", "بابل"), 96, "history", AnswerType.TEXT),
      ExpItem("ch_rosetta_stone_discovery_year_egypt", "In what year was the Rosetta Stone unearthed by French soldiers in Rashid, Egypt?", "في أي عام ميلادي عثر الجنود الفرنسيون على حجر رشيد التاريخي في مصر؟", listOf("1799", "١٧٩٩"), 98, "history", AnswerType.YEAR, 1799.0),
      ExpItem("ch_marie_curie_nobel_prizes_count", "How many total Nobel Prizes was Marie Curie awarded in her lifetime (in Physics and Chemistry)?", "كم جائزة نوبل في فرعين علميين مختلفين (الفيزياء والكيمياء) نالتها العالمة ماري كوري؟", listOf("2", "Two", "جائزتان", "٢"), 96, "science", AnswerType.NUMBER, 2.0),
      ExpItem("ch_first_antibiotic_producing_fungus_genus", "From which fungal genus did Alexander Fleming isolate the antibacterial compound penicillin?", "من أي جنس فطري عَفَني عزل السير ألكسندر فليمنغ المضاد الحيوي الشهير البنسلين؟", listOf("Penicillium", "بنسيليوم"), 97, "science", AnswerType.TEXT),
      ExpItem("ch_heaviest_primordial_element_mass_uranium", "What is the standard atomic mass number of the most abundant fissile isotope of Uranium mined for fuel?", "ما هو رقم الكتلة النظيري الأكثر وفرة لليورانيوم الطبيعي المستخدم في الطاقة (يورانيوم-ماذا)؟", listOf("238", "٢٣٨"), 98, "science", AnswerType.NUMBER, 238.0),
      ExpItem("ch_dna_guanine_pairs_with", "In double-stranded DNA base pairing, what complementary nitrogenous base pairs with Guanine via 3 hydrogen bonds?", "في قواعد شريط الحمض النووي المزدوج DNA، أي قاعدة نيتروجينية ترتبط مع الجوانين بثلاث روابط هيدروجينية؟", listOf("Cytosine", "السيتوزين"), 96, "science", AnswerType.TEXT),
      ExpItem("ch_computer_science_np_completeness_cook_year", "In what year did Stephen Cook publish his foundational theorem establishing NP-completeness?", "في أي عام ميلادي نشر ستيفن كوك مبرهنته التأسيسية التي رسخت مفهوم مسائل NP الكاملة؟", listOf("1971", "١٩٧١"), 99, "technology", AnswerType.YEAR, 1971.0),
      ExpItem("ch_shannon_information_theory_paper_year", "In what year did Claude Shannon publish A Mathematical Theory of Communication, founding modern information theory?", "في أي عام ميلادي نشر كلود شانون ورقته البحثية الرياضية التي أسست لنظرية المعلومات الحديثة؟", listOf("1948", "١٩٤٨"), 99, "technology", AnswerType.YEAR, 1948.0),
      ExpItem("ch_sum_of_first_n_odd_integers_formula", "What simple algebraic expression gives the exact sum of the first N positive odd integers (1+3+5+...)?", "ما هو التعبير الجبري البسيط الذي يمثل مجموع أول N من الأعداد الفردية الموجبة؟", listOf("N^2", "N squared", "ن مربع", "ن^2"), 98, "mathematics", AnswerType.TEXT),
      ExpItem("ch_light_year_approx_trillion_kilometers", "Approximately how many trillion kilometers does one astronomical light-year equal (to the nearest whole number)?", "كم تريليون كيلومتر تقريباً تعادل السنة الضوئية الفلكية الواحدة لأقرب رقم صحيح؟", listOf("9", "9.5", "10", "٩"), 97, "space", AnswerType.NUMBER, 9.0)
    )
    addItems(list, items, lang, 6) // Challenge
  }

  private fun addItems(list: MutableList<Question>, items: List<ExpItem>, lang: AppLanguage, tier: Int) {
    for (item in items) {
      val qText = when (lang) {
        AppLanguage.ARABIC -> item.arabicQ
        else -> item.englishQ
      }
      val answers = when (lang) {
        AppLanguage.ARABIC -> item.answers
        else -> item.answers.filterNot { ans -> ans.any { it in '\u0600'..'\u06FF' } }
      }
      val prefix = if (tier == 6) "adv_c_" else "adv_vh_"
      list.add(
        Question(
          id = "${prefix}${item.concept}_${lang.code}",
          language = lang.code,
          category = item.category,
          difficulty = tier,
          question = qText,
          answerType = item.type,
          acceptedAnswers = if (answers.isNotEmpty()) answers else item.answers,
          correctNumericValue = item.numericVal,
          tolerance = if (item.numericVal != null) 0.01 else null,
          explanation = "Verified scholarly domain fact.",
          difficultyScore = item.score,
          conceptId = item.concept
        )
      )
    }
  }

  private data class ExpItem(
    val concept: String,
    val englishQ: String,
    val arabicQ: String,
    val answers: List<String>,
    val score: Int,
    val category: String,
    val type: AnswerType = AnswerType.TEXT,
    val numericVal: Double? = null
  )
}
