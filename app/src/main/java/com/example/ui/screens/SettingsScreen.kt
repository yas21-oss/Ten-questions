package com.example.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.Localization
import com.example.data.model.AppLanguage
import com.example.data.model.UserAccount
import com.example.ui.components.DevConfigDialog

@Composable
fun SettingsScreen(
  user: UserAccount?,
  currentLanguage: AppLanguage,
  isDarkTheme: Boolean,
  isSoundEnabled: Boolean,
  isHapticEnabled: Boolean,
  onSelectLanguage: (AppLanguage) -> Unit,
  onToggleDarkTheme: (Boolean) -> Unit,
  onToggleSound: (Boolean) -> Unit,
  onToggleHaptic: (Boolean) -> Unit,
  onLogOut: () -> Unit,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  var showLanguageDialog by remember { mutableStateOf(false) }
  var showDevSetupInfo by remember { mutableStateOf(false) }

  Surface(
    modifier = modifier.fillMaxSize(),
    color = Color(0xFF090D18)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
      // Header
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = onBack,
          modifier = Modifier.testTag("settings_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color(0xFF94A3B8)
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = Localization.get("settings", language = currentLanguage),
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp
          ),
          color = Color.White
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Language setting
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(Color(0xFF13192B))
          .border(1.dp, Color(0xFF232D48), RoundedCornerShape(20.dp))
          .clickable { showLanguageDialog = true }
          .padding(20.dp)
          .testTag("change_language_setting_card")
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Language,
              contentDescription = null,
              tint = Color(0xFF38BDF8),
              modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
              Text(
                text = Localization.get("change_language", currentLanguage),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
              )
              Text(
                text = "${currentLanguage.flagEmoji} ${currentLanguage.nativeName} (${currentLanguage.englishName})",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF94A3B8)
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Preferences Box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(Color(0xFF13192B))
          .border(1.dp, Color(0xFF232D48), RoundedCornerShape(20.dp))
          .padding(horizontal = 20.dp, vertical = 12.dp)
      ) {
        Column {
          // Sound
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = null,
                tint = Color(0xFF6366F1),
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(14.dp))
              Text(
                text = Localization.get("sound_effects", currentLanguage),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
              )
            }
            Switch(
              checked = isSoundEnabled,
              onCheckedChange = onToggleSound,
              colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF6366F1)),
              modifier = Modifier.testTag("sound_switch")
            )
          }

          // Haptics
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Vibration,
                contentDescription = null,
                tint = Color(0xFF6366F1),
                modifier = Modifier.size(22.dp)
              )
              Spacer(modifier = Modifier.width(14.dp))
              Text(
                text = Localization.get("haptic_feedback", currentLanguage),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
              )
            }
            Switch(
              checked = isHapticEnabled,
              onCheckedChange = onToggleHaptic,
              colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF6366F1)),
              modifier = Modifier.testTag("haptic_switch")
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Cloud status
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(Color(0xFF13192B))
          .border(1.dp, Color(0xFF232D48), RoundedCornerShape(20.dp))
          .padding(20.dp)
      ) {
        Column {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.CloudDone,
              contentDescription = null,
              tint = Color(0xFF10B981),
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = Localization.get("online_status", currentLanguage),
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
              color = Color.White
            )
          }
          Spacer(modifier = Modifier.height(10.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Security,
              contentDescription = null,
              tint = Color(0xFF6366F1),
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
              text = Localization.get("anti_cheat_verified", currentLanguage),
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
              color = Color.White
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Developer setup guide
      OutlinedButton(
        onClick = { showDevSetupInfo = true },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
      ) {
        Icon(imageVector = Icons.Default.Info, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = Localization.get("dev_config_note", currentLanguage), color = Color(0xFF94A3B8))
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Log out button
      OutlinedButton(
        onClick = onLogOut,
        modifier = Modifier
          .fillMaxWidth()
          .testTag("log_out_button"),
        shape = RoundedCornerShape(16.dp)
      ) {
        Icon(imageVector = Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(18.dp), tint = Color(0xFFEF4444))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = Localization.get("log_out", currentLanguage), color = Color(0xFFEF4444))
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Studio Branding Footer
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Image(
          painter = painterResource(id = R.drawable.company_logo),
          contentDescription = "Studio Logo",
          modifier = Modifier
            .size(26.dp)
            .clip(RoundedCornerShape(6.dp))
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
          text = "Ten Questions",
          style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
          color = Color(0xFF64748B)
        )
      }
    }
  }

  if (showLanguageDialog) {
    androidx.compose.ui.window.Dialog(onDismissRequest = { showLanguageDialog = false }) {
      LanguageSelectionScreen(
        currentLanguage = currentLanguage,
        onLanguageSelected = { lang ->
          onSelectLanguage(lang)
          showLanguageDialog = false
        }
      )
    }
  }

  if (showDevSetupInfo) {
    DevConfigDialog(
      provider = "Online Account Services",
      missingKeys = listOf(
        "google-services.json for live Google Sign-In",
        "default_web_client_id in build config",
        "Facebook App ID & Client Token in strings.xml"
      ),
      onDismiss = { showDevSetupInfo = false }
    )
  }
}
