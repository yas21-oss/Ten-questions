package com.example.data.questions

import com.example.data.model.AnswerType
import com.example.data.model.Question

object SpanishQuestions {

  val list: List<Question> by lazy {
    listOf(
      // VERY EASY
      Question("es_ve_1", "es", "everyday", 1, "¿De qué color es la hierba natural?", AnswerType.TEXT, listOf("Verde"), explanation = "La hierba es verde.", difficultyScore = 5, conceptId = "grass_natural_color"),
      Question("es_ve_2", "es", "everyday", 1, "¿Cuántos días tiene una semana?", AnswerType.NUMBER, listOf("7", "Siete"), correctNumericValue = 7.0, tolerance = 0.001, explanation = "Una semana tiene 7 días.", difficultyScore = 5, conceptId = "days_in_week"),
      Question("es_ve_3", "es", "animals", 1, "¿Qué animal doméstico maúlla (dice miau)?", AnswerType.TEXT, listOf("Gato", "El gato"), explanation = "Los gatos maúllan.", difficultyScore = 5, conceptId = "cat_meow_sound"),
      Question("es_ve_4", "es", "mathematics", 1, "¿Cuánto es 2 + 2?", AnswerType.NUMBER, listOf("4", "Cuatro"), correctNumericValue = 4.0, tolerance = 0.001, explanation = "2 + 2 = 4.", difficultyScore = 3, conceptId = "math_2_plus_2"),
      Question("es_ve_5", "es", "animals", 1, "¿Cuántas patas tiene un perro normal?", AnswerType.NUMBER, listOf("4", "Cuatro"), correctNumericValue = 4.0, tolerance = 0.001, explanation = "Un perro tiene cuatro patas.", difficultyScore = 5, conceptId = "dog_legs_count"),
      Question("es_ve_6", "es", "language", 1, "¿Cuál es el opuesto de caliente?", AnswerType.TEXT, listOf("Frío", "Frio"), explanation = "Frío es lo opuesto de caliente.", difficultyScore = 8, conceptId = "opposite_of_hot"),
      Question("es_ve_7", "es", "science", 1, "¿En qué planeta vivimos?", AnswerType.TEXT, listOf("Tierra", "La Tierra"), explanation = "Vivimos en el planeta Tierra.", difficultyScore = 6, conceptId = "planet_we_live_on"),
      Question("es_ve_8", "es", "mathematics", 1, "¿Qué número viene inmediatamente después del 9?", AnswerType.NUMBER, listOf("10", "Diez"), correctNumericValue = 10.0, tolerance = 0.001, explanation = "El 10 viene tras el 9.", difficultyScore = 4, conceptId = "number_after_9"),
      Question("es_ve_9", "es", "everyday", 1, "¿De qué color suele ser el cielo despejado durante el día?", AnswerType.TEXT, listOf("Azul"), explanation = "El cielo despejado se ve azul.", difficultyScore = 6, conceptId = "sky_color_daytime"),
      Question("es_ve_10", "es", "everyday", 1, "¿Cuántos meses tiene un año?", AnswerType.NUMBER, listOf("12", "Doce"), correctNumericValue = 12.0, tolerance = 0.001, explanation = "Un año tiene 12 meses.", difficultyScore = 8, conceptId = "months_in_year"),
      Question("es_ve_11", "es", "science", 1, "¿Cómo se llama el agua congelada en estado sólido?", AnswerType.TEXT, listOf("Hielo"), explanation = "El agua congelada es hielo.", difficultyScore = 8, conceptId = "water_frozen_ice"),
      Question("es_ve_12", "es", "animals", 1, "¿Qué animal ladra?", AnswerType.TEXT, listOf("Perro", "El perro"), explanation = "Los perros ladran.", difficultyScore = 5, conceptId = "dog_bark_sound"),
      Question("es_ve_13", "es", "food", 1, "¿Qué dulce alimento producen las abejas?", AnswerType.TEXT, listOf("Miel", "La miel"), explanation = "Las abejas producen miel.", difficultyScore = 10, conceptId = "honey_produced_by_bees"),
      Question("es_ve_14", "es", "language", 1, "¿Cuál es el opuesto de grande?", AnswerType.TEXT, listOf("Pequeño", "Pequeno", "Chico"), explanation = "Pequeño es el opuesto de grande.", difficultyScore = 8, conceptId = "opposite_of_big"),
      Question("es_ve_15", "es", "everyday", 1, "¿Cuántas ruedas tiene una bicicleta tradicional?", AnswerType.NUMBER, listOf("2", "Dos"), correctNumericValue = 2.0, tolerance = 0.001, explanation = "Una bicicleta tiene 2 ruedas.", difficultyScore = 6, conceptId = "bicycle_wheels_count"),
      Question("es_ve_16", "es", "mathematics", 1, "¿Qué figura geométrica tiene 3 lados?", AnswerType.TEXT, listOf("Triángulo", "Triangulo"), explanation = "El triángulo tiene 3 lados.", difficultyScore = 10, conceptId = "triangle_sides_count"),
      Question("es_ve_17", "es", "science", 1, "¿Qué estrella da luz y calor a la Tierra?", AnswerType.TEXT, listOf("Sol", "El Sol"), explanation = "El Sol es nuestra estrella central.", difficultyScore = 8, conceptId = "sun_lights_earth"),
      Question("es_ve_18", "es", "mathematics", 1, "¿Cuánto es 5 + 5?", AnswerType.NUMBER, listOf("10", "Diez"), correctNumericValue = 10.0, tolerance = 0.001, explanation = "5 + 5 = 10.", difficultyScore = 5, conceptId = "math_5_plus_5"),
      Question("es_ve_19", "es", "food", 1, "¿De qué color es un plátano maduro normalmente?", AnswerType.TEXT, listOf("Amarillo"), explanation = "Los plátanos maduros son amarillos.", difficultyScore = 7, conceptId = "banana_ripe_yellow"),
      Question("es_ve_20", "es", "language", 1, "¿Cuál es el opuesto del día?", AnswerType.TEXT, listOf("Noche", "La noche"), explanation = "La noche es el opuesto del día.", difficultyScore = 7, conceptId = "opposite_of_day"),
      Question("es_ve_21", "es", "everyday", 1, "¿Cuántos dedos tiene una mano humana típicamente?", AnswerType.NUMBER, listOf("5", "Cinco"), correctNumericValue = 5.0, tolerance = 0.001, explanation = "Una mano humana tiene 5 dedos.", difficultyScore = 5, conceptId = "fingers_on_hand"),
      Question("es_ve_22", "es", "food", 1, "¿Qué bebida blanca producen las vacas?", AnswerType.TEXT, listOf("Leche", "La leche"), explanation = "Las vacas producen leche.", difficultyScore = 6, conceptId = "cows_produce_milk"),
      Question("es_ve_23", "es", "animals", 1, "¿Qué ave pone los huevos que se comen comúnmente en el desayuno?", AnswerType.TEXT, listOf("Gallina", "La gallina"), explanation = "Las gallinas ponen huevos.", difficultyScore = 6, conceptId = "chicken_breakfast_eggs"),
      Question("es_ve_24", "es", "science", 1, "¿Qué astro brilla de noche y cambia de fases cada mes?", AnswerType.TEXT, listOf("Luna", "La Luna"), explanation = "La Luna es el satélite de la Tierra.", difficultyScore = 7, conceptId = "moon_night_phases"),
      Question("es_ve_25", "es", "mathematics", 1, "¿Cuánto es 10 menos 3?", AnswerType.NUMBER, listOf("7", "Siete"), correctNumericValue = 7.0, tolerance = 0.001, explanation = "10 menos 3 es 7.", difficultyScore = 6, conceptId = "math_10_minus_3"),

      // EASY
      Question("es_ea_1", "es", "geography", 2, "¿Cuántos continentes hay en la Tierra?", AnswerType.NUMBER, listOf("7", "Siete"), correctNumericValue = 7.0, tolerance = 0.001, explanation = "Hay 7 continentes.", difficultyScore = 25, conceptId = "continents_count_earth"),
      Question("es_ea_2", "es", "everyday", 2, "¿Qué color resulta al mezclar azul y amarillo?", AnswerType.TEXT, listOf("Verde"), explanation = "Azul con amarillo crea el verde.", difficultyScore = 22, conceptId = "mixing_blue_yellow_green"),
      Question("es_ea_3", "es", "science", 2, "¿Qué planeta es conocido como el Planeta Rojo?", AnswerType.TEXT, listOf("Marte"), explanation = "Marte se apoda el Planeta Rojo.", difficultyScore = 24, conceptId = "mars_red_planet"),
      Question("es_ea_4", "es", "geography", 2, "¿Cuál es la capital de Francia?", AnswerType.TEXT, listOf("París", "Paris"), explanation = "París es la capital de Francia.", difficultyScore = 22, conceptId = "capital_of_france"),
      Question("es_ea_5", "es", "animals", 2, "¿Cuál es el animal terrestre más veloz del mundo?", AnswerType.TEXT, listOf("Guepardo", "El guepardo"), explanation = "El guepardo corre a más de 100 km/h.", difficultyScore = 28, conceptId = "cheetah_fastest_land_animal"),
      Question("es_ea_6", "es", "science", 2, "¿A cuántos grados Celsius se congela el agua?", AnswerType.NUMBER, listOf("0", "Cero"), correctNumericValue = 0.0, tolerance = 0.001, explanation = "El agua se congela a 0 °C.", difficultyScore = 24, conceptId = "water_freezing_point_celsius"),
      Question("es_ea_7", "es", "science", 2, "¿A cuántos grados Celsius hierve el agua al nivel del mar?", AnswerType.NUMBER, listOf("100", "Cien"), correctNumericValue = 100.0, tolerance = 0.001, explanation = "El agua hierve a 100 °C.", difficultyScore = 26, conceptId = "water_boiling_point_celsius"),
      Question("es_ea_8", "es", "sports", 2, "¿Cuántos jugadores tiene un equipo de fútbol en el campo?", AnswerType.NUMBER, listOf("11", "Once"), correctNumericValue = 11.0, tolerance = 0.001, explanation = "Juegan 11 futbolistas por equipo.", difficultyScore = 28, conceptId = "football_players_on_pitch"),
      Question("es_ea_9", "es", "animals", 2, "¿Qué gran felino es llamado popularmente el rey de la selva?", AnswerType.TEXT, listOf("León", "Leon", "El león"), explanation = "El león es conocido como el rey de la selva.", difficultyScore = 23, conceptId = "lion_king_of_jungle"),
      Question("es_ea_10", "es", "geography", 2, "¿Cuál es la capital de España?", AnswerType.TEXT, listOf("Madrid"), explanation = "Madrid es la capital de España.", difficultyScore = 22, conceptId = "capital_of_spain"),
      Question("es_ea_11", "es", "geography", 2, "¿Cuál es la capital de Italia?", AnswerType.TEXT, listOf("Roma"), explanation = "Roma es la capital de Italia.", difficultyScore = 22, conceptId = "capital_of_italy"),
      Question("es_ea_12", "es", "mathematics", 2, "¿Cuánto es 10 por 10?", AnswerType.NUMBER, listOf("100", "Cien"), correctNumericValue = 100.0, tolerance = 0.001, explanation = "10 × 10 = 100.", difficultyScore = 25, conceptId = "math_10_times_10"),
      Question("es_ea_13", "es", "everyday", 2, "¿Cuántas horas tiene un día completo?", AnswerType.NUMBER, listOf("24", "Veinticuatro"), correctNumericValue = 24.0, tolerance = 0.001, explanation = "Un día completo tiene 24 horas.", difficultyScore = 21, conceptId = "hours_in_day"),
      Question("es_ea_14", "es", "animals", 2, "¿Cuál es el mamífero terrestre más grande del planeta?", AnswerType.TEXT, listOf("Elefante", "El elefante"), explanation = "El elefante africano es el mayor mamífero terrestre.", difficultyScore = 26, conceptId = "elephant_largest_land_mammal"),
      Question("es_ea_15", "es", "geography", 2, "¿Cuál es el océano más grande de la Tierra?", AnswerType.TEXT, listOf("Pacífico", "Pacifico", "Océano Pacífico"), explanation = "El océano Pacífico es el más grande.", difficultyScore = 30, conceptId = "pacific_ocean_largest"),

      // MEDIUM
      Question("es_me_1", "es", "geography", 3, "¿Cuál es tradicionalmente considerado el río más largo del mundo?", AnswerType.TEXT, listOf("Nilo", "El Nilo", "Río Nilo"), explanation = "El río Nilo es tradicionalmente el más largo.", difficultyScore = 45, conceptId = "nile_longest_river"),
      Question("es_me_2", "es", "art", 3, "¿Quién pintó la Mona Lisa?", AnswerType.TEXT, listOf("Leonardo da Vinci", "Da Vinci", "Leonardo"), explanation = "Leonardo da Vinci la pintó.", difficultyScore = 48, conceptId = "mona_lisa_painter"),
      Question("es_me_3", "es", "geography", 3, "¿Cuál es la capital de Estados Unidos?", AnswerType.MULTI_ACCEPTED_TEXT, listOf("Washington", "Washington D.C.", "Washington DC"), explanation = "Washington D.C. es la capital estadounidense.", difficultyScore = 45, conceptId = "capital_of_usa"),
      Question("es_me_4", "es", "art", 3, "¿Cuántas cuerdas tiene un violín estándar?", AnswerType.NUMBER, listOf("4", "Cuatro"), correctNumericValue = 4.0, tolerance = 0.001, explanation = "Un violín estándar tiene 4 cuerdas.", difficultyScore = 48, conceptId = "violin_strings_count"),
      Question("es_me_5", "es", "everyday", 3, "¿Cuántos días tiene un año estándar no bisiesto?", AnswerType.NUMBER, listOf("365"), correctNumericValue = 365.0, tolerance = 0.001, explanation = "Un año estándar tiene 365 días.", difficultyScore = 44, conceptId = "days_in_standard_year"),
      Question("es_me_6", "es", "science", 3, "¿Cuál es la fórmula química del agua?", AnswerType.TEXT, listOf("H2O", "H20"), explanation = "La fórmula del agua es H2O.", difficultyScore = 42, conceptId = "water_chemical_formula"),
      Question("es_me_7", "es", "geography", 3, "¿Cuál es la capital de Japón?", AnswerType.TEXT, listOf("Tokio", "Tokyo"), explanation = "Tokio es la capital de Japón.", difficultyScore = 43, conceptId = "capital_of_japan"),
      Question("es_me_8", "es", "space", 3, "¿Cuál es el planeta más grande de nuestro sistema solar?", AnswerType.TEXT, listOf("Júpiter", "Jupiter"), explanation = "Júpiter es el planeta más masivo del sistema solar.", difficultyScore = 46, conceptId = "jupiter_largest_planet"),
      Question("es_me_9", "es", "history", 3, "¿En qué año llegó el primer ser humano a la Luna?", AnswerType.YEAR, listOf("1969"), correctNumericValue = 1969.0, tolerance = 0.0001, explanation = "El ser humano llegó a la Luna en 1969.", difficultyScore = 58, conceptId = "moon_landing_year"),

      // HARD
      Question("es_ha_1", "es", "history", 4, "¿En qué año se hundió el Titanic?", AnswerType.YEAR, listOf("1912"), correctNumericValue = 1912.0, tolerance = 0.0001, explanation = "El Titanic se hundió en 1912.", difficultyScore = 65, conceptId = "titanic_sinking_year"),
      Question("es_ha_2", "es", "science", 4, "¿Cuál es el gas más abundante en la atmósfera terrestre?", AnswerType.TEXT, listOf("Nitrógeno", "Nitrogeno"), explanation = "El nitrógeno compone el 78% del aire.", difficultyScore = 68, conceptId = "nitrogen_most_abundant_gas"),
      Question("es_ha_3", "es", "geography", 4, "¿Cuál es la capital federal de Australia?", AnswerType.TEXT, listOf("Canberra"), explanation = "Canberra es la capital de Australia.", difficultyScore = 72, conceptId = "capital_of_australia")
    )
  }
}
