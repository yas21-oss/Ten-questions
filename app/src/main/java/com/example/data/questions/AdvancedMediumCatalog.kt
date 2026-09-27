package com.example.data.questions

import com.example.data.model.AnswerType
import com.example.data.model.AppLanguage
import com.example.data.model.Question

/**
 * AdvancedMediumCatalog adds 160 genuinely distinct knowledge concepts (scores 41-60).
 * Highly diversified across Space, Technology, History, Art, Music, Science, Sports, Food, Language.
 */
object AdvancedMediumCatalog {

  fun getCatalog(language: AppLanguage): List<Question> {
    val list = mutableListOf<Question>()
    buildSpaceQuestions(list, language)
    buildHistoryQuestions(list, language)
    buildTechQuestions(list, language)
    buildArtMusicQuestions(list, language)
    buildScienceNatureQuestions(list, language)
    buildFoodSportsLanguageQuestions(list, language)
    return list
  }

  // 1. SPACE & ASTRONOMY (25 concepts, scores 42-60)
  private fun buildSpaceQuestions(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      Quad("space_halley_comet_years", "About how many years does Halley's Comet take to orbit the Sun?", "كم سنة تقريباً يستغرق مذنب هالي لإتمام دورة واحدة حول الشمس؟", listOf("76", "75", "75-76", "٧٦"), 52, "space", AnswerType.NUMBER, 76.0),
      Quad("space_mars_moons_count", "How many moons does the planet Mars have?", "كم عدداً من الأقمار يمتلك كوكب المريخ؟", listOf("2", "Two", "اثنان", "٢"), 46, "space", AnswerType.NUMBER, 2.0),
      Quad("space_rings_planet_saturn", "Which solar system planet is most famous for its prominent prominent rings?", "أي كواكب المجموعة الشمسية يشتهر بحلقاته البارزة والجميلة حوله؟", listOf("Saturn", "زحل"), 42, "space", AnswerType.TEXT),
      Quad("space_hottest_planet_venus", "What is the hottest planet in our solar system due to greenhouse gases?", "ما هو أكثر كواكب المجموعة الشمسية حرارة بسبب غازات الاحتباس الحراري؟", listOf("Venus", "الزهرة", "الزهره"), 48, "space", AnswerType.TEXT),
      Quad("space_dwarf_planet_pluto", "What celestial body was reclassified as a dwarf planet in 2006?", "ما هو الجرم السماوي الذي أعيد تصنيفه ككوكب قزم عام 2006؟", listOf("Pluto", "بلوتو"), 44, "space", AnswerType.TEXT),
      Quad("space_jupiter_great_red_spot", "What giant swirling storm has raged on Jupiter for centuries?", "ما هو اسم العاصفة الحلزونية الضخمة الشهيرة على سطح المشتري؟", listOf("Great Red Spot", "البقعة الحمراء العظيمة", "البقعة الحمراء"), 50, "space", AnswerType.TEXT),
      Quad("space_first_satellite_sputnik", "What was the name of the first artificial satellite launched in 1957?", "ما هو اسم أول قمر صناعي أطلقه الإنسان إلى الفضاء عام 1957؟", listOf("Sputnik", "Sputnik 1", "سبوتنيك"), 56, "space", AnswerType.TEXT),
      Quad("space_milky_way_galaxy_type", "What shape type of galaxy is our Milky Way?", "ما هو الشكل الهندسي المصنف لمجرتنا درب التبانة؟", listOf("Spiral", "Barred spiral", "حلزونية", "حلزوني"), 54, "space", AnswerType.TEXT),
      Quad("space_closest_star_proxima", "Besides the Sun, what star is closest to Earth?", "باستثناء الشمس، ما هو أقرب نجم إلى كوكب الأرض؟", listOf("Proxima Centauri", "بروكسيما سنتوري", "رجل القنطور الأقرب"), 58, "space", AnswerType.TEXT),
      Quad("space_light_minutes_sun_earth", "Approximately how many minutes does sunlight take to reach Earth?", "كم دقيقة تقريباً يستغرق ضوء الشمس ليصل إلى الأرض؟", listOf("8", "8 minutes", "ثمانية", "٨"), 47, "space", AnswerType.NUMBER, 8.0),
      Quad("space_largest_moon_ganymede", "What is the largest moon in the entire solar system, orbiting Jupiter?", "ما هو أكبر قمر في المجموعة الشمسية بأكملها، والذي يدور حول المشتري؟", listOf("Ganymede", "غانيميد"), 58, "space", AnswerType.TEXT),
      Quad("space_titan_atmosphere_element", "What gas makes up over 90 percent of Saturn's moon Titan's atmosphere?", "ما هو الغاز الرئيسي الذي يشكل أكثر من 90 بالمئة من غلاف قمر تيتان؟", listOf("Nitrogen", "N2", "النيتروجين"), 60, "space", AnswerType.TEXT),
      Quad("space_asteroid_belt_between", "Between which two planets does the main asteroid belt lie?", "بين أي كوكبين يقع حزام الكويكبات الرئيسي في مجموعتنا الشمسية؟", listOf("Mars and Jupiter", "المريخ والمشتري"), 55, "space", AnswerType.TEXT),
      Quad("space_lunar_eclipse_alignment", "During a total lunar eclipse, what celestial body casts a shadow on the Moon?", "أثناء خسوف القمر، أي جرم سماوي يلقي بظله على سطح القمر؟", listOf("Earth", "The Earth", "الأرض", "الارض"), 45, "space", AnswerType.TEXT),
      Quad("space_solar_eclipse_alignment", "During a total solar eclipse, what passes directly between Earth and the Sun?", "أثناء كسوف الشمس، ما الذي يمر مباشرة بين الأرض والشمس ليحجب ضوءها؟", listOf("Moon", "The Moon", "القمر"), 44, "space", AnswerType.TEXT),
      Quad("space_andromeda_galaxy_nearest", "What is the nearest major spiral galaxy to our Milky Way?", "ما هي أقرب مجرة حلزونية كبرى إلى مجرتنا درب التبانة؟", listOf("Andromeda", "أندروميدا", "المرأة المسلسلة"), 52, "space", AnswerType.TEXT),
      Quad("space_first_spacewalk_human", "Which cosmonaut performed the world's first spacewalk in 1965?", "من هو رائد الفضاء الذي أجرى أول عملية سير في الفضاء الخارجي عام 1965؟", listOf("Alexei Leonov", "Leonov", "أليكسي ليونوف"), 60, "space", AnswerType.TEXT),
      Quad("space_mars_rover_curiosity", "Which NASA rover landed on Mars in 2012 to explore Gale Crater?", "أي مسبار تابع لناسا هبط على المريخ عام 2012 لاستكشاف فوهة غيل؟", listOf("Curiosity", "كيريوسيتي"), 56, "space", AnswerType.TEXT),
      Quad("space_iss_abbreviation_name", "What does the abbreviation ISS stand for in space exploration?", "ماذا يعني اختصار المحطة الفضائية الدولية بالإنجليزية ISS؟", listOf("International Space Station", "محطة الفضاء الدولية"), 48, "space", AnswerType.TEXT),
      Quad("space_sun_core_fusion_gas", "What light element fuses into helium inside the Sun's burning core?", "ما هو العنصر الخفيف الذي يندمج ليتحول إلى هيليوم داخل قلب الشمس؟", listOf("Hydrogen", "الهيدروجين"), 49, "space", AnswerType.TEXT),
      Quad("space_neptune_blue_color_gas", "What atmospheric gas gives Neptune its vivid blue coloration?", "ما هو الغاز الموجود في غلاف نبتون الذي يمنحه لونه الأزرق الزاهي؟", listOf("Methane", "CH4", "الميثان"), 57, "space", AnswerType.TEXT),
      Quad("space_moon_craters_cause", "What celestial impacts formed the vast majority of craters on the Moon?", "ما هي الأجرام التي اصطدمت بالقمر وسببت تشكل الغالبية العظمى من فوهاته؟", listOf("Meteorites", "Asteroids", "النيازك", "الشهب والنيازك"), 46, "space", AnswerType.TEXT),
      Quad("space_supernova_definition", "What astronomical term describes the explosive death of a massive star?", "ما هو المصطلح الفلكي الذي يصف الانفجار العظيم عند موت نجم هائل الكتلة؟", listOf("Supernova", "مستعر أعظم", "سوبرنوفا"), 53, "space", AnswerType.TEXT),
      Quad("space_light_year_measures", "What physical dimension does a light-year measure in astronomy?", "ما هي الكمية الفيزيائية التي تقيسها السنة الضوئية في علم الفلك؟", listOf("Distance", "المسافة", "مسافة"), 45, "space", AnswerType.TEXT),
      Quad("space_sun_surface_layer_photo", "What is the visible light-emitting surface layer of the Sun called?", "ماذا تُسمى الطبقة السطحية المضيئة المرئية من الشمس؟", listOf("Photosphere", "الفوتوسفير", "الغلاف الضوئي"), 59, "space", AnswerType.TEXT)
    )
    addItems(list, items, lang)
  }

  // 2. HISTORY (25 concepts, scores 42-60)
  private fun buildHistoryQuestions(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      Quad("hist_columbus_voyage_year", "In what year did Christopher Columbus reach the Americas?", "في أي عام ميلادي وصل كريستوفر كولومبوس إلى القارة الأمريكية؟", listOf("1492", "١٤٩٢"), 44, "history", AnswerType.YEAR, 1492.0),
      Quad("hist_fall_of_rome_year", "In what year did the Western Roman Empire fall (476 AD)?", "في أي عام ميلادي سقطت الإمبراطورية الرومانية الغربية (476 م)؟", listOf("476", "٤٧٦"), 58, "history", AnswerType.YEAR, 476.0),
      Quad("hist_french_revolution_year", "In what year did the French Revolution begin with the Bastille storming?", "في أي عام ميلادي اندلعت الثورة الفرنسية بسقوط سجن الباستيل؟", listOf("1789", "١٧٨٩"), 52, "history", AnswerType.YEAR, 1789.0),
      Quad("hist_us_independence_year", "In what year was the United States Declaration of Independence signed?", "في أي عام ميلادي تم التوقيع على وثيقة إعلان استقلال الولايات المتحدة؟", listOf("1776", "١٧٧٦"), 46, "history", AnswerType.YEAR, 1776.0),
      Quad("hist_ww1_start_year", "In what calendar year did World War I begin?", "في أي عام ميلادي اندلعت الحرب العالمية الأولى؟", listOf("1914", "١٩١٤"), 48, "history", AnswerType.YEAR, 1914.0),
      Quad("hist_berlin_wall_fall_year", "In what year did the Berlin Wall officially fall?", "في أي عام ميلادي سقط جدار برلين ليعلن بدء توحيد ألمانيا؟", listOf("1989", "١٩٨٩"), 50, "history", AnswerType.YEAR, 1989.0),
      Quad("hist_first_us_president", "Who served as the very first President of the United States?", "من هو أول رئيس للولايات المتحدة الأمريكية؟", listOf("George Washington", "Washington", "جورج واشنطن"), 43, "history", AnswerType.TEXT),
      Quad("hist_ancient_wonders_pyramid", "Which ancient wonder is the only one still largely intact today?", "أي من عجائب الدنيا السبع القديمة هي الوحيدة التي لا تزال قائمة حتى اليوم؟", listOf("Great Pyramid of Giza", "Pyramids of Giza", "الهرم الأكبر", "أهرامات الجيزة"), 44, "history", AnswerType.TEXT),
      Quad("hist_julius_caesar_assassinated_year", "In what year BC was Roman dictator Julius Caesar assassinated?", "في أي عام قبل الميلاد تم اغتيال الدكتاتور الروماني يوليوس قيصر؟", listOf("44", "44 BC", "٤٤"), 59, "history", AnswerType.NUMBER, 44.0),
      Quad("hist_alexander_great_tutor", "Which famous philosopher was the personal tutor of Alexander the Great?", "من هو الفيلسوف الإغريقي الشهير الذي كان معلماً خاصاً للإسكندر الأكبر؟", listOf("Aristotle", "أرسطو"), 54, "history", AnswerType.TEXT),
      Quad("hist_silk_road_trade_commodity", "What luxurious fabric gave its name to the ancient Eurasian trade routes?", "ما هو القماش الفاخر الذي سميت باسمه طريق التجارة الأوراسية القديمة؟", listOf("Silk", "الحرير"), 42, "history", AnswerType.TEXT),
      Quad("hist_black_death_century", "In which century AD did the Black Death plague devastate Eurasia?", "في أي قرن ميلادي انتشر طاعون الموت الأسود في أوروبا وآسيا؟", listOf("14", "14th", "الرابع عشر", "١٤"), 55, "history", AnswerType.NUMBER, 14.0),
      Quad("hist_suez_canal_connects_seas", "Which two seas are connected directly by the Suez Canal?", "أي بحرين يربط بينهما طريق قناة السويس الملاحي مباشرة؟", listOf("Mediterranean and Red Sea", "البحر المتوسط والبحر الأحمر"), 48, "history", AnswerType.TEXT),
      Quad("hist_panama_canal_opened_year", "In what year did the Panama Canal officially open for navigation?", "في أي عام ميلادي افتتحت قناة بنما المائية رسمياً للملاحة؟", listOf("1914", "١٩١٤"), 58, "history", AnswerType.YEAR, 1914.0),
      Quad("hist_gandhi_salt_march_nation", "In which country did Mahatma Gandhi lead the historic Salt March?", "في أي دولة قاد المهاتما غاندي مسيرة الملح التاريخية ضد الاستعمار؟", listOf("India", "الهند"), 47, "history", AnswerType.TEXT),
      Quad("hist_nelson_mandela_presidency_country", "Of which nation did Nelson Mandela become the first Black president in 1994?", "أي دولة أصبح نيلسون مانديلا أول رئيس أسود لها في عام 1994؟", listOf("South Africa", "جنوب أفريقيا"), 46, "history", AnswerType.TEXT),
      Quad("hist_roman_numeral_for_hundred", "What Latin letter represents 100 in Roman numerals?", "ما هو الحرف اللاتيني الذي يمثل الرقم 100 في الأرقام الرومانية؟", listOf("C"), 45, "history", AnswerType.TEXT),
      Quad("hist_roman_numeral_for_thousand", "What Latin letter represents 1000 in Roman numerals?", "ما هو الحرف اللاتيني الذي يمثل الرقم 1000 في الأرقام الرومانية؟", listOf("M"), 46, "history", AnswerType.TEXT),
      Quad("hist_sumerian_writing_system", "What wedge-shaped script was invented by ancient Sumerians in Mesopotamia?", "ما هو الخط المسماري الشهير الذي ابتكره السومريون في بلاد الرافدين؟", listOf("Cuneiform", "الكتابة المسمارية", "الخط المسماري"), 57, "history", AnswerType.TEXT),
      Quad("hist_cleopatra_last_pharaoh_dynasty", "To which Greek ruling dynasty did Queen Cleopatra VII belong?", "إلى أي سلالة حاكمة إغريقية كانت تنتمي الملكة كليوباترا السابعة؟", listOf("Ptolemaic", "Ptolemy", "البطالمة", "السلالة البطلمية"), 58, "history", AnswerType.TEXT),
      Quad("hist_renaissance_cradle_city", "Which Italian city is widely celebrated as the birthplace of the Renaissance?", "أي مدينة إيطالية تُعرف تاريخياً بأنها مهد عصر النهضة الأوروبية؟", listOf("Florence", "فلورنسا"), 52, "history", AnswerType.TEXT),
      Quad("hist_printing_press_inventor", "Who introduced movable mechanical type printing to Europe around 1440?", "من هو المخترع الألماني الذي ابتكر الطباعة بالحروف المتحركة نحو 1440؟", listOf("Johannes Gutenberg", "Gutenberg", "غوتنبرغ", "يوهان غوتنبرغ"), 49, "history", AnswerType.TEXT),
      Quad("hist_french_emperor_waterloo", "Which French emperor was decisively defeated at the Battle of Waterloo in 1815?", "أي إمبراطور فرنسي تعرض للهزيمة الحاسمة في معركة واترلو عام 1815؟", listOf("Napoleon", "Napoleon Bonaparte", "نابليون", "نابليون بونابرت"), 47, "history", AnswerType.TEXT),
      Quad("hist_un_founded_year", "In what year was the United Nations (UN) founded after World War II?", "في أي عام ميلادي تم تأسيس منظمة الأمم المتحدة عقب الحرب العالمية الثانية؟", listOf("1945", "١٩٤٥"), 51, "history", AnswerType.YEAR, 1945.0),
      Quad("hist_first_female_uk_prime_minister", "Who was the first female Prime Minister of the United Kingdom?", "من هي أول امرأة تشغل منصب رئيس وزراء المملكة المتحدة (المرأة الحديدية)؟", listOf("Margaret Thatcher", "Thatcher", "مارغريت تاتشر"), 53, "history", AnswerType.TEXT)
    )
    addItems(list, items, lang)
  }

  // 3. TECHNOLOGY & COMPUTING (25 concepts, scores 42-60)
  private fun buildTechQuestions(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      Quad("tech_bits_in_byte", "How many bits are in one standard computer byte?", "كم بتاً يوجد في البايت الواحد القياسي؟", listOf("8", "Eight", "ثمانية", "٨"), 42, "technology", AnswerType.NUMBER, 8.0),
      Quad("tech_kilobyte_bytes_standard", "How many bytes are traditionally in one kibibyte (binary 2^10)?", "كم بايت في الكيلوبايت الثنائي الدقيق (2 أس 10)؟", listOf("1024", "١٠٢٤"), 48, "technology", AnswerType.NUMBER, 1024.0),
      Quad("tech_first_programmer_ada", "Who is widely acknowledged as the world's very first computer programmer?", "من هي الشخصية التي تُعتبر تاريخياً أول مبرمجة حاسوب في العالم؟", listOf("Ada Lovelace", "Lovelace", "آدا لوفلايس"), 56, "technology", AnswerType.TEXT),
      Quad("tech_binary_number_system_base", "What is the numerical base of the binary system used in digital computers?", "ما هو الأساس العددي لنظام العد الثنائي المستخدم في المعالجات الرقمية؟", listOf("2", "Two", "اثنان", "٢"), 43, "technology", AnswerType.NUMBER, 2.0),
      Quad("tech_hexadecimal_base", "What is the numerical base of the hexadecimal system?", "ما هو الأساس العددي لنظام العد الست عشري المستخدم في البرمجة؟", listOf("16", "Sixteen", "ستة عشر", "١٦"), 49, "technology", AnswerType.NUMBER, 16.0),
      Quad("tech_creator_of_linux", "Who created the Linux operating system kernel in 1991?", "من هو المبرمج الفنلندي الذي ابتكر نواة نظام لينكس عام 1991؟", listOf("Linus Torvalds", "Torvalds", "لينوس تورفالدس"), 54, "technology", AnswerType.TEXT),
      Quad("tech_creator_of_python", "Who conceived and created the Python programming language?", "من هو مهندس البرمجيات الهولندي الذي ابتكر لغة بايثون البرمجية؟", listOf("Guido van Rossum", "Van Rossum", "خايدو فان روسوم", "غيدو فان روسم"), 58, "technology", AnswerType.TEXT),
      Quad("tech_http_stands_for", "In web addresses, what does HTTP stand for?", "في عناوين المواقع الإلكترونية، ماذا يعني اختصار بروتوكول HTTP؟", listOf("Hypertext Transfer Protocol", "بروتوكول نقل النص الفائق"), 53, "technology", AnswerType.TEXT),
      Quad("tech_cpu_stands_for", "What do the letters CPU stand for in computer hardware?", "ماذا يعني اختصار CPU لوحدة المعالجة الرئيسية في الحاسوب؟", listOf("Central Processing Unit", "وحدة المعالجة المركزية"), 45, "technology", AnswerType.TEXT),
      Quad("tech_ram_stands_for", "What does RAM stand for in computer storage hardware?", "ماذا يعني اختصار ذاكرة الوصول العشوائي RAM في الحاسوب؟", listOf("Random Access Memory", "ذاكرة الوصول العشوائي"), 46, "technology", AnswerType.TEXT),
      Quad("tech_wifi_frequency_standard_ghz", "What common wireless frequency band operates alongside 5 GHz?", "ما هو النطاق الترددي اللاسلكي الشائع لشبكات الواي فاي إلى جانب 5 غيغاهرتز؟", listOf("2.4", "2.4 GHz", "٢.٤"), 55, "technology", AnswerType.DECIMAL, 2.4),
      Quad("tech_gps_stands_for", "What does GPS stand for in satellite navigation technology?", "ماذا يعني اختصار نظام الملاحة العالمي GPS بالأقمار الصناعية؟", listOf("Global Positioning System", "نظام تحديد المواقع العالمي"), 47, "technology", AnswerType.TEXT),
      Quad("tech_founder_of_microsoft_bill", "Which American entrepreneur co-founded Microsoft alongside Paul Allen?", "من هو الملياردير الأمريكي الذي شارك في تأسيس شركة مايكروسوفت مع بول ألين؟", listOf("Bill Gates", "Gates", "بيل غيتس"), 43, "technology", AnswerType.TEXT),
      Quad("tech_co_founder_apple_steve_jobs", "Which iconic visionary co-founded Apple with Steve Wozniak in 1976?", "من هو الشريك المؤسس الشهير لشركة أبل مع ستيف وزنياك عام 1976؟", listOf("Steve Jobs", "Jobs", "ستيف جوبز"), 42, "technology", AnswerType.TEXT),
      Quad("tech_search_engine_google_year", "In what year was the search engine company Google incorporated (1998)?", "في أي عام ميلادي تم تأسيس شركة غوغل رسمياً (1998)؟", listOf("1998", "١٩٩٨"), 52, "technology", AnswerType.YEAR, 1998.0),
      Quad("tech_social_network_facebook_year", "In what year was Facebook first launched by Mark Zuckerberg (2004)?", "في أي عام ميلادي أطلق مارك زوكربيرغ شبكة فيسبوك لأول مرة (2004)؟", listOf("2004", "٢٠٠٤"), 48, "technology", AnswerType.YEAR, 2004.0),
      Quad("tech_c_programming_creator", "Who developed the foundational C programming language at Bell Labs?", "من هو عالم الحاسوب الذي ابتكر لغة السي C الأساسية في مختبرات بيل؟", listOf("Dennis Ritchie", "Ritchie", "دينيس ريتشي"), 59, "technology", AnswerType.TEXT),
      Quad("tech_first_apple_macintosh_year", "In what year did Apple release the revolutionary original Macintosh (1984)?", "في أي عام ميلادي أطلقت أبل حاسوب ماكنتوش الثوري الشهير بإعلانه (1984)؟", listOf("1984", "١٩٨٤"), 54, "technology", AnswerType.YEAR, 1984.0),
      Quad("tech_bluetooth_named_after_king", "Bluetooth wireless technology was named after which Scandinavian Viking king?", "سُميت تقنية البلوتوث اللاسلكية نسبة إلى أي ملك فايكنغ إسكندنافي شهير؟", listOf("Harald Bluetooth", "Harald", "هارالد بلوتوث"), 57, "technology", AnswerType.TEXT),
      Quad("tech_standard_usb_meaning", "What does the abbreviation USB stand for in hardware connections?", "ماذا يعني الاختصار الشهير USB في منافذ وتوصيلات الأجهزة الحاسوبية؟", listOf("Universal Serial Bus", "الناقل التسلسلي العام"), 47, "technology", AnswerType.TEXT),
      Quad("tech_html_markup_language_meaning", "What does HTML stand for in web page authoring?", "ماذا تعني لغة HTML المستخدمة في هيكلة صفحات الويب وتصميمها؟", listOf("Hypertext Markup Language", "لغة توصيف النص الفائق"), 48, "technology", AnswerType.TEXT),
      Quad("tech_sql_database_meaning", "What does SQL stand for in relational database management systems?", "ماذا يعني الاختصار SQL لقواعد البيانات العلائقية البرمجية؟", listOf("Structured Query Language", "لغة الاستعلام البنيوية"), 52, "technology", AnswerType.TEXT),
      Quad("tech_first_transistor_year", "In what decade was the revolutionary point-contact transistor invented at Bell Labs (1940s)?", "في أي عقد من القرن العشرين تم اختراع الترانزستور الثوري في مختبرات بيل؟", listOf("1947", "1940s", "١٩٤٧"), 60, "technology", AnswerType.YEAR, 1947.0),
      Quad("tech_compact_disc_inventors", "Which two electronics giants jointly developed the standard Audio CD in 1982?", "أي شركتين عملاقتين للإلكترونيات طورتا معاً القرص المدمج CD الصوتي عام 1982؟", listOf("Sony and Philips", "Philips and Sony", "سوني وفيليبس"), 59, "technology", AnswerType.TEXT),
      Quad("tech_qwerty_keyboard_layout", "What are the first six letters on the top letter row of standard keyboards?", "ما هي الأحرف الستة الأولى في الصف العلوي للوحة المفاتيح الإنجليزية القياسية؟", listOf("QWERTY", "كويرتي"), 44, "technology", AnswerType.TEXT)
    )
    addItems(list, items, lang)
  }

  // 4. ART, MUSIC & ARCHITECTURE (25 concepts, scores 42-60)
  private fun buildArtMusicQuestions(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      Quad("art_sistine_chapel_ceiling_painter", "Who painted the breathtaking frescoes on the ceiling of the Sistine Chapel?", "من هو فنان عصر النهضة العبقري الذي رسم سقف كنيسة سيستينا في الفاتيكان؟", listOf("Michelangelo", "مايكل أنجلو", "ميكيلانجيلو"), 47, "art", AnswerType.TEXT),
      Quad("art_sculpture_david_artist", "Which Renaissance master sculpted the marble statue of David in Florence?", "من هو النحات الذي نحت تمثال داود الرخامي الشهير في فلورنسا؟", listOf("Michelangelo", "مايكل أنجلو", "ميكيلانجيلو"), 46, "art", AnswerType.TEXT),
      Quad("art_the_scream_painter", "Which Norwegian expressionist painted the haunting masterpiece The Scream?", "من هو الرسام التعبيري النرويجي الذي رسم لوحة الصرخة الشهيرة؟", listOf("Edvard Munch", "Munch", "إدفارت مونك", "إدوارد مونش"), 52, "art", AnswerType.TEXT),
      Quad("art_guernica_painter_picasso", "Which Spanish cubist master painted the anti-war masterpiece Guernica?", "من هو الفنان الإسباني رائد التكعيبية الذي رسم لوحة غيرنيكا المناهضة للحرب؟", listOf("Pablo Picasso", "Picasso", "بابلو بيكاسو", "بيكاسو"), 50, "art", AnswerType.TEXT),
      Quad("art_the_thinker_sculptor", "Which French sculptor created the bronze sculpture The Thinker?", "من هو النحات الفرنسي الشهير الذي أبدع تمثال المفكر البرونزي؟", listOf("Auguste Rodin", "Rodin", "أوغست رودان", "رودان"), 53, "art", AnswerType.TEXT),
      Quad("art_girl_with_pearl_earring", "Which Dutch Golden Age master painted Girl with a Pearl Earring?", "من هو الرسام الهولندي الشهير الذي رسم لوحة الفتاة ذات القرط اللؤلؤي؟", listOf("Johannes Vermeer", "Vermeer", "يوهانس فيرمير", "فيرمير"), 55, "art", AnswerType.TEXT),
      Quad("art_sagrada_familia_architect", "Which visionary architect designed the Basilica de la Sagrada Familia in Barcelona?", "من هو المعماري الكتالوني العبقري الذي صمم كنيسة ساغرادا فاميليا في برشلونة؟", listOf("Antoni Gaudi", "Gaudi", "أنطوني غاودي", "غاودي"), 51, "art", AnswerType.TEXT),
      Quad("art_taj_mahal_emperor_built", "Which Mughal emperor commissioned the Taj Mahal for his wife Mumtaz?", "أي إمبراطور مغولي بنى ضريح تاج محل تخليداً لذكرى زوجته ممتاز محل؟", listOf("Shah Jahan", "شاه جهان"), 49, "art", AnswerType.TEXT),
      Quad("art_parthenon_dedicated_goddess", "To which Greek goddess was the Parthenon temple atop the Acropolis dedicated?", "لأي إلهة إغريقية تم تكريس معبد البارثينون الشهير على هضبة الأكروبوليس؟", listOf("Athena", "أثينا"), 54, "art", AnswerType.TEXT),
      Quad("art_colosseum_oval_amphitheatre", "In which ancient empire was the Colosseum amphitheatre constructed?", "في أي إمبراطورية قديمة تم تشييد مدرج الكولوسيوم البيضاوي الضخم؟", listOf("Roman Empire", "Rome", "الإمبراطورية الرومانية", "روما"), 43, "art", AnswerType.TEXT),
      Quad("art_beethoven_symphony_no_9_choral", "Which German composer wrote the iconic Ninth Symphony with Ode to Joy?", "من هو المؤلف الموسيقي الألماني الأصم الذي أبدع السيمفونية التاسعة ونشيد الفرح؟", listOf("Ludwig van Beethoven", "Beethoven", "بيتهوفن", "لودفيغ فان بيتهوفن"), 45, "art", AnswerType.TEXT),
      Quad("art_mozart_birthplace_city", "In which Austrian city was classical prodigy Wolfgang Amadeus Mozart born?", "في أي مدينة نمساوية ولد عبقري الموسيقى الكلاسيكية موزارت؟", listOf("Salzburg", "سالزبورغ"), 52, "art", AnswerType.TEXT),
      Quad("art_vivaldi_four_seasons_concertos", "Which Italian Baroque composer wrote the celebrated concerto suite The Four Seasons?", "من هو المؤلف الإيطالي الباروكي الذي ألف رائعة الفصول الأربعة الموسيقية؟", listOf("Antonio Vivaldi", "Vivaldi", "أنطونيو فيفالدي", "فيفالدي"), 48, "art", AnswerType.TEXT),
      Quad("art_four_main_orchestral_families", "How many instrument families make up a standard classical symphony orchestra?", "كم عدد عائلات الآلات الموسيقية التي يتكون منها الأوركسترا السيمفوني الكامل؟", listOf("4", "Four", "أربعة", "٤"), 47, "art", AnswerType.NUMBER, 4.0),
      Quad("art_clef_symbol_for_higher_pitches", "What musical clef is also universally known as the G clef?", "ما هو مفتاح التدوين الموسيقي المعروف عالمياً باسم مفتاح صول؟", listOf("Treble clef", "Treble", "مفتاح صول"), 46, "art", AnswerType.TEXT),
      Quad("art_bass_clef_known_as", "What musical clef is also universally known as the F clef?", "ما هو مفتاح التدوين الموسيقي المعروف أيضاً بمفتاح فا للنغمات المنخفضة؟", listOf("Bass clef", "Bass", "مفتاح فا"), 48, "art", AnswerType.TEXT),
      Quad("art_quixote_author_cervantes", "Who wrote the timeless Spanish literary masterpiece Don Quixote?", "من هو الأديب الإسباني الشهير الذي كتب الرواية الخالدة دون كيشوت؟", listOf("Miguel de Cervantes", "Cervantes", "ميغيل دي ثيربانتس", "ثيربانتس"), 50, "art", AnswerType.TEXT),
      Quad("art_divine_comedy_poet_dante", "Which Italian poet composed the monumental epic poem The Divine Comedy?", "من هو الشاعر الإيطالي العظيم الذي ألف الملحمة الشعرية الكوميديا الإلهية؟", listOf("Dante Alighieri", "Dante", "دانتي أليغييري", "دانتي"), 53, "art", AnswerType.TEXT),
      Quad("art_odyssey_iliad_greek_poet", "Which ancient Greek epic poet is credited with composing the Iliad and Odyssey?", "من هو الشاعر الإغريقي القديم صاحب ملحمتي الإلياذة والأوديسة؟", listOf("Homer", "هوميروس"), 47, "art", AnswerType.TEXT),
      Quad("art_war_and_peace_author", "Which Russian literary giant wrote the massive epic novel War and Peace?", "من هو الكاتب الروسي الفذ صاحب الرواية الملحمية الشهيرة الحرب والسلام؟", listOf("Leo Tolstoy", "Tolstoy", "ليو تولستوي", "تولستوي"), 51, "art", AnswerType.TEXT),
      Quad("art_les_miserables_author", "Who wrote the French historic novel Les Misérables in 1862?", "من هو الأديب الفرنسي العظيم مؤلف رواية البؤساء الشهيرة عام 1862؟", listOf("Victor Hugo", "Hugo", "فيكتور هوغو", "هوغو"), 49, "art", AnswerType.TEXT),
      Quad("art_sherlock_holmes_creator", "Which Scottish author created the brilliant consulting detective Sherlock Holmes?", "من هو الكاتب الاسكتلندي الذي ابتكر شخصية المحقق الشهير شرلوك هولمز؟", listOf("Arthur Conan Doyle", "Conan Doyle", "آرثر كونان دويل"), 45, "art", AnswerType.TEXT),
      Quad("art_origami_japanese_paper_art", "What is the traditional Japanese art of decorative paper folding called?", "ماذا يُسمى الفن الياباني التقليدي الشهير لطي الورق وتحويله لأشكال مجسمة؟", listOf("Origami", "أوريغامي"), 44, "art", AnswerType.TEXT),
      Quad("art_louvre_museum_city", "In which capital city is the world's most visited art museum, the Louvre?", "في أي عاصمة يقع متحف اللوفر الفني الأشهر والأكثر زيارة في العالم؟", listOf("Paris", "باريس"), 42, "art", AnswerType.TEXT),
      Quad("art_hermitage_museum_city", "In which Russian city is the vast State Hermitage Museum situated?", "في أي مدينة روسية تاريخية يقع متحف الإرميتاج الضخم الشهير؟", listOf("Saint Petersburg", "St Petersburg", "سانت بطرسبرغ", "سان بطرسبرج"), 56, "art", AnswerType.TEXT)
    )
    addItems(list, items, lang)
  }

  // 5. SCIENCE & NATURE (20 concepts, scores 42-60)
  private fun buildScienceNatureQuestions(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      Quad("sci_periodic_table_creator", "Which Russian chemist devised the first periodic table of chemical elements in 1869?", "من هو العالم الكيميائي الروسي الذي ابتكر أول جدول دوري للعناصر الكيميائية؟", listOf("Dmitri Mendeleev", "Mendeleev", "ديمتري مندلييف", "مندلييف"), 52, "science", AnswerType.TEXT),
      Quad("sci_element_symbol_k_potassium", "What essential metallic element is represented by the chemical symbol K?", "ما هو العنصر الكيميائي المهم الممثل بالحرف اللاتيني K في الجدول الدوري؟", listOf("Potassium", "البوتاسيوم"), 54, "science", AnswerType.TEXT),
      Quad("sci_element_symbol_na_sodium", "What element derived from Latin natrium has the chemical symbol Na?", "ما هو العنصر الكيميائي المشتق اسمه اللاتيني من النطرون ورمزه Na؟", listOf("Sodium", "الصوديوم"), 51, "science", AnswerType.TEXT),
      Quad("sci_element_symbol_fe_iron", "What common metallic element has the chemical symbol Fe (from Latin ferrum)?", "ما هو المعدن الشائع الذي يحمل الرمز الكيميائي Fe المشتق من اسمه اللاتيني؟", listOf("Iron", "الحديد"), 48, "science", AnswerType.TEXT),
      Quad("sci_dna_shape_double_helix", "What geometric shape describes the three-dimensional structure of double-stranded DNA?", "ما هو الوصف الهندسي المجسم للتركيب البنائي الدقيق لشريط الحمض النووي DNA؟", listOf("Double helix", "Helix", "حلزون مزدوج", "الحلزون المزدوج"), 49, "science", AnswerType.TEXT),
      Quad("sci_cell_powerhouse_organelle", "Which cellular organelle is nicknamed the powerhouse of the eukaryotic cell?", "ما هي العضية الخلوية الملقبة بمحطة توليد الطاقة في الخلية الحية؟", listOf("Mitochondria", "Mitochondrion", "الميتوكوندريا", "الميتوكندريا"), 47, "science", AnswerType.TEXT),
      Quad("sci_cell_control_center_nucleus", "What membrane-bound organelle contains the genetic chromosomes in animal cells?", "ما هي العضية المركزية التي تحتوي على الكروموسومات الجينية في الخلية الحيوانية؟", listOf("Nucleus", "النواة"), 44, "science", AnswerType.TEXT),
      Quad("sci_noble_gas_helium_atomic_number", "What is the atomic number of Helium, the lightest noble gas?", "ما هو العدد الذري لغاز الهيليوم، أخف الغازات النبيلة في الطبيعة؟", listOf("2", "Two", "اثنان", "٢"), 46, "science", AnswerType.NUMBER, 2.0),
      Quad("sci_speed_of_sound_air_approx", "Approximately how many meters per second does sound travel through 20C air?", "كم متراً في الثانية تقريباً تبلغ سرعة الصوت في الهواء عند حرارة 20 مئوية؟", listOf("343", "340", "٣٤٣"), 58, "science", AnswerType.NUMBER, 343.0),
      Quad("sci_absolute_zero_celsius", "What is absolute zero temperature in degrees Celsius (to the nearest whole number)?", "ما هي درجة الصفر المطلق بوحدة الدرجة المئوية (سلزيوس) تقريباً؟", listOf("-273", "-273 C", "-273.15", "٢٧٣-"), 57, "science", AnswerType.NUMBER, -273.0),
      Quad("sci_ph_pure_neutral_water", "What is the pH level of completely pure neutral water at room temperature?", "ما هو الرقم الهيدروجيني (pH) للماء النقي المتعادل عند درجة حرارة الغرفة؟", listOf("7", "Seven", "سبعة", "٧"), 44, "science", AnswerType.NUMBER, 7.0),
      Quad("sci_ocean_depth_mariana_trench", "What is the deepest oceanic trench on planet Earth?", "ما هو أعمق خندق مائي محيطي على كوكب الأرض في قاع المحيط الهادئ؟", listOf("Mariana Trench", "خندق ماريانا"), 50, "science", AnswerType.TEXT),
      Quad("sci_ozone_layer_atmospheric_layer", "In which layer of Earth's atmosphere is the protective ozone layer located?", "في أي طبقة من طبقات الغلاف الجوي للأرض تقع طبقة الأوزون الواقية؟", listOf("Stratosphere", "الستراتوسفير"), 56, "science", AnswerType.TEXT),
      Quad("sci_human_body_largest_internal_organ", "What is the largest internal solid organ in the human body?", "ما هو أكبر عضو داخلي صلب في جسم الإنسان ويقوم بتنقية السموم؟", listOf("Liver", "The liver", "الكبد"), 45, "science", AnswerType.TEXT),
      Quad("sci_human_teeth_adult_permanent", "How many permanent teeth does a standard adult human have including wisdom teeth?", "كم عدد الأسنان الدائمة لدى الإنسان البالغ بما في ذلك ضروس العقل؟", listOf("32", "Thirty-two", "اثنان وثلاثون", "٣٢"), 48, "science", AnswerType.NUMBER, 32.0),
      Quad("sci_marsupial_pouch_kangaroo", "What biological infraclass of mammals carries and nurses its young in a pouch?", "ما هي الفئة الحيوية من الثدييات التي تحمل صغارها داخل جيب أو جراب جلدي؟", listOf("Marsupials", "Marsupial", "الجرابيات", "الكيسيات"), 49, "science", AnswerType.TEXT),
      Quad("sci_largest_invertebrate_squid", "What massive deep-sea cephalopod is the largest living invertebrate by mass?", "ما هو الرخوي البحري العملاق الذي يُعد أضخم حيوان لا فقاري على وجه الأرض؟", listOf("Colossal Squid", "Giant Squid", "الحبار العملاق", "الحبار الضخم"), 58, "science", AnswerType.TEXT),
      Quad("sci_photosynthesis_byproduct_gas", "What life-giving gas is released as a byproduct during plant photosynthesis?", "ما هو الغاز الحيوي الضروري الذي تطلقه النباتات كناتج عن التمثيل الضوئي؟", listOf("Oxygen", "O2", "الأكسجين", "الاوكسجين"), 42, "science", AnswerType.TEXT),
      Quad("sci_smallest_unit_of_matter_atom", "What is the basic constituent unit of chemical matter and elements?", "ما هي الوحدة البنائية الأساسية للمادة الكيميائية والعناصر؟", listOf("Atom", "الذرة"), 43, "science", AnswerType.TEXT),
      Quad("sci_gravity_acceleration_earth", "What is the approximate gravitational acceleration on Earth's surface (m/s^2)?", "كم تبلغ عجلة الجاذبية الأرضية عند مستوى سطح البحر تقريباً (م/ث²)؟", listOf("9.8", "9.81", "٩.٨"), 52, "science", AnswerType.DECIMAL, 9.8)
    )
    addItems(list, items, lang)
  }

  // 6. FOOD, SPORTS & LANGUAGE (40 concepts, scores 41-60)
  private fun buildFoodSportsLanguageQuestions(list: MutableList<Question>, lang: AppLanguage) {
    val items = listOf(
      Quad("food_saffron_spice_flower", "From which delicate purple flower are expensive saffron threads harvested?", "من أي زهرة أرجوانية رقيقة يتم جني خيوط الزعفران الثمينة؟", listOf("Crocus", "Crocus sativus", "الزعفران", "زهرة الزعفران"), 58, "food", AnswerType.TEXT),
      Quad("food_tofu_ingredient_soy", "From which legume milk is traditional tofu curd produced?", "من حليب أي نوع من البقوليات يتم تصنيع وتخثير جبن التوفو النباتي؟", listOf("Soybean", "Soy", "فول الصويا", "الصويا"), 45, "food", AnswerType.TEXT),
      Quad("food_parmesan_cheese_country", "In which country did the famous hard cheese Parmigiano-Reggiano originate?", "في أي دولة نشأ جبن البارميزان (بارميجيانو ريجيانو) الإيطالي الشهير؟", listOf("Italy", "إيطاليا"), 43, "food", AnswerType.TEXT),
      Quad("food_guacamole_base_fruit", "What buttery green fruit is the primary ingredient in Mexican guacamole?", "ما هي الفاكهة الخضراء الغنية بالدهون التي تشكل المكون الأساسي للغواكامولي؟", listOf("Avocado", "الأفوكادو", "افوكادو"), 42, "food", AnswerType.TEXT),
      Quad("food_coffee_arabica_robusta", "What are the two most commercially cultivated species of coffee bean worldwide?", "ما هما أشهر سلالتين تجاريتين لبن القهوة يتم زراعتهما حول العالم؟", listOf("Arabica and Robusta", "أرابيكا وروبوستا"), 55, "food", AnswerType.TEXT),
      Quad("food_fermentation_baking_yeast", "What microscopic fungus is used in bread making to make dough rise?", "ما هو الفطر المجهري وحيد الخلية المستخدم في الخبز لتخمير العجين ونفخه؟", listOf("Yeast", "الخميرة"), 44, "food", AnswerType.TEXT),
      Quad("food_hummus_base_legume", "What legume bean is blended with tahini and lemon to make hummus?", "ما هو نوع البقوليات الذي يُهرس مع الطحينة والليمون لتحضير طبق الحمص؟", listOf("Chickpeas", "Chickpea", "الحمص"), 42, "food", AnswerType.TEXT),
      Quad("food_sushi_wrap_seaweed", "What is the Japanese culinary name for edible dried seaweed sheets used in sushi?", "ما هو الاسم الياباني لأوراق الأعشاب البحرية المجففة المستخدمة في لف السوشي؟", listOf("Nori", "نوري"), 54, "food", AnswerType.TEXT),
      Quad("food_kimchi_origin_country", "Which Asian country is internationally renowned for the fermented cabbage dish kimchi?", "أي دولة آسيوية تشتهر عالمياً بطبق الكيمتشي (الملفوف المخمر والمتبل)؟", listOf("South Korea", "Korea", "كوريا الجنوبية", "كوريا"), 46, "food", AnswerType.TEXT),
      Quad("food_couscous_origin_region", "From which North African cultural region does traditional steamed couscous originate?", "من أي منطقة جغرافية في شمال أفريقيا نشأ طبق الكسكسي التقليدي الشهير؟", listOf("Maghreb", "North Africa", "المغرب العربي", "شمال أفريقيا"), 48, "food", AnswerType.TEXT),
      Quad("food_balsamic_vinegar_city", "In which Italian province is authentic traditional balsamic vinegar aged?", "في أي مدينة ومقاطعة إيطالية يتم تعتيق الخل البلسمي التقليدي الشهير؟", listOf("Modena", "مودينا"), 59, "food", AnswerType.TEXT),
      Quad("food_dim_sum_cuisine_tradition", "From which world cuisine does the steaming tradition of Dim Sum originate?", "من أي مطبخ عالمي نشأت وجبات الديم سوم الصغيرة المطهوة بالبخار؟", listOf("Chinese", "China", "المطبخ الصيني", "الصين"), 46, "food", AnswerType.TEXT),
      Quad("food_wasabi_rhizome_family", "Authentic wasabi paste is grated from the rhizome of what plant family?", "يُبشر معجون الواسابي الياباني الأصلي من جذمور نبات ينتمي لعائلة الفجل وماذا؟", listOf("Horseradish", "Mustard", "الفجل الحار", "الخردل"), 57, "food", AnswerType.TEXT),
      Quad("food_truffle_fungus_harvest", "What subterranean fungus prized in gourmet dining is traditionally found using dogs or pigs?", "ما هو فطر الكمأة الجوفي الثمين الذي يُبحث عنه تحت الأرض بالاستعانة بالكلاب؟", listOf("Truffle", "Truffles", "الكمأة", "الترافل"), 53, "food", AnswerType.TEXT),
      Quad("food_curry_spice_turmeric_color", "What bright yellow spice gives standard curry powder its rich golden hue?", "ما هو التابل الأصفر الزاهي الذي يمنح مسحوق الكاري لونه الذهبي وفوائده الصحية؟", listOf("Turmeric", "الكركم"), 46, "food", AnswerType.TEXT),
      // Sports
      Quad("spt_marathon_distance_km", "What is the exact official distance of an Olympic marathon in kilometers?", "كم تبلغ المسافة الرسمية الدقيقة لسباق الماراثون الأولمبي بالكيلومترات؟", listOf("42.195", "42.2", "٤٢.١٩٥"), 54, "sports", AnswerType.DECIMAL, 42.195),
      Quad("spt_fifa_world_cup_years_cycle", "Every how many years is the men's FIFA World Cup tournament held?", "كل كم سنة تقام بطولة كأس العالم لكرة القدم للرجال؟", listOf("4", "Four", "أربع", "أربعة", "٤"), 42, "sports", AnswerType.NUMBER, 4.0),
      Quad("spt_first_modern_olympics_year", "In what year were the first modern Olympic Games held in Athens?", "في أي عام ميلادي أقيمت أول دورة ألعاب أولمبية حديثة في أثينا؟", listOf("1896", "١٨٩٦"), 53, "sports", AnswerType.YEAR, 1896.0),
      Quad("spt_first_world_cup_host_uruguay", "Which South American nation hosted and won the inaugural FIFA World Cup in 1930?", "أي دولة في أمريكا الجنوبية استضافت وفازت بأول بطولة لكأس العالم عام 1930؟", listOf("Uruguay", "أوروغواي", "الاوروغواي"), 55, "sports", AnswerType.TEXT),
      Quad("spt_decathlon_events_count", "How many track and field events constitute the men's athletics decathlon?", "كم عدد المنافسات الرياضية المتنوعة التي يتألف منها سباق العشاري لألعاب القوى؟", listOf("10", "Ten", "عشرة", "١٠"), 48, "sports", AnswerType.NUMBER, 10.0),
      Quad("spt_heptathlon_events_count", "How many events constitute the women's athletics heptathlon?", "كم عدد المنافسات الرياضية التي يتألف منها سباق السباعي لألعاب القوى للسيدات؟", listOf("7", "Seven", "سبعة", "٧"), 50, "sports", AnswerType.NUMBER, 7.0),
      Quad("spt_tennis_grand_slam_count", "How many Grand Slam tournaments are held in professional tennis each year?", "كم عدد بطولات الغراند سلام الكبرى التي تُقام في التنس كل عام؟", listOf("4", "Four", "أربعة", "٤"), 43, "sports", AnswerType.NUMBER, 4.0),
      Quad("spt_wimbledon_surface_grass", "On what natural surface is the prestigious Wimbledon tennis championship played?", "على أي نوع من الأرضيات الطبيعية تُقام بطولة ويمبلدون للتنس العريقة؟", listOf("Grass", "العشب", "الملاعب العشبية"), 46, "sports", AnswerType.TEXT),
      Quad("spt_french_open_surface_clay", "On what court surface is the French Open (Roland Garros) tennis tournament played?", "على أي نوع من الملاعب الترابية تُقام بطولة رولان غاروس (فرنسا المفتوحة) للتنس؟", listOf("Clay", "التراب", "الملاعب الترابية"), 47, "sports", AnswerType.TEXT),
      Quad("spt_chessboard_squares_count", "How many total squares are on a standard chess board (8x8)?", "كم عدد المربعات الإجمالي على رقعة الشطرنج القياسية (8 في 8)؟", listOf("64", "Sixty-four", "أربعة وستون", "٦٤"), 44, "sports", AnswerType.NUMBER, 64.0),
      Quad("spt_curling_stone_material", "From what heavy stone mineral are Olympic curling stones crafted?", "من أي نوع من الصخور الصلبة الثقيلة تُصنع أحجار رياضة الكيرلنغ الأولمبية؟", listOf("Granite", "الغرانيت", "الجرانيت"), 57, "sports", AnswerType.TEXT),
      Quad("spt_tour_de_france_leader_jersey", "What color is the leader's jersey (maillot jaune) in the Tour de France?", "ما هو لون القميص الشهير الذي يرتديه متصدر الترتيب العام في طواف فرنسا؟", listOf("Yellow", "أصفر", "الاصفر"), 46, "sports", AnswerType.TEXT),
      Quad("spt_golf_albatross_under_par", "In golf terminology, how many strokes under par is an albatross on a hole?", "في مصطلحات الغولف، كم ضربة أقل من المعدل (تحت البار) يُعد الألباتروس؟", listOf("3", "Three", "ثلاثة", "٣"), 58, "sports", AnswerType.NUMBER, 3.0),
      Quad("spt_badminton_projectile_shuttlecock", "What feathered or plastic projectile is hit back and forth in badminton?", "ما هو المقذوف الريشي أو البلاستيكي الخفيف الذي يُضرب بالمضرب في كرة الريشة؟", listOf("Shuttlecock", "Birdie", "الريشة", "كرة الريشة"), 48, "sports", AnswerType.TEXT),
      Quad("spt_boxing_round_duration_minutes", "How many minutes long is a standard professional men's boxing round?", "كم دقيقة تبلغ مدة الجولة الواحدة في ملاكمة المحترفين للرجال؟", listOf("3", "Three", "ثلاث دقائق", "٣"), 47, "sports", AnswerType.NUMBER, 3.0),
      // Language
      Quad("lang_esperanto_inventor_zamenhof", "Which ophthalmologist created the constructed international language Esperanto in 1887?", "من هو الطبيب البولندي الذي ابتكر لغة إسبرانتو المصطنعة للتواصل الدولي عام 1887؟", listOf("L. L. Zamenhof", "Zamenhof", "زامنهوف"), 59, "language", AnswerType.TEXT),
      Quad("lang_arabic_alphabet_letters_count", "How many letters are in the standard Arabic alphabet?", "كم عدد حروف الهجاء في الأبجدية العربية القياسية؟", listOf("28", "Twenty-eight", "ثمانية وعشرون", "٢٨"), 43, "language", AnswerType.NUMBER, 28.0),
      Quad("lang_greek_first_letter_alpha", "What is the first letter of the Greek alphabet?", "ما هو الحرف الأول في الأبجدية الإغريقية القديمة والحديثة؟", listOf("Alpha", "ألفا"), 42, "language", AnswerType.TEXT),
      Quad("lang_greek_last_letter_omega", "What is the 24th and final letter of the Greek alphabet?", "ما هو الحرف الأخير الرابع والعشرون في الأبجدية الإغريقية؟", listOf("Omega", "أوميغا", "اوميجا"), 43, "language", AnswerType.TEXT),
      Quad("lang_palindrome_definition_word", "What term describes a word, sentence, or number that reads the same forwards and backwards?", "ماذا يُسمى اللفظ أو الجملة أو العدد الذي يُقرأ بنفس الترتيب من اليمين أو اليسار؟", listOf("Palindrome", "متناظر", "قلب مستو"), 54, "language", AnswerType.TEXT),
      Quad("lang_cyrillic_script_creators", "Which Byzantine saintly brothers are credited with devising the predecessor of Cyrillic?", "أي قديسين أخوين بيزنطيين يُنسب إليهما ابتكار الأبجدية السلافية المبكرة؟", listOf("Cyril and Methodius", "كيرلس وميثوديوس"), 58, "language", AnswerType.TEXT),
      Quad("lang_official_un_languages_count", "How many official working languages does the United Nations have?", "كم عدد اللغات الرسمية المعتمدة في منظمة الأمم المتحدة؟", listOf("6", "Six", "ست لغات", "٦"), 47, "language", AnswerType.NUMBER, 6.0),
      Quad("lang_latin_language_family", "To which subfamily of the Indo-European languages did Latin belong?", "إلى أي فرع من فروع اللغات الهندية الأوروبية تنتمي اللغة اللاتينية وتفرعاتها؟", listOf("Italic", "Romance", "الإيطالقية", "الرومانسية"), 56, "language", AnswerType.TEXT),
      Quad("lang_hieroglyphs_deciphered_stone", "What ancient granodiorite stele found in 1799 was key to deciphering Egyptian hieroglyphs?", "ما هو الحجر الأثري الشهير الذي عُثر عليه عام 1799 وكان مفتاح فك الهيروغليفية؟", listOf("Rosetta Stone", "Rosetta", "حجر رشيد"), 51, "language", AnswerType.TEXT),
      Quad("lang_polyglot_definition_person", "What word describes a person who knows and speaks multiple different languages fluently?", "ما هي الكلمة التي تصف الشخص القادر على التحدث بعدة لغات بطلاقة؟", listOf("Polyglot", "متعدد اللغات"), 50, "language", AnswerType.TEXT)
    )
    addItems(list, items, lang)
  }

  private fun addItems(list: MutableList<Question>, items: List<Quad>, lang: AppLanguage) {
    for (item in items) {
      val qText = when (lang) {
        AppLanguage.ARABIC -> item.arabicQ
        AppLanguage.SPANISH -> item.englishQ // natural fallback with international answers
        AppLanguage.FRENCH -> item.englishQ
        else -> item.englishQ
      }
      val answers = when (lang) {
        AppLanguage.ARABIC -> item.answers
        else -> item.answers.filterNot { ans -> ans.any { it in '\u0600'..'\u06FF' } }
      }
      list.add(
        Question(
          id = "adv_m_${item.concept}_${lang.code}",
          language = lang.code,
          category = item.category,
          difficulty = 3,
          question = qText,
          answerType = item.type,
          acceptedAnswers = if (answers.isNotEmpty()) answers else item.answers,
          correctNumericValue = item.numericVal,
          tolerance = if (item.numericVal != null) 0.01 else null,
          explanation = "Verified reference fact.",
          difficultyScore = item.score,
          conceptId = item.concept
        )
      )
    }
  }

  private data class Quad(
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
