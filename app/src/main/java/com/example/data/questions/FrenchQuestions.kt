package com.example.data.questions

import com.example.data.model.AnswerType
import com.example.data.model.Question

object FrenchQuestions {

  val list: List<Question> by lazy {
    listOf(
      // VERY EASY
      Question("fr_ve_1", "fr", "everyday", 1, "De quelle couleur est l'herbe naturelle ?", AnswerType.TEXT, listOf("Vert", "Verte"), explanation = "L'herbe est verte.", difficultyScore = 5, conceptId = "grass_natural_color"),
      Question("fr_ve_2", "fr", "everyday", 1, "Combien de jours y a-t-il dans une semaine ?", AnswerType.NUMBER, listOf("7", "Sept"), correctNumericValue = 7.0, tolerance = 0.001, explanation = "Une semaine compte 7 jours.", difficultyScore = 5, conceptId = "days_in_week"),
      Question("fr_ve_3", "fr", "animals", 1, "Quel animal de compagnie miaule ?", AnswerType.TEXT, listOf("Chat", "Le chat"), explanation = "Les chats miaulent.", difficultyScore = 5, conceptId = "cat_meow_sound"),
      Question("fr_ve_4", "fr", "mathematics", 1, "Combien font 2 + 2 ?", AnswerType.NUMBER, listOf("4", "Quatre"), correctNumericValue = 4.0, tolerance = 0.001, explanation = "2 + 2 = 4.", difficultyScore = 3, conceptId = "math_2_plus_2"),
      Question("fr_ve_5", "fr", "animals", 1, "Combien de pattes a un chien ?", AnswerType.NUMBER, listOf("4", "Quatre"), correctNumericValue = 4.0, tolerance = 0.001, explanation = "Un chien a quatre pattes.", difficultyScore = 5, conceptId = "dog_legs_count"),
      Question("fr_ve_6", "fr", "language", 1, "Quel est le contraire de chaud ?", AnswerType.TEXT, listOf("Froid"), explanation = "Froid est le contraire de chaud.", difficultyScore = 8, conceptId = "opposite_of_hot"),
      Question("fr_ve_7", "fr", "science", 1, "Sur quelle planète vivons-nous ?", AnswerType.TEXT, listOf("Terre", "La Terre"), explanation = "Nous vivons sur la Terre.", difficultyScore = 6, conceptId = "planet_we_live_on"),
      Question("fr_ve_8", "fr", "mathematics", 1, "Quel nombre vient immédiatement après 9 ?", AnswerType.NUMBER, listOf("10", "Dix"), correctNumericValue = 10.0, tolerance = 0.001, explanation = "Le 10 suit le 9.", difficultyScore = 4, conceptId = "number_after_9"),
      Question("fr_ve_9", "fr", "everyday", 1, "De quelle couleur est le ciel le jour par beau temps ?", AnswerType.TEXT, listOf("Bleu"), explanation = "Le ciel dégagé est bleu.", difficultyScore = 6, conceptId = "sky_color_daytime"),
      Question("fr_ve_10", "fr", "everyday", 1, "Combien de mois compte une année ?", AnswerType.NUMBER, listOf("12", "Douze"), correctNumericValue = 12.0, tolerance = 0.001, explanation = "Il y a 12 mois dans l'année.", difficultyScore = 8, conceptId = "months_in_year"),
      Question("fr_ve_11", "fr", "science", 1, "Comment appelle-t-on l'eau gelée à l'état solide ?", AnswerType.TEXT, listOf("Glace"), explanation = "L'eau solide est la glace.", difficultyScore = 8, conceptId = "water_frozen_ice"),
      Question("fr_ve_12", "fr", "animals", 1, "Quel animal aboie ?", AnswerType.TEXT, listOf("Chien", "Le chien"), explanation = "Les chiens aboient.", difficultyScore = 5, conceptId = "dog_bark_sound"),
      Question("fr_ve_13", "fr", "food", 1, "Quel aliment sucré produisent les abeilles ?", AnswerType.TEXT, listOf("Miel", "Le miel"), explanation = "Les abeilles fabriquent du miel.", difficultyScore = 10, conceptId = "honey_produced_by_bees"),
      Question("fr_ve_14", "fr", "language", 1, "Quel est le contraire de grand ?", AnswerType.TEXT, listOf("Petit"), explanation = "Petit est le contraire de grand.", difficultyScore = 8, conceptId = "opposite_of_big"),
      Question("fr_ve_15", "fr", "everyday", 1, "Combien de roues possède un vélo ordinaire ?", AnswerType.NUMBER, listOf("2", "Deux"), correctNumericValue = 2.0, tolerance = 0.001, explanation = "Un vélo a 2 roues.", difficultyScore = 6, conceptId = "bicycle_wheels_count"),
      Question("fr_ve_16", "fr", "mathematics", 1, "Quelle forme géométrique a 3 côtés ?", AnswerType.TEXT, listOf("Triangle", "Un triangle"), explanation = "Le triangle a trois côtés.", difficultyScore = 10, conceptId = "triangle_sides_count"),
      Question("fr_ve_17", "fr", "science", 1, "Quelle étoile éclaire et réchauffe la Terre ?", AnswerType.TEXT, listOf("Soleil", "Le Soleil"), explanation = "Le Soleil est notre étoile.", difficultyScore = 8, conceptId = "sun_lights_earth"),
      Question("fr_ve_18", "fr", "mathematics", 1, "Combien font 5 + 5 ?", AnswerType.NUMBER, listOf("10", "Dix"), correctNumericValue = 10.0, tolerance = 0.001, explanation = "5 + 5 = 10.", difficultyScore = 5, conceptId = "math_5_plus_5"),
      Question("fr_ve_19", "fr", "food", 1, "De quelle couleur est une banane mûre en général ?", AnswerType.TEXT, listOf("Jaune"), explanation = "Une banane mûre est jaune.", difficultyScore = 7, conceptId = "banana_ripe_yellow"),
      Question("fr_ve_20", "fr", "language", 1, "Quel est le contraire du jour ?", AnswerType.TEXT, listOf("Nuit", "La nuit"), explanation = "La nuit est le contraire du jour.", difficultyScore = 7, conceptId = "opposite_of_day"),
      Question("fr_ve_21", "fr", "everyday", 1, "Combien de doigts a généralement une main humaine ?", AnswerType.NUMBER, listOf("5", "Cinq"), correctNumericValue = 5.0, tolerance = 0.001, explanation = "Une main humaine a 5 doigts.", difficultyScore = 5, conceptId = "fingers_on_hand"),
      Question("fr_ve_22", "fr", "food", 1, "Quelle boisson blanche les vaches produisent-elles ?", AnswerType.TEXT, listOf("Lait", "Le lait"), explanation = "Les vaches produisent du lait.", difficultyScore = 6, conceptId = "cows_produce_milk"),
      Question("fr_ve_23", "fr", "animals", 1, "Quel oiseau pond les œufs consommés au petit-déjeuner ?", AnswerType.TEXT, listOf("Poule", "La poule"), explanation = "Les poules pondent des œufs.", difficultyScore = 6, conceptId = "chicken_breakfast_eggs"),
      Question("fr_ve_24", "fr", "science", 1, "Quel astre brille la nuit et change de phase chaque mois ?", AnswerType.TEXT, listOf("Lune", "La Lune"), explanation = "La Lune orbite autour de la Terre.", difficultyScore = 7, conceptId = "moon_night_phases"),
      Question("fr_ve_25", "fr", "mathematics", 1, "Combien font 10 moins 3 ?", AnswerType.NUMBER, listOf("7", "Sept"), correctNumericValue = 7.0, tolerance = 0.001, explanation = "10 - 3 = 7.", difficultyScore = 6, conceptId = "math_10_minus_3"),

      // EASY
      Question("fr_ea_1", "fr", "geography", 2, "Combien de continents compte la Terre ?", AnswerType.NUMBER, listOf("7", "Sept"), correctNumericValue = 7.0, tolerance = 0.001, explanation = "La Terre compte 7 continents.", difficultyScore = 25, conceptId = "continents_count_earth"),
      Question("fr_ea_2", "fr", "everyday", 2, "Quelle couleur obtient-on en mélangeant le bleu et le jaune ?", AnswerType.TEXT, listOf("Vert"), explanation = "Bleu et jaune donnent le vert.", difficultyScore = 22, conceptId = "mixing_blue_yellow_green"),
      Question("fr_ea_3", "fr", "science", 2, "Quelle planète est surnommée la planète rouge ?", AnswerType.TEXT, listOf("Mars"), explanation = "Mars est la planète rouge.", difficultyScore = 24, conceptId = "mars_red_planet"),
      Question("fr_ea_4", "fr", "geography", 2, "Quelle est la capitale de la France ?", AnswerType.TEXT, listOf("Paris"), explanation = "Paris est la capitale de la France.", difficultyScore = 22, conceptId = "capital_of_france"),
      Question("fr_ea_5", "fr", "animals", 2, "Quel est l'animal terrestre le plus rapide à la course ?", AnswerType.TEXT, listOf("Guépard", "Le guépard"), explanation = "Le guépard est le plus rapide.", difficultyScore = 28, conceptId = "cheetah_fastest_land_animal"),
      Question("fr_ea_6", "fr", "science", 2, "À quelle température en degrés Celsius l'eau gèle-t-elle ?", AnswerType.NUMBER, listOf("0", "Zéro"), correctNumericValue = 0.0, tolerance = 0.001, explanation = "L'eau gèle à 0 °C.", difficultyScore = 24, conceptId = "water_freezing_point_celsius"),
      Question("fr_ea_7", "fr", "science", 2, "À quelle température en degrés Celsius l'eau bout-elle au niveau de la mer ?", AnswerType.NUMBER, listOf("100", "Cent"), correctNumericValue = 100.0, tolerance = 0.001, explanation = "L'eau bout à 100 °C.", difficultyScore = 26, conceptId = "water_boiling_point_celsius"),
      Question("fr_ea_8", "fr", "sports", 2, "Combien de joueurs par équipe jouent sur le terrain au football ?", AnswerType.NUMBER, listOf("11", "Onze"), correctNumericValue = 11.0, tolerance = 0.001, explanation = "Il y a 11 joueurs par équipe.", difficultyScore = 28, conceptId = "football_players_on_pitch"),
      Question("fr_ea_9", "fr", "animals", 2, "Quel félin est surnommé le roi de la jungle ?", AnswerType.TEXT, listOf("Lion", "Le lion"), explanation = "Le lion est surnommé le roi de la jungle.", difficultyScore = 23, conceptId = "lion_king_of_jungle"),
      Question("fr_ea_10", "fr", "geography", 2, "Quelle est la capitale de l'Italie ?", AnswerType.TEXT, listOf("Rome"), explanation = "Rome est la capitale de l'Italie.", difficultyScore = 22, conceptId = "capital_of_italy"),
      Question("fr_ea_11", "fr", "geography", 2, "Quelle est la capitale de l'Espagne ?", AnswerType.TEXT, listOf("Madrid"), explanation = "Madrid est la capitale de l'Espagne.", difficultyScore = 22, conceptId = "capital_of_spain"),
      Question("fr_ea_12", "fr", "mathematics", 2, "Combien font 10 fois 10 ?", AnswerType.NUMBER, listOf("100", "Cent"), correctNumericValue = 100.0, tolerance = 0.001, explanation = "10 × 10 = 100.", difficultyScore = 25, conceptId = "math_10_times_10"),
      Question("fr_ea_13", "fr", "everyday", 2, "Combien d'heures y a-t-il dans une journée complète ?", AnswerType.NUMBER, listOf("24", "Vingt-quatre"), correctNumericValue = 24.0, tolerance = 0.001, explanation = "Une journée compte 24 heures.", difficultyScore = 21, conceptId = "hours_in_day"),
      Question("fr_ea_14", "fr", "animals", 2, "Quel est le plus grand mammifère terrestre vivant ?", AnswerType.TEXT, listOf("Éléphant", "Elephant", "L'éléphant"), explanation = "L'éléphant est le plus grand mammifère terrestre.", difficultyScore = 26, conceptId = "elephant_largest_land_mammal"),
      Question("fr_ea_15", "fr", "geography", 2, "Quel est le plus grand océan de la planète Terre ?", AnswerType.TEXT, listOf("Pacifique", "Océan Pacifique"), explanation = "L'océan Pacifique est le plus vaste.", difficultyScore = 30, conceptId = "pacific_ocean_largest"),

      // MEDIUM
      Question("fr_me_1", "fr", "geography", 3, "Quel est le plus long fleuve du monde ?", AnswerType.TEXT, listOf("Le Nil", "Nil"), explanation = "Le Nil est traditionnellement le plus long fleuve.", difficultyScore = 45, conceptId = "nile_longest_river"),
      Question("fr_me_2", "fr", "art", 3, "Qui a peint La Joconde ?", AnswerType.TEXT, listOf("Léonard de Vinci", "De Vinci", "Leonardo da Vinci"), explanation = "Léonard de Vinci a peint La Joconde.", difficultyScore = 48, conceptId = "mona_lisa_painter"),
      Question("fr_me_3", "fr", "geography", 3, "Quelle est la capitale des États-Unis ?", AnswerType.MULTI_ACCEPTED_TEXT, listOf("Washington", "Washington D.C.", "Washington DC"), explanation = "Washington D.C. est la capitale des États-Unis.", difficultyScore = 45, conceptId = "capital_of_usa"),
      Question("fr_me_4", "fr", "art", 3, "Combien de cordes possède un violon classique ?", AnswerType.NUMBER, listOf("4", "Quatre"), correctNumericValue = 4.0, tolerance = 0.001, explanation = "Un violon a 4 cordes.", difficultyScore = 48, conceptId = "violin_strings_count"),
      Question("fr_me_5", "fr", "everyday", 3, "Combien de jours compte une année ordinaire non bissextile ?", AnswerType.NUMBER, listOf("365"), correctNumericValue = 365.0, tolerance = 0.001, explanation = "Une année compte 365 jours.", difficultyScore = 44, conceptId = "days_in_standard_year"),
      Question("fr_me_6", "fr", "science", 3, "Quelle est la formule chimique de l'eau ?", AnswerType.TEXT, listOf("H2O", "H20"), explanation = "L'eau a pour formule H2O.", difficultyScore = 42, conceptId = "water_chemical_formula"),
      Question("fr_me_7", "fr", "geography", 3, "Quelle est la capitale du Japon ?", AnswerType.TEXT, listOf("Tokyo"), explanation = "Tokyo est la capitale du Japon.", difficultyScore = 43, conceptId = "capital_of_japan"),
      Question("fr_me_8", "fr", "space", 3, "Quelle est la planète la plus volumineuse de notre système solaire ?", AnswerType.TEXT, listOf("Jupiter"), explanation = "Jupiter est la plus grande planète du système solaire.", difficultyScore = 46, conceptId = "jupiter_largest_planet"),
      Question("fr_me_9", "fr", "history", 3, "En quelle année l'homme a-t-il marché sur la Lune pour la première fois ?", AnswerType.YEAR, listOf("1969"), correctNumericValue = 1969.0, tolerance = 0.0001, explanation = "La mission Apollo 11 s'est posée en 1969.", difficultyScore = 58, conceptId = "moon_landing_year"),

      // HARD
      Question("fr_ha_1", "fr", "history", 4, "En quelle année le Titanic a-t-il coulé ?", AnswerType.YEAR, listOf("1912"), correctNumericValue = 1912.0, tolerance = 0.0001, explanation = "Le Titanic a coulé en 1912.", difficultyScore = 65, conceptId = "titanic_sinking_year"),
      Question("fr_ha_2", "fr", "science", 4, "Quel est le gaz le plus abondant dans l'atmosphère terrestre ?", AnswerType.TEXT, listOf("Azote", "Nitrogène"), explanation = "L'azote compose 78% de l'air.", difficultyScore = 68, conceptId = "nitrogen_most_abundant_gas"),
      Question("fr_ha_3", "fr", "geography", 4, "Quelle est la capitale fédérale de l'Australie ?", AnswerType.TEXT, listOf("Canberra"), explanation = "Canberra est la capitale de l'Australie.", difficultyScore = 72, conceptId = "capital_of_australia")
    )
  }
}
