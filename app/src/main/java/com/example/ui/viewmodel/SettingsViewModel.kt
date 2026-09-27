package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AppLanguage
import com.example.data.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = UserPreferencesRepository(application)

  val currentLanguage: StateFlow<AppLanguage> = repository.selectedLanguageFlow.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = AppLanguage.ENGLISH
  )

  val hasCompletedOnboarding: StateFlow<Boolean> = repository.hasCompletedOnboardingFlow.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = false
  )

  val isDarkTheme: StateFlow<Boolean> = repository.isDarkThemeFlow.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = true
  )

  val isSoundEnabled: StateFlow<Boolean> = repository.isSoundEnabledFlow.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = true
  )

  val isHapticEnabled: StateFlow<Boolean> = repository.isHapticEnabledFlow.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = true
  )

  fun selectLanguage(language: AppLanguage) {
    viewModelScope.launch {
      repository.setSelectedLanguage(language)
    }
  }

  fun setDarkTheme(enabled: Boolean) {
    viewModelScope.launch {
      repository.setDarkTheme(enabled)
    }
  }

  fun setSoundEnabled(enabled: Boolean) {
    viewModelScope.launch {
      repository.setSoundEnabled(enabled)
    }
  }

  fun setHapticEnabled(enabled: Boolean) {
    viewModelScope.launch {
      repository.setHapticEnabled(enabled)
    }
  }
}
