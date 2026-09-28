package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import com.example.ui.components.DevConfigDialog
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.GameOverScreen
import com.example.ui.screens.GameScreen
import com.example.ui.screens.GlobalRankingScreen
import com.example.ui.screens.LanguageSelectionScreen
import com.example.ui.screens.MainMenuScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.WinScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AuthViewModel
import com.example.ui.viewmodel.GameState
import com.example.ui.viewmodel.GameViewModel
import com.example.ui.viewmodel.SettingsViewModel

enum class AppScreen {
  SPLASH,
  AUTH,
  LANGUAGE_SETUP,
  MAIN_MENU,
  GAME,
  SETTINGS,
  PROFILE,
  RANKING
}

class MainActivity : ComponentActivity() {

  private val authViewModel: AuthViewModel by viewModels()
  private val gameViewModel: GameViewModel by viewModels()
  private val settingsViewModel: SettingsViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()
      val currentLanguage by settingsViewModel.currentLanguage.collectAsState()
      val hasCompletedOnboarding by settingsViewModel.hasCompletedOnboarding.collectAsState()
      val isSoundEnabled by settingsViewModel.isSoundEnabled.collectAsState()
      val isHapticEnabled by settingsViewModel.isHapticEnabled.collectAsState()

      val currentUser by authViewModel.currentUser.collectAsState()
      val isAuthLoading by authViewModel.isLoading.collectAsState()
      val devConfigDetails by authViewModel.devConfigDetails.collectAsState()

      val gameState by gameViewModel.gameState.collectAsState()
      val rankedUsers by gameViewModel.globalRanking.collectAsState(initial = emptyList())

