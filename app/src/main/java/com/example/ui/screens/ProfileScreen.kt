package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.repository.SyncStatus
import com.example.ui.components.TQProfileHeader

@Composable
fun ProfileScreen(
  user: UserAccount?,
  language: AppLanguage,
  syncStatus: SyncStatus? = null,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val currentStage = user?.unlockedStage ?: 1
  val totalStars = user?.totalStars ?: 0
  val totalXp = user?.xp ?: 0L
  val stagesWon = user?.stagesWon ?: 0

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
          modifier = Modifier.testTag("profile_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color(0xFF94A3B8)
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = Localization.get("profile", language),
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp
          ),
          color = Color.White
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Profile Header Box
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(24.dp))
          .background(Color(0xFF13192B))
          .border(1.dp, Color(0xFF232D48), RoundedCornerShape(24.dp))
          .padding(20.dp)
      ) {
        TQProfileHeader(user = user)
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Stats Section
      Text(
        text = "CAREER STATISTICS",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Black,
          letterSpacing = 2.sp
        ),
        color = Color(0xFF64748B)
      )

      Spacer(modifier = Modifier.height(14.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        ProfileStatBox(
          title = "TOTAL STARS",
          value = "$totalStars",
          accentColor = Color(0xFFFBBF24),
          modifier = Modifier.weight(1f)
        )
        ProfileStatBox(
          title = "STAGE LEVEL",
          value = "$currentStage",
          accentColor = Color(0xFF38BDF8),
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(12.dp))

      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        ProfileStatBox(
          title = "TOTAL XP",
          value = "$totalXp",
          accentColor = Color(0xFF6366F1),
          modifier = Modifier.weight(1f)
        )
        ProfileStatBox(
          title = "STAGES WON",
          value = "$stagesWon",
          accentColor = Color(0xFF10B981),
          modifier = Modifier.weight(1f)
        )
      }

      Spacer(modifier = Modifier.height(24.dp))

      // Storage & Sync Status Section (QA requirement 8)
      Text(
        text = "STORAGE & SYNCHRONIZATION",
        style = MaterialTheme.typography.labelSmall.copy(
          fontWeight = FontWeight.Black,
          letterSpacing = 2.sp
        ),
        color = Color(0xFF64748B)
      )

      Spacer(modifier = Modifier.height(14.dp))

      val currentStatus = syncStatus ?: SyncStatus(
        label = "Local Storage Active",
        isOnline = true,
        isCloudSynced = false,
        hasPendingSync = false
      )

      val statusColor = when {
        currentStatus.isCloudSynced -> Color(0xFF10B981) // Green
        currentStatus.hasPendingSync -> Color(0xFFF59E0B) // Amber/Orange
        !currentStatus.isOnline -> Color(0xFF94A3B8) // Gray
        else -> Color(0xFF38BDF8) // Blue
      }

      val statusIcon = when {
        currentStatus.isCloudSynced -> Icons.Default.CloudDone
        currentStatus.hasPendingSync -> Icons.Default.Sync
        else -> Icons.Default.CloudOff
      }

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(20.dp))
          .background(Color(0xFF13192B))
          .border(1.dp, Color(0xFF1E2846), RoundedCornerShape(20.dp))
          .padding(18.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(
            modifier = Modifier
              .size(44.dp)
              .clip(CircleShape)
              .background(statusColor.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = statusIcon,
              contentDescription = null,
              tint = statusColor,
              modifier = Modifier.size(22.dp)
            )
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = currentStatus.label,
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
              color = Color.White
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
              text = if (currentStatus.isCloudSynced) {
                "All progress is safely synchronized to your account."
              } else if (!currentStatus.isOnline) {
                "Offline mode. Progress is saved locally and will sync when reconnected."
              } else {
                "Progress is saved in secure local Room database storage."
              },
              style = MaterialTheme.typography.bodySmall,
              color = Color(0xFF94A3B8)
            )
          }
        }
      }
    }
  }
}

@Composable
private fun ProfileStatBox(
  title: String,
  value: String,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(20.dp))
      .background(Color(0xFF13192B))
      .border(1.dp, Color(0xFF1E2846), RoundedCornerShape(20.dp))
      .padding(18.dp)
  ) {
    Column {
      Text(
        text = title,
        style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
        color = Color(0xFF64748B),
        fontWeight = FontWeight.Bold
      )
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = value,
        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
        color = accentColor
      )
    }
  }
}
