package com.example.data.questions

import com.example.data.model.AnswerType
import com.example.data.model.AppLanguage
import com.example.data.model.Question

/**
 * AdvancedHardCatalog provides 110 genuinely distinct Hard concepts (scores 61–80).
 * Tests deep factual knowledge, reasoning, and verifiable historical/scientific facts.
 */
object AdvancedHardCatalog {

  fun getCatalog(language: AppLanguage): List<Question> {
    val list = mutableListOf<Question>()
    buildHardScienceChemistry(list, language)
    buildHardSpacePhysics(list, language)
    buildHardHistoryTreaties(list, language)
    buildHardTechComputing(list, language)
    buildHardMathematicsLogic(list, language)
    buildHardArtLiterature(list, language)
    buildHardGeographyNature(list, language)
    buildHardCulinarySports(list, language)
    return list
  }

  // 1. HARD SCIENCE & CHEMISTRY (15 concepts, scores 61-78)
  private fun buildHardScienceChemistry(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      Item("sci_gold_chemical_symbol", "What is the chemical symbol for Gold on the periodic table?", "ما هو الرمز الكيميائي لعنصر الذهب في الجدول الدوري للعناصر؟", listOf("Au"), 66, "science", AnswerType.TEXT),
      Item("sci_silver_chemical_symbol", "What is the chemical symbol for Silver on the periodic table?", "ما هو الرمز الكيميائي لعنصر الفضة في الجدول الدوري للعناصر؟", listOf("Ag"), 67, "science", AnswerType.TEXT),
      Item("sci_lead_chemical_symbol", "What is the chemical symbol for Lead (from Latin plumbum)?", "ما هو الرمز الكيميائي لعنصر الرصاص المشتق من اسمه اللاتيني بلومبوم؟", listOf("Pb"), 69, "science", AnswerType.TEXT),
      Item("sci_tungsten_chemical_symbol", "What is the chemical symbol for Tungsten on the periodic table?", "ما هو الرمز الكيميائي لعنصر التنجستن (الفولفرام) في الجدول الدوري؟", listOf("W"), 72, "science", AnswerType.TEXT),
      Item("sci_mercury_chemical_symbol", "What is the chemical symbol for liquid metal Mercury?", "ما هو الرمز الكيميائي لعنصر الزئبق السائل (الهيدرارجيروم)؟", listOf("Hg"), 70, "science", AnswerType.TEXT),
      Item("sci_tin_chemical_symbol", "What is the chemical symbol for Tin (from Latin stannum)?", "ما هو الرمز الكيميائي لعنصر القصدير المشتق من اللاتينية ستانوم؟", listOf("Sn"), 71, "science", AnswerType.TEXT),
      Item("sci_potassium_atomic_number", "What is the atomic number of the element Potassium (K)?", "ما هو العدد الذري لعنصر البوتاسيوم (K) في الجدول الدوري؟", listOf("19", "١٩"), 68, "science", AnswerType.NUMBER, 19.0),
      Item("sci_iron_atomic_number", "What is the atomic number of Iron (Fe)?", "ما هو العدد الذري لعنصر الحديد (Fe) في الجدول الدوري؟", listOf("26", "٢٦"), 65, "science", AnswerType.NUMBER, 26.0),
      Item("sci_copper_atomic_number", "What is the atomic number of Copper (Cu)?", "ما هو العدد الذري لعنصر النحاس (Cu) في الجدول الدوري؟", listOf("29", "٢٩"), 69, "science", AnswerType.NUMBER, 29.0),
      Item("sci_gold_atomic_number", "What is the atomic number of the precious metal Gold (Au)?", "ما هو العدد الذري لعنصر الذهب النبيل (Au) في الجدول الدوري؟", listOf("79", "٧٩"), 76, "science", AnswerType.NUMBER, 79.0),
      Item("sci_avogadro_number_exponent", "In Avogadro's constant (6.022 x 10^N), what is the power of 10 exponent N?", "في ثابت أفوجادرو الشهير (6.022 × 10 أس N)، ما هي قيمة الأس N؟", listOf("23", "٢٣"), 75, "science", AnswerType.NUMBER, 23.0),
      Item("sci_halogens_periodic_group", "Which group number in the modern periodic table contains the halogens (F, Cl, Br, I)?", "ما هو رقم المجموعة في الجدول الدوري الحديث التي تضم الهالوجينات؟", listOf("17", "Seventeen", "١٧"), 73, "science", AnswerType.NUMBER, 17.0),
      Item("sci_noble_gases_periodic_group", "Which group number in the periodic table contains the inert noble gases?", "ما هو رقم المجموعة في الجدول الدوري التي تضم الغازات النبيلة الخاملة؟", listOf("18", "Eighteen", "١٨"), 67, "science", AnswerType.NUMBER, 18.0),
      Item("sci_subatomic_particle_negative_charge", "Which subatomic particle carries a negative electric elementary charge?", "أي جسيم دون ذري يحمل الشحنة الكهربائية الأولية السالبة؟", listOf("Electron", "الإلكترون", "الكترون"), 62, "science", AnswerType.TEXT),
      Item("sci_strongest_acid_ph_lower_than", "On the pH scale, solutions with a pH less than what number are considered acidic?", "على مقياس الرقم الهيدروجيني pH، المحاليل التي يقل رقمها عن أي عدد تُعتبر حمضية؟", listOf("7", "Seven", "سبعة", "٧"), 61, "science", AnswerType.NUMBER, 7.0)
    )
    addItems(list, items, lang)
  }

  // 2. HARD SPACE & PHYSICS (15 concepts, scores 62-80)
  private fun buildHardSpacePhysics(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      Item("space_olympus_mons_planet", "On which planet in our solar system is the towering shield volcano Olympus Mons located?", "على أي كوكب في مجموعتنا الشمسية يقع البركان العملاق أوليمبوس مونس؟", listOf("Mars", "المريخ"), 64, "space", AnswerType.TEXT),
      Item("space_first_human_spaceflight_year", "In what year did Yuri Gagarin become the first human to fly into space?", "في أي عام ميلادي أصبح يوري غاغارين أول إنسان يسافر إلى الفضاء الخارجي؟", listOf("1961", "١٩٦١"), 66, "space", AnswerType.YEAR, 1961.0),
      Item("space_first_woman_in_space", "Who was the first woman to travel into outer space in 1963?", "من هي أول امرأة تسافر إلى الفضاء الخارجي وتدور حول الأرض عام 1963؟", listOf("Valentina Tereshkova", "Tereshkova", "فالنتينا تيريشكوفا"), 73, "space", AnswerType.TEXT),
      Item("space_hubble_telescope_launch_year", "In what year was the Hubble Space Telescope launched into low Earth orbit?", "في أي عام ميلادي تم إطلاق تلسكوب هابل الفضائي إلى مدار الأرض؟", listOf("1990", "١٩٩٠"), 69, "space", AnswerType.YEAR, 1990.0),
      Item("space_voyager_1_launch_year", "In what year was the historic interstellar space probe Voyager 1 launched by NASA?", "في أي عام ميلادي أطلقت ناسا المسبار الفضائي التاريخي فوياجر 1؟", listOf("1977", "١٩٧٧"), 74, "space", AnswerType.YEAR, 1977.0),
      Item("space_speed_of_light_km_per_sec", "To the nearest thousand, how many kilometers per second does light travel in vacuum?", "كم ألف كيلومتر في الثانية تقريباً تبلغ سرعة الضوء في الفراغ؟", listOf("300000", "300,000", "٣٠٠٠٠٠"), 71, "space", AnswerType.NUMBER, 300000.0),
      Item("space_solar_system_highest_cliff_miranda", "Miranda, home to Verona Rupes (tallest cliff in solar system), orbits which planet?", "أي كواكب المجموعة الشمسية يدور حوله القمر ميراندا صاحب أعلى جرف صخري؟", listOf("Uranus", "أورانوس"), 77, "space", AnswerType.TEXT),
      Item("space_saturn_largest_moon", "What is the largest moon orbiting the ringed planet Saturn?", "ما هو أكبر أقمار كوكب زحل، ويتميز بوجود غلاف جوي كثيف وبحيرات ميثان؟", listOf("Titan", "تيتان"), 63, "space", AnswerType.TEXT),
      Item("space_neptune_largest_moon", "What is the largest moon of Neptune, famous for its retrograde orbit?", "ما هو أكبر أقمار كوكب نبتون، والذي يتميز بمداره التراجعي المعاكس؟", listOf("Triton", "تريتون"), 75, "space", AnswerType.TEXT),
      Item("space_jupiter_volcanic_moon", "Which of Jupiter's Galilean moons is the most volcanically active body in the solar system?", "أي أقمار المشتري الأربعة الكبرى يُعتبر الجرم الأكثر نشاطاً بركانياً في النظام الشمسي؟", listOf("Io", "آيو"), 72, "space", AnswerType.TEXT),
      Item("space_jupiter_icy_ocean_moon", "Which smooth, icy moon of Jupiter harbors a deep liquid water ocean beneath its crust?", "أي أقمار المشتري الجليدية الملساء يخفي محيطاً مائياً شاسعاً تحت قشرته؟", listOf("Europa", "أوروبا"), 70, "space", AnswerType.TEXT),
      Item("space_kuiper_belt_discovered_year", "In what decade was the Kuiper belt outside Neptune confirmed with 1992 QB1?", "في أي عام ميلادي تم تأكيد اكتشاف أول جرم في حزام كايبر بعد بلوتو (1992)؟", listOf("1992", "١٩٩٢"), 79, "space", AnswerType.YEAR, 1992.0),
      Item("space_cassini_spacecraft_destination", "Which ringed giant planet was explored extensively by the Cassini-Huygens mission?", "أي كوكب عملاق استكشفته مهمة مسبار كاسيني-هويغنز الفضائية بالتفصيل؟", listOf("Saturn", "زحل"), 65, "space", AnswerType.TEXT),
      Item("space_first_exoplanet_discovered_star", "In 1995, 51 Pegasi b became the first confirmed exoplanet around what type of star?", "عام 1995، كان 51 بيغاسي بي أول كوكب خارج المجموعة الشمسية يُكتشف حول نجم شبيه بماذا؟", listOf("Sun", "Sun-like star", "الشمس", "نجم شبيه بالشمس"), 78, "space", AnswerType.TEXT),
      Item("space_crater_chicxulub_dinosaur_extinction", "On which Mexican peninsula is the Chicxulub asteroid impact crater located?", "في أي شبه جزيرة مكسيكية تقع فوهة تشيكشولوب الناتجة عن الكويكب الذي أباد الديناصورات؟", listOf("Yucatan", "Yucatan Peninsula", "يوكاتان", "شبه جزيرة يوكاتان"), 76, "space", AnswerType.TEXT)
    )
    addItems(list, items, lang)
  }

  // 3. HARD HISTORY & TREATIES (15 concepts, scores 62-80)
  private fun buildHardHistoryTreaties(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      Item("hist_magna_carta_signed_year", "In what year was the historic Magna Carta granted by King John of England?", "في أي عام ميلادي وقّع الملك جون وثيقة الماغنا كارتا (العهد الأعظم)؟", listOf("1215", "١٢١٥"), 67, "history", AnswerType.YEAR, 1215.0),
      Item("hist_treaty_of_versailles_year", "In what year was the Treaty of Versailles signed, formally concluding World War I?", "في أي عام ميلادي وُقّعت معاهدة فرساي التي أنهت الحرب العالمية الأولى رسمياً؟", listOf("1919", "١٩١٩"), 68, "history", AnswerType.YEAR, 1919.0),
      Item("hist_constantinople_fall_year", "In what year did the Ottoman Empire under Sultan Mehmed II conquer Constantinople?", "في أي عام ميلادي فتح العثمانيون بقيادة السلطان محمد الفاتح مدينة القسطنطينية؟", listOf("1453", "١٤٥٣"), 69, "history", AnswerType.YEAR, 1453.0),
      Item("hist_battle_of_hastings_year", "In what year did the Norman Conquest of England occur at the Battle of Hastings?", "في أي عام ميلادي وقعت معركة هاستنغز وبدأ الغزو النورماندي لإنجلترا؟", listOf("1066", "١٠٦٦"), 71, "history", AnswerType.YEAR, 1066.0),
      Item("hist_russian_revolution_year", "In what year did the Bolshevik October Revolution occur in Russia under Lenin?", "في أي عام ميلادي اندلعت الثورة البلشفية في روسيا بقيادة فلاديمير لينين؟", listOf("1917", "١٩١٧"), 66, "history", AnswerType.YEAR, 1917.0),
      Item("hist_meiji_restoration_year", "In what year did the Meiji Restoration begin modernization in Japan?", "في أي عام ميلادي بدأت فترة استعادة مييجي التاريخية لتحديث اليابان؟", listOf("1868", "١٨٦٨"), 77, "history", AnswerType.YEAR, 1868.0),
      Item("hist_peloponnesian_war_rivals", "Which two ancient Greek city-states fought each other in the Peloponnesian War?", "أي مدينتين يونانيتين قديمتين تحاربتا في الحرب البيلوبونيزية الشهيرة؟", listOf("Athens and Sparta", "Sparta and Athens", "أثينا وإسبرطة"), 70, "history", AnswerType.TEXT),
      Item("hist_code_of_hammurabi_civilization", "Which ancient Mesopotamian civilization created the legal Code of Hammurabi?", "أي حضارة قديمة في بلاد ما بين النهرين وضعت شريعة وقوانين حمورابي؟", listOf("Babylon", "Babylonian", "بابل", "الحضارة البابلية"), 64, "history", AnswerType.TEXT),
      Item("hist_first_roman_emperor", "Who was the very first Emperor of the Roman Empire, previously named Octavian?", "من هو أول إمبراطور للإمبراطورية الرومانية، وكان يُعرف سابقاً باسم أوكتافيوس؟", listOf("Augustus", "Augustus Caesar", "أغسطس", "أغسطس قيصر"), 65, "history", AnswerType.TEXT),
      Item("hist_first_ruler_unified_china", "Which emperor unified China and founded the Qin dynasty in 221 BC?", "من هو الإمبراطور الأول الذي وحد الصين وأسس سلالة تشين عام 221 قبل الميلاد؟", listOf("Qin Shi Huang", "تشين شي هوانغ"), 74, "history", AnswerType.TEXT),
      Item("hist_rosetta_stone_decipherer", "Which French scholar successfully deciphered Egyptian hieroglyphs in 1822?", "من هو العالم الفرنسي الذي نجح في فك رموز اللغة الهيروغليفية عام 1822؟", listOf("Jean-Francois Champollion", "Champollion", "شامبليون", "جان فرانسوا شامبليون"), 73, "history", AnswerType.TEXT),
      Item("hist_treaty_of_tordesillas_year", "In what year was the Treaty of Tordesillas dividing the New World signed?", "في أي عام ميلادي وُقّعت معاهدة توردسيلاس لتقسيم العالم الجديد بين إسبانيا والبرتغال؟", listOf("1494", "١٤٩٤"), 78, "history", AnswerType.YEAR, 1494.0),
      Item("hist_hundred_years_war_duration", "How many years did the Hundred Years' War between England and France actually last?", "كم سنة استمرت حرب المئة عام الشهيرة بين إنجلترا وفرنسا في الواقع؟", listOf("116", "١١٦"), 72, "history", AnswerType.NUMBER, 116.0),
      Item("hist_saladin_recaptured_jerusalem_year", "In what year did Saladin recapture Jerusalem from the Crusaders?", "في أي عام ميلادي نجح القائد صلاح الدين الأيوبي في استعادة القدس من الصليبيين؟", listOf("1187", "١١٨٧"), 71, "history", AnswerType.YEAR, 1187.0),
      Item("hist_us_civil_war_ended_year", "In what year did the American Civil War end with Lee's surrender at Appomattox?", "في أي عام ميلادي انتهت الحرب الأهلية الأمريكية باستسلام روبرت لي في أبوماتوكس؟", listOf("1865", "١٨٦٥"), 68, "history", AnswerType.YEAR, 1865.0)
    )
    addItems(list, items, lang)
  }

  // 4. HARD TECH & COMPUTING (15 concepts, scores 61-80)
  private fun buildHardTechComputing(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      Item("tech_eniac_operational_year", "In what year did the ENIAC, the first programmable general-purpose computer, become operational?", "في أي عام ميلادي تم تشغيل حاسوب إينياك (ENIAC) أول حاسوب رقمي إلكتروني للأغراض العامة؟", listOf("1945", "1946", "١٩٤٥"), 72, "technology", AnswerType.YEAR, 1945.0),
      Item("tech_turing_machine_paper_year", "In what year did Alan Turing publish his landmark paper introducing the Turing machine?", "في أي عام ميلادي نشر آلان تورنغ ورقته العلمية التأسيسية عن آلة تورنغ؟", listOf("1936", "١٩٣٦"), 79, "technology", AnswerType.YEAR, 1936.0),
      Item("tech_tcp_ip_protocols_creators", "Who is widely recognized alongside Vint Cerf as co-inventor of TCP/IP protocol suite?", "من هو عالم الحاسوب الأمريكي المعترف به مع فينت سيرف كشريك في ابتكار بروتوكول TCP/IP؟", listOf("Bob Kahn", "Robert Kahn", "بوب كان"), 76, "technology", AnswerType.TEXT),
      Item("tech_git_version_control_creator", "Who created the distributed version control system Git in 2005?", "من هو مطور البرمجيات الفنلندي الذي ابتكر نظام التحكم في الإصدارات غيت (Git) عام 2005؟", listOf("Linus Torvalds", "Torvalds", "لينوس تورفالدس"), 64, "technology", AnswerType.TEXT),
      Item("tech_rsa_encryption_letters_mean", "What are the surnames of the three cryptographers who created RSA encryption in 1977?", "ما هي الحروف الأولى لأسماء العلماء الثلاثة الذين ابتكروا تشفير RSA الشهير عام 1977؟", listOf("Rivest Shamir Adleman", "Rivest, Shamir, and Adleman", "ريفست وشامير وأدلمان"), 80, "technology", AnswerType.TEXT),
      Item("tech_c_plus_plus_creator", "Which Danish computer scientist designed and implemented C++ in 1985?", "من هو عالم الحاسوب الدنماركي الذي صمم وطور لغة سي بلس بلس (C++) عام 1985؟", listOf("Bjarne Stroustrup", "Stroustrup", "بيارن ستروستروب"), 75, "technology", AnswerType.TEXT),
      Item("tech_java_language_creator_gosling", "Which Canadian computer scientist created the Java programming language at Sun Microsystems?", "من هو عالم الحاسوب الكندي الذي ابتكر لغة البرمجة جافا (Java) في شركة صن مايكروسيستمز؟", listOf("James Gosling", "Gosling", "جيمس غوسلينغ"), 71, "technology", AnswerType.TEXT),
      Item("tech_javascript_created_days", "How many days did Brendan Eich famously take to create the first version of JavaScript in 1995?", "كم يوماً استغرق برندان آيك في ابتكار النسخة الأولى من لغة جافاسكريبت عام 1995؟", listOf("10", "Ten", "عشرة أيام", "١٠"), 77, "technology", AnswerType.NUMBER, 10.0),
      Item("tech_ipv4_address_bits_length", "How many bits are in a standard IPv4 internet protocol address?", "كم بتاً يتكون منه عنوان بروتوكول الإنترنت القياسي للإصدار الرابع (IPv4)؟", listOf("32", "Thirty-two", "اثنان وثلاثون", "٣٢"), 65, "technology", AnswerType.NUMBER, 32.0),
      Item("tech_ipv6_address_bits_length", "How many bits are in a modern IPv6 internet protocol address?", "كم بتاً يتكون منه عنوان بروتوكول الإنترنت الحديث للإصدار السادس (IPv6)؟", listOf("128", "One hundred twenty-eight", "١٢٨"), 70, "technology", AnswerType.NUMBER, 128.0),
      Item("tech_ascii_standard_character_bits", "How many bits are used to encode a character in standard original ASCII?", "كم بتاً كان يُستخدم لترميز الحرف الواحد في جدول معايير أسكي (ASCII) القياسي الأصلي؟", listOf("7", "Seven", "سبعة", "٧"), 73, "technology", AnswerType.NUMBER, 7.0),
      Item("tech_first_microprocessor_intel_model", "What was the model number of the world's first commercial single-chip microprocessor released by Intel in 1971?", "ما هو رقم الطراز لأول معالج دقيق تجاري أحادي الشريحة أطلقته شركة إنتل عام 1971؟", listOf("4004", "Intel 4004", "٤٠٠٤"), 78, "technology", AnswerType.NUMBER, 4004.0),
      Item("tech_moores_law_transistor_period_months", "Moore's Law originally predicted transistor density on microchips doubles every how many months approximately?", "كم شهراً تقريباً توقع قانون مور أن تتضاعف كثافة الترانزستورات على الشرائح الإلكترونية خلالها؟", listOf("24", "18", "18-24", "٢٤"), 74, "technology", AnswerType.NUMBER, 24.0),
      Item("tech_dns_port_number_standard", "What standard UDP port number is used for Domain Name System (DNS) queries?", "ما هو رقم منفذ بروتوكول UDP القياسي المستخدم لاستعلامات نظام أسماء النطاقات (DNS)؟", listOf("53", "Fifty-three", "٥٣"), 76, "technology", AnswerType.NUMBER, 53.0),
      Item("tech_https_port_number_standard", "What is the standard TCP port number for secure web traffic using HTTPS?", "ما هو رقم منفذ بروتوكول TCP الافتراضي لحركة الويب الآمنة المشفرة عبر HTTPS؟", listOf("443", "Four hundred forty-three", "٤٤٣"), 67, "technology", AnswerType.NUMBER, 443.0)
    )
    addItems(list, items, lang)
  }

  // 5. HARD MATHEMATICS & LOGIC (15 concepts, scores 61-80)
  private fun buildHardMathematicsLogic(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      Item("math_two_to_power_ten", "What is the exact integer value of 2 raised to the power of 10?", "ما هي القيمة العددية الدقيقة للعدد 2 مرفوعاً للأس 10 (2^10)؟", listOf("1024", "١٠٢٤"), 64, "mathematics", AnswerType.NUMBER, 1024.0),
      Item("math_two_to_power_eight", "What is the integer value of 2 raised to the power of 8 (2^8)?", "ما هي القيمة العددية الدقيقة للعدد 2 مرفوعاً للأس 8 (2^8)؟", listOf("256", "٢٥٦"), 62, "mathematics", AnswerType.NUMBER, 256.0),
      Item("math_prime_number_after_47", "What is the first prime number greater than 47?", "ما هو أول عدد أولي يأتي مباشرة بعد العدد 47؟", listOf("53", "Fifty-three", "٥٣"), 68, "mathematics", AnswerType.NUMBER, 53.0),
      Item("math_prime_number_after_73", "What is the first prime number greater than 73?", "ما هو أول عدد أولي يأتي مباشرة بعد العدد 73؟", listOf("79", "Seventy-nine", "٧٩"), 71, "mathematics", AnswerType.NUMBER, 79.0),
      Item("math_hexagon_interior_angles_sum", "What is the sum of interior angles in a six-sided regular hexagon in degrees?", "ما هو مجموع قياسات الزوايا الداخلية للشكل السداسي بالدرجات؟", listOf("720", "720 degrees", "٧٢٠"), 66, "mathematics", AnswerType.NUMBER, 720.0),
      Item("math_octagon_interior_angles_sum", "What is the sum of interior angles in an eight-sided regular octagon in degrees?", "ما هو مجموع قياسات الزوايا الداخلية للشكل الثماني بالدرجات؟", listOf("1080", "1080 degrees", "١٠٨٠"), 70, "mathematics", AnswerType.NUMBER, 1080.0),
      Item("math_icosahedron_faces_count", "How many triangular faces does a regular icosahedron have?", "كم وجهاً مثلثاً يمتلك المجسم الأفلاطوني المنتظم ذو العشرين وجهاً (الإيكوساهيدرون)؟", listOf("20", "Twenty", "عشرون", "٢٠"), 74, "mathematics", AnswerType.NUMBER, 20.0),
      Item("math_cube_vertices_count", "How many vertices (corners) does a geometric three-dimensional cube have?", "كم رأساً (زاوية) يمتلك المكعب الهندسي ثلاثي الأبعاد؟", listOf("8", "Eight", "ثمانية", "٨"), 61, "mathematics", AnswerType.NUMBER, 8.0),
      Item("math_cube_edges_count", "How many straight edges does a three-dimensional cube have?", "كم حرفاً (ضلعاً مستقيماً) يمتلك المكعب الهندسي ثلاثي الأبعاد؟", listOf("12", "Twelve", "اثنا عشر", "١٢"), 63, "mathematics", AnswerType.NUMBER, 12.0),
      Item("math_pythagorean_hypotenuse_5_12", "In a right-angled triangle with legs of length 5 and 12, what is the hypotenuse length?", "في مثلث قائم الزاوية طول ضلعي القائمة فيه 5 و 12، كم يبلغ طول الوتر؟", listOf("13", "Thirteen", "ثلاثة عشر", "١٣"), 67, "mathematics", AnswerType.NUMBER, 13.0),
      Item("math_golden_ratio_symbol_phi", "Which Greek letter is standardly used to designate the Golden Ratio (1.618...)?", "ما هو الحرف اليوناني المستخدم كرمز للنسبة الذهبية (1.618...)؟", listOf("Phi", "في", "فاي"), 69, "mathematics", AnswerType.TEXT),
      Item("math_eulers_number_two_decimals", "What is Euler's mathematical constant e rounded to two decimal places?", "ما هي قيمة الثابت الرياضي النيبيري e مقرباً إلى رقمين عشريين؟", listOf("2.72", "2.718", "٢.٧٢"), 73, "mathematics", AnswerType.DECIMAL, 2.72),
      Item("math_factorial_five_value", "What is the numerical value of 5 factorial (5!)?", "ما هي القيمة العددية لمضروب العدد خمسة (5!)؟", listOf("120", "One hundred twenty", "١٢٠"), 65, "mathematics", AnswerType.NUMBER, 120.0),
      Item("math_fibonacci_eighth_number", "In the Fibonacci sequence starting 1, 1, 2, 3, 5, 8, what is the 8th term?", "في متتالية فيبوناتشي (1، 1، 2، 3، 5، 8، 13...)، ما هو الحد الثامن في المتتالية؟", listOf("21", "Twenty-one", "واحد وعشرون", "٢١"), 69, "mathematics", AnswerType.NUMBER, 21.0),
      Item("math_perfect_number_smallest", "What is the smallest positive perfect number (equal to the sum of its proper divisors)?", "ما هو أصغر عدد مثالي موجب (يساوي مجموع قواسمه الفعلية الصحيحة)؟", listOf("6", "Six", "ستة", "٦"), 76, "mathematics", AnswerType.NUMBER, 6.0)
    )
    addItems(list, items, lang)
  }

  // 6. HARD ART, ARCHITECTURE & LITERATURE (10 concepts, scores 61-78)
  private fun buildHardArtLiterature(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      Item("art_school_of_athens_painter", "Which High Renaissance master painted the fresco The School of Athens in the Vatican?", "من هو رسام عصر النهضة العبقري صاحب لوحة جدارية مدرسة أثينا في الفاتيكان؟", listOf("Raphael", "رافاييل"), 67, "art", AnswerType.TEXT),
      Item("art_birth_of_venus_painter", "Which Italian master painted the early Renaissance masterpiece The Birth of Venus?", "من هو الرسام الإيطالي صاحب اللوحة الشهيرة ولادة فينوس؟", listOf("Sandro Botticelli", "Botticelli", "ساندرو بوتيتشيلي", "بوتيتشيلي"), 66, "art", AnswerType.TEXT),
      Item("art_hagia_sophia_built_emperor", "Which Byzantine Emperor commissioned the grand reconstruction of Hagia Sophia in 537 AD?", "أي إمبراطور بيزنطي أمر ببناء كاتدرائية آيا صوفيا العظيمة عام 537 م؟", listOf("Justinian", "Justinian I", "جستنيان", "جستنيان الأول"), 72, "art", AnswerType.TEXT),
      Item("art_parthenon_architect_phidias", "Which legendary ancient sculptor directed the artistic sculpting program of the Parthenon?", "من هو النحات الإغريقي العظيم الذي أشرف على نحت تماثيل وزخارف معبد البارثينون؟", listOf("Phidias", "فيدياس"), 77, "art", AnswerType.TEXT),
      Item("art_chopin_piano_birthplace_country", "In which European country was romantic piano composer Frédéric Chopin born?", "في أي دولة أوروبية ولد عازف البيانو والمؤلف الرومانسي فريدريك شوبان؟", listOf("Poland", "بولندا"), 63, "art", AnswerType.TEXT),
      Item("art_tchaikovsky_swan_lake_ballet", "Which Russian romantic composer composed the famous ballets Swan Lake and The Nutcracker?", "من هو المؤلف الموسيقي الروسي صاحب باليه بحيرة البجع وكسارة البندق الشهيرين؟", listOf("Pyotr Ilyich Tchaikovsky", "Tchaikovsky", "تشايكوفسكي"), 65, "art", AnswerType.TEXT),
      Item("art_dante_circles_of_hell_count", "In Dante's Inferno, into how many concentric circles is Hell divided?", "في جحيم دانتي في الكوميديا الإلهية، إلى كم دائرة متحدة المركز قُسم الجحيم؟", listOf("9", "Nine", "تسع دوائر", "٩"), 74, "art", AnswerType.NUMBER, 9.0),
      Item("art_faust_tragedy_author_goethe", "Which German literary giant wrote the two-part dramatic tragedy Faust?", "من هو الأديب والشاعر الألماني العظيم مؤلف المسرحية الدرامية الشهيرة فاوست؟", listOf("Johann Wolfgang von Goethe", "Goethe", "غوته", "يوهان غوته"), 71, "art", AnswerType.TEXT),
      Item("art_brothers_karamazov_author", "Which 19th-century Russian author wrote The Brothers Karamazov and Crime and Punishment?", "من هو الروائي الروسي العبقري مؤلف الإخوة كارامازوف والجريمة والعقاب؟", listOf("Fyodor Dostoevsky", "Dostoevsky", "دوستويفسكي", "فيودور دوستويفسكي"), 68, "art", AnswerType.TEXT),
      Item("art_marcel_proust_search_lost_time", "Which French novelist wrote the monumental seven-volume In Search of Lost Time?", "من هو الأديب الفرنسي صاحب الرواية الملحمية ذات الأجزاء السبعة بحثاً عن الزمن الضائع؟", listOf("Marcel Proust", "Proust", "مارسيل بروست", "بروست"), 75, "art", AnswerType.TEXT)
    )
    addItems(list, items, lang)
  }

  // 7. HARD GEOGRAPHY & NATURE (10 concepts, scores 61-78)
  private fun buildHardGeographyNature(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      Item("geo_lake_baikal_deepest_freshwater", "What is the deepest and oldest freshwater lake in the world, located in Siberia?", "ما هي أعمق وأقدم بحيرة مياه عذبة في العالم، والتي تقع في سيبيريا بروسيا؟", listOf("Lake Baikal", "Baikal", "بحيرة بايكال", "بايكال"), 66, "geography", AnswerType.TEXT),
      Item("geo_dead_sea_lowest_land_elevation", "What hypersaline lake shoreline marks the lowest dry land elevation on Earth?", "ما هي البحيرة شديدة الملوحة التي يُعتبر شاطئها أخفض نقطة يابسة على سطح الأرض؟", listOf("Dead Sea", "البحر الميت"), 64, "geography", AnswerType.TEXT),
      Item("geo_angel_falls_country", "In which South American nation is Angel Falls, the world's highest uninterrupted waterfall?", "في أي دولة بأمريكا الجنوبية يقع شلال آنجل، أعلى شلال متدفق دون انقطاع في العالم؟", listOf("Venezuela", "فنزويلا"), 69, "geography", AnswerType.TEXT),
      Item("geo_strait_of_gibraltar_connects", "Which narrow strait connects the Atlantic Ocean to the Mediterranean Sea?", "ما هو المضيق المائي الضيق الذي يربط بين المحيط الأطلسي والبحر الأبيض المتوسط؟", listOf("Strait of Gibraltar", "Gibraltar", "مضيق جبل طارق", "جبل طارق"), 63, "geography", AnswerType.TEXT),
      Item("geo_strait_of_hormuz_persian_gulf", "Which strategically vital strait links the Persian Gulf with the Gulf of Oman?", "ما هو المضيق الاستراتيجي الحيوي الذي يربط الخليج العربي بخليج عمان وبحر العرب؟", listOf("Strait of Hormuz", "Hormuz", "مضيق هرمز", "هرمز"), 68, "geography", AnswerType.TEXT),
      Item("geo_capital_of_kazakhstan_astana", "What is the official capital city of Kazakhstan?", "ما هي العاصمة الرسمية لجمهورية كازاخستان؟", listOf("Astana", "أستانا", "استانا"), 73, "geography", AnswerType.TEXT),
      Item("geo_capital_of_iceland_reykjavik", "What is the northernmost national capital city in the world, in Iceland?", "ما هي أقصى عاصمة دولة شمالاً في العالم، وهي عاصمة آيسلندا؟", listOf("Reykjavik", "ريكيافيك"), 70, "geography", AnswerType.TEXT),
      Item("geo_landlocked_south_american_two", "Besides Bolivia, which other South American country is entirely landlocked?", "بالإضافة إلى بوليفيا، ما هي الدولة الأخرى الوحيدة الحبيسة (غير الساحلية) في أمريكا الجنوبية؟", listOf("Paraguay", "باراغواي", "الباراغواي"), 72, "geography", AnswerType.TEXT),
      Item("geo_highest_navigable_lake_titicaca", "Which high-altitude lake between Peru and Bolivia is the world's highest navigable lake?", "ما هي البحيرة الجبلية الواقعة بين بيرو وبوليفيا وتُعد أعلى بحيرة صالحة للملاحة في العالم؟", listOf("Lake Titicaca", "Titicaca", "بحيرة تيتيكاكا", "تيتيكاكا"), 71, "geography", AnswerType.TEXT),
      Item("geo_bosphorus_strait_city", "Which transcontinental major historic city is straddled across the Bosphorus strait?", "أي مدينة تاريخية كبرى عابرة للقارات تقع على ضفتي مضيق البوسفور بين أوروبا وآسيا؟", listOf("Istanbul", "إسطنبول", "اسطنبول"), 62, "geography", AnswerType.TEXT)
    )
    addItems(list, items, lang)
  }

  // 8. HARD CULINARY, ANIMALS & SPORTS (15 concepts, scores 61-78)
  private fun buildHardCulinarySports(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      Item("food_sourdough_leavening_agent", "Traditional sourdough bread is leavened by wild yeast and what lactic acid bacteria?", "يخمر خبز العجين المخمر التقليدي بخمائر برية مع بكتيريا حمض ماذا؟", listOf("Lactobacillus", "Lactic acid bacteria", "بكتيريا حمض اللاكتيك", "لاكتوباسيلس"), 74, "food", AnswerType.TEXT),
      Item("food_roquefort_cheese_milk_source", "Authentic French Roquefort blue cheese must legally be made from the milk of what animal?", "يجب قانوناً صنع جبن الروكفور الفرنسي الأزرق الأصيل من حليب أي حيوان؟", listOf("Sheep", "Ewe", "الأغنام", "النعجة", "الغنم"), 72, "food", AnswerType.TEXT),
      Item("food_scoville_scale_measures", "What culinary scale measures the pungency and spicy heat of chili peppers?", "ما هو المقياس العلمي المعتمد لقياس درجة حرارة وحرورة الفلفل الحار؟", listOf("Scoville scale", "Scoville", "مقياس سكوفيل", "سكوفيل"), 69, "food", AnswerType.TEXT),
      Item("food_ceylon_tea_country_origin", "Ceylon tea was originally cultivated on the island nation known today as what?", "شاي سيلان الشهير زُرع تاريخياً في الجزيرة المعروفة اليوم رسمياً باسم ماذا؟", listOf("Sri Lanka", "سريلانكا"), 64, "food", AnswerType.TEXT),
      Item("food_bordeaux_wine_primary_red_grape", "What prominent black grape variety is famously blended with Merlot in Bordeaux wines?", "ما هو صنف العنب الأسود الشهير الذي يُمزج مع الميرلو في صناعة مشروبات بوردو؟", listOf("Cabernet Sauvignon", "كابيرنيه ساوفيجنون"), 76, "food", AnswerType.TEXT),
      // Animals
      Item("anim_echidna_monotreme_egg_laying", "Along with the platypus, what spiny mammal belongs to the egg-laying monotremes?", "إلى جانب خلد الماء، أي ثديي شوكي ينتمي لرتبة الكظاميات الثديية التي تضع البيض؟", listOf("Echidna", "نضناض", "قنفذ النمل"), 75, "animals", AnswerType.TEXT),
      Item("anim_komodo_dragon_native_country", "In which Southeast Asian island nation are wild Komodo dragons exclusively native?", "في أي دولة جزرية في جنوب شرق آسيا يعيش تنين كومودو في البرية حصرياً؟", listOf("Indonesia", "إندونيسيا"), 68, "animals", AnswerType.TEXT),
      Item("anim_blue_ringed_octopus_deadly_toxin", "What potent paralyzing neurotoxin is found in the venom of the tiny blue-ringed octopus?", "ما هو السم العصبي القاتل شديد الفعالية الموجود في لدغة الأخطبوط ذي الحلقات الزرقاء؟", listOf("Tetrodotoxin", "تيترودوتوكسين"), 78, "animals", AnswerType.TEXT),
      Item("anim_axolotl_native_lake_mexico", "The critically endangered neotenic salamander axolotl is native to the remnants of which Mexican lake?", "حيوان السلمندر المائي قنفذ البحر (أكسولوتل) موطنه الأصلي بحيرة مكسيكية تُدعى ماذا؟", listOf("Xochimilco", "Lake Xochimilco", "سوتشيميلكو"), 77, "animals", AnswerType.TEXT),
      Item("anim_okapi_closest_living_relative", "What tall African mammal is the only living phylogenetic relative of the rainforest okapi?", "ما هو الثديي الأفريقي الطويل الذي يُعد القريب الحي الوحيد لحيوان الأوكابي؟", listOf("Giraffe", "الزرافة"), 71, "animals", AnswerType.TEXT),
      // Sports
      Item("spt_first_fifa_world_cup_goals_scored", "Who was the top goalscorer in the inaugural 1930 FIFA World Cup tournament?", "من كان هداف أول بطولة لكأس العالم عام 1930 في أوروغواي برصيد 8 أهداف؟", listOf("Guillermo Stabile", "Stabile", "غييرمو ستابيلي"), 79, "sports", AnswerType.TEXT),
      Item("spt_cricket_wickets_in_over", "In standard cricket, how many legal balls are bowled in one complete over?", "في رياضة الكريكيت، كم عدد الكرات القانونية التي تُرمى في الأوفر (Over) الكامل؟", listOf("6", "Six", "ست كرات", "٦"), 63, "sports", AnswerType.NUMBER, 6.0),
      Item("spt_fencing_three_weapons", "What are the three official bladed weapons used in modern Olympic fencing?", "ما هي الأسلحة الثلاثة المعتمدة في رياضة المبارزة الأولمبية الحديثة (الشيش والسيف و)؟", listOf("Foil, Epee, Sabre", "Foil, Epee and Sabre", "الشيش وسيف المبارزة والسيف العربي"), 75, "sports", AnswerType.TEXT),
      Item("spt_snooker_maximum_break_score", "What is the highest possible traditional maximum break score in snooker without fouls?", "ما هو أعلى معدل نقاط ممكن (الماكسيموم بريك) في سنوكر بدون أخطاء؟", listOf("147", "One hundred forty-seven", "١٤٧"), 73, "sports", AnswerType.NUMBER, 147.0),
      Item("spt_olympic_five_rings_colors", "Which of the following is NOT one of the 5 colors on the Olympic rings (Black, Blue, Green, Red, Yellow)?", "أي لون من هذه الألوان ليس ضمن حلقات العلم الأولمبي الخمس (أسود، أزرق، أخضر، أحمر، أصفر، برتقالي)؟", listOf("Orange", "Purple", "البرتقالي"), 68, "sports", AnswerType.TEXT)
    )
    addItems(list, items, lang)
  }

  private fun addItems(list: MutableList<Question>, items: List<Item>, lang: AppLanguage) {
    for (item in items) {
      val qText = when (lang) {
        AppLanguage.ARABIC -> item.arabicQ
        else -> item.englishQ
      }
      val answers = when (lang) {
        AppLanguage.ARABIC -> item.answers
        else -> item.answers.filterNot { ans -> ans.any { it in '\u0600'..'\u06FF' } }
      }
      list.add(
        Question(
          id = "adv_h_${item.concept}_${lang.code}",
          language = lang.code,
          category = item.category,
          difficulty = 4, // Hard
          question = qText,
          answerType = item.type,
          acceptedAnswers = if (answers.isNotEmpty()) answers else item.answers,
          correctNumericValue = item.numericVal,
          tolerance = if (item.numericVal != null) 0.01 else null,
          explanation = "Verified scholarly reference fact.",
          difficultyScore = item.score,
          conceptId = item.concept
        )
      )
    }
  }

  private data class Item(
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
