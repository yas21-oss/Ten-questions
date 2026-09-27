package com.example.data.model

import androidx.compose.ui.unit.LayoutDirection

enum class AppLanguage(
  val code: String,
  val nativeName: String,
  val englishName: String,
  val flagEmoji: String,
  val isRtl: Boolean = false
) {
  ARABIC("ar", "العربية", "Arabic", "🇸🇦", isRtl = true),
  ENGLISH("en", "English", "English", "🇺🇸"),
  SPANISH("es", "Español", "Spanish", "🇪🇸"),
  FRENCH("fr", "Français", "French", "🇫🇷"),
  GERMAN("de", "Deutsch", "German", "🇩🇪"),
  ITALIAN("it", "Italiano", "Italian", "🇮🇹"),
  PORTUGUESE("pt", "Português", "Portuguese", "🇧🇷"),
  TURKISH("tr", "Türkçe", "Turkish", "🇹🇷"),
  RUSSIAN("ru", "Русский", "Russian", "🇷🇺"),
  CHINESE("zh", "中文 (简体)", "Chinese", "🇨🇳"),
  JAPANESE("ja", "日本語", "Japanese", "🇯🇵"),
  KOREAN("ko", "한국어", "Korean", "🇰🇷"),
  HINDI("hi", "हिन्दी", "Hindi", "🇮🇳"),
  INDONESIAN("id", "Bahasa Indonesia", "Indonesian", "🇮🇩"),
  DUTCH("nl", "Nederlands", "Dutch", "🇳🇱"),
  POLISH("pl", "Polski", "Polish", "🇵🇱"),
  UKRAINIAN("uk", "Українська", "Ukrainian", "🇺🇦");

  val layoutDirection: LayoutDirection
    get() = if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

  companion object {
    fun fromCode(code: String): AppLanguage {
      return entries.find { it.code.equals(code, ignoreCase = true) } ?: ENGLISH
    }
  }
}
