package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.UserAccount
import com.example.data.repository.AuthRepository
import com.example.data.repository.AuthResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

  private val repository = AuthRepository(
    application,
    AppDatabase.getDatabase(application)
  )

  val currentUser: StateFlow<UserAccount?> = repository.currentUserFlow.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = null
  )

  private val _isLoading = MutableStateFlow(false)
  val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

  private val _devConfigDetails = MutableStateFlow<Pair<String, List<String>>?>(null)
  val devConfigDetails: StateFlow<Pair<String, List<String>>?> = _devConfigDetails.asStateFlow()

  fun signInWithGoogle() {
    viewModelScope.launch {
      _isLoading.value = true
      when (val result = repository.signInWithGoogle()) {
        is AuthResult.Success -> {
          // Logged in
        }
        is AuthResult.RequiresConfig -> {
          _devConfigDetails.value = Pair(result.provider, result.missingKeys)
        }
        is AuthResult.Error -> {
          // Log or display error
        }
      }
      _isLoading.value = false
    }
  }

  fun signInWithFacebook() {
    viewModelScope.launch {
      _isLoading.value = true
      when (val result = repository.signInWithFacebook()) {
        is AuthResult.Success -> {
          // Logged in
        }
        is AuthResult.RequiresConfig -> {
          _devConfigDetails.value = Pair(result.provider, result.missingKeys)
        }
        is AuthResult.Error -> {
          // Log or display error
        }
      }
      _isLoading.value = false
    }
  }

  fun playAsGuest() {
    viewModelScope.launch {
      _isLoading.value = true
      repository.playAsGuest()
      _isLoading.value = false
    }
  }

  fun dismissDevConfig() {
    _devConfigDetails.value = null
  }

  fun signOut() {
    viewModelScope.launch {
      repository.signOut()
    }
  }
}
