package com.example.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_settings")

class UserPreferencesRepository(private val context: Context) {

  companion object {
    private val KEY_SELECTED_LANGUAGE = stringPreferencesKey("selected_language")
    private val KEY_HAS_COMPLETED_ONBOARDING = booleanPreferencesKey("has_completed_onboarding")
    private val KEY_DARK_THEME = booleanPreferencesKey("dark_theme")
    private val KEY_SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
    private val KEY_HAPTIC_ENABLED = booleanPreferencesKey("haptic_enabled")
  }

  val selectedLanguageFlow: Flow<AppLanguage> = context.dataStore.data.map { prefs ->
    val code = prefs[KEY_SELECTED_LANGUAGE] ?: "en"
    AppLanguage.fromCode(code)
  }

  val hasCompletedOnboardingFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
    prefs[KEY_HAS_COMPLETED_ONBOARDING] ?: false
  }

  val isDarkThemeFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
    prefs[KEY_DARK_THEME] ?: true // Default to modern sleek dark theme for gaming
  }

  val isSoundEnabledFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
    prefs[KEY_SOUND_ENABLED] ?: true
  }

  val isHapticEnabledFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
    prefs[KEY_HAPTIC_ENABLED] ?: true
  }

  suspend fun setSelectedLanguage(language: AppLanguage) {
    context.dataStore.edit { prefs ->
      prefs[KEY_SELECTED_LANGUAGE] = language.code
      prefs[KEY_HAS_COMPLETED_ONBOARDING] = true
    }
  }

  suspend fun setDarkTheme(enabled: Boolean) {
    context.dataStore.edit { prefs ->
      prefs[KEY_DARK_THEME] = enabled
    }
  }

  suspend fun setSoundEnabled(enabled: Boolean) {
    context.dataStore.edit { prefs ->
      prefs[KEY_SOUND_ENABLED] = enabled
    }
  }

  suspend fun setHapticEnabled(enabled: Boolean) {
    context.dataStore.edit { prefs ->
      prefs[KEY_HAPTIC_ENABLED] = enabled
    }
  }
}