      var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }

      MyApplicationTheme(darkTheme = isDarkTheme) {
        CompositionLocalProvider(LocalLayoutDirection provides currentLanguage.layoutDirection) {
          Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            val contentModifier = Modifier.padding(innerPadding)

            when (currentScreen) {
              AppScreen.SPLASH -> {
                SplashScreen(
                  onSplashFinished = {
                    if (currentUser == null) {
                      currentScreen = AppScreen.AUTH
                    } else if (!hasCompletedOnboarding) {
                      currentScreen = AppScreen.LANGUAGE_SETUP
                    } else {
                      currentScreen = AppScreen.MAIN_MENU
                    }
                  },
                  modifier = contentModifier
                )
              }

              AppScreen.AUTH -> {
                AuthScreen(
                  language = currentLanguage,
                  isLoading = isAuthLoading,
                  onGoogleSignIn = {
                    authViewModel.signInWithGoogle()
                    if (!hasCompletedOnboarding) {
                      currentScreen = AppScreen.LANGUAGE_SETUP
                    } else {
                      currentScreen = AppScreen.MAIN_MENU
                    }
                  },
                  onFacebookSignIn = {
                    authViewModel.signInWithFacebook()
                    if (!hasCompletedOnboarding) {
                      currentScreen = AppScreen.LANGUAGE_SETUP
                    } else {
                      currentScreen = AppScreen.MAIN_MENU
                    }
                  },
                  onGuestPlay = {
                    authViewModel.playAsGuest()
                    if (!hasCompletedOnboarding) {
                      currentScreen = AppScreen.LANGUAGE_SETUP
                    } else {
                      currentScreen = AppScreen.MAIN_MENU
                    }
                  },
                  modifier = contentModifier
                )
              }

              AppScreen.LANGUAGE_SETUP -> {
                BackHandler {
                  if (currentUser == null) {
                    currentScreen = AppScreen.AUTH
                  } else {
                    currentScreen = AppScreen.MAIN_MENU
                  }
                }
                LanguageSelectionScreen(
                  currentLanguage = currentLanguage,
                  onLanguageSelected = { selectedLang ->
                    settingsViewModel.selectLanguage(selectedLang)
                    currentScreen = AppScreen.MAIN_MENU
                  },
                  modifier = contentModifier
                )
              }

              AppScreen.MAIN_MENU -> {
                MainMenuScreen(
                  user = currentUser,
                  language = currentLanguage,
                  syncStatus = gameViewModel.syncStatus,
                  onPlay = { stageNum ->
                    val uid = currentUser?.uid ?: "guest_player"
                    gameViewModel.startStage(stageNum, currentLanguage, uid, isReplay = false)
                    currentScreen = AppScreen.GAME
                  },
                  onOpenRanking = { currentScreen = AppScreen.RANKING },
                  onOpenProfile = { currentScreen = AppScreen.PROFILE },
                  onOpenSettings = { currentScreen = AppScreen.SETTINGS },
                  modifier = contentModifier
                )
              }

              AppScreen.RANKING -> {
                BackHandler { currentScreen = AppScreen.MAIN_MENU }

                GlobalRankingScreen(
                  rankedUsers = rankedUsers,
                  currentUser = currentUser,
                  language = currentLanguage,
                  onBack = { currentScreen = AppScreen.MAIN_MENU },
                  modifier = contentModifier
                )
              }

              AppScreen.GAME -> {
                BackHandler {
                  gameViewModel.resetToIdle()
                  currentScreen = AppScreen.MAIN_MENU
                }

                when (val state = gameState) {
                  is GameState.Playing -> {
                    GameScreen(
                      state = state,
                      language = currentLanguage,
                      isSoundEnabled = isSoundEnabled,
                      isHapticEnabled = isHapticEnabled,
                      onInputChange = { gameViewModel.updateInput(it) },
                      onSubmit = { gameViewModel.submitAnswer(currentUser) },
                      onExit = {
                        gameViewModel.resetToIdle()
                        currentScreen = AppScreen.MAIN_MENU
                      },
                      modifier = contentModifier
                    )
                  }

                  is GameState.Won -> {
                    WinScreen(
                      stageNumber = state.stageNumber,
                      stageTotalXp = state.stageTotalXp,
                      starsEarned = state.starsEarned,
                      language = currentLanguage,
                      isSoundEnabled = isSoundEnabled,
                      isHapticEnabled = isHapticEnabled,
                      onNextStage = {
                        val uid = currentUser?.uid ?: "guest_player"
                        gameViewModel.startStage(state.stageNumber + 1, currentLanguage, uid, isReplay = false)
                      },
                      onReplay = {
                        val uid = currentUser?.uid ?: "guest_player"
                        gameViewModel.startStage(state.stageNumber, currentLanguage, uid, isReplay = true)
                      },
                      modifier = contentModifier
                    )
                  }

                  is GameState.Failed -> {
                    GameOverScreen(
                      stageNumber = state.stageNumber,
                      failedQuestion = state.failedQuestion,
                      correctAnswers = state.correctAnswers,
                      explanation = state.explanation,
                      language = currentLanguage,
                      isSoundEnabled = isSoundEnabled,
                      isHapticEnabled = isHapticEnabled,
                      onRetryStage = {
                        val uid = currentUser?.uid ?: "guest_player"
                        gameViewModel.startStage(state.stageNumber, currentLanguage, uid, isReplay = false)
                      },
                      modifier = contentModifier
                    )
                  }

                  else -> {
                    MainMenuScreen(
                      user = currentUser,
                      language = currentLanguage,
                      syncStatus = gameViewModel.syncStatus,
                      onPlay = { stageNum ->
                        val uid = currentUser?.uid ?: "guest_player"
                        gameViewModel.startStage(stageNum, currentLanguage, uid, isReplay = false)
                        currentScreen = AppScreen.GAME
                      },
                      onOpenRanking = { currentScreen = AppScreen.RANKING },
                      onOpenProfile = { currentScreen = AppScreen.PROFILE },
                      onOpenSettings = { currentScreen = AppScreen.SETTINGS },
                      modifier = contentModifier
                    )
                  }
                }
              }

              AppScreen.SETTINGS -> {
                BackHandler { currentScreen = AppScreen.MAIN_MENU }

                SettingsScreen(
                  user = currentUser,
                  currentLanguage = currentLanguage,
                  isDarkTheme = isDarkTheme,
                  isSoundEnabled = isSoundEnabled,
                  isHapticEnabled = isHapticEnabled,
                  onSelectLanguage = { settingsViewModel.selectLanguage(it) },
                  onToggleDarkTheme = { settingsViewModel.setDarkTheme(it) },
                  onToggleSound = { settingsViewModel.setSoundEnabled(it) },
                  onToggleHaptic = { settingsViewModel.setHapticEnabled(it) },
                  onLogOut = {
                    authViewModel.signOut()
                    currentScreen = AppScreen.AUTH
                  },
                  onBack = { currentScreen = AppScreen.MAIN_MENU },
                  modifier = contentModifier
                )
              }

              AppScreen.PROFILE -> {
                BackHandler { currentScreen = AppScreen.MAIN_MENU }

                ProfileScreen(
                  user = currentUser,
                  language = currentLanguage,
                  syncStatus = gameViewModel.syncStatus,
                  onBack = { currentScreen = AppScreen.MAIN_MENU },
                  modifier = contentModifier
                )
              }
            }

            // Developer OAuth Configuration Dialog when triggered
            devConfigDetails?.let { (provider, missingKeys) ->
              DevConfigDialog(
                provider = provider,
                missingKeys = missingKeys,
                onDismiss = { authViewModel.dismissDevConfig() }
              )
            }
          }
        }
      }
    }
  }
}
