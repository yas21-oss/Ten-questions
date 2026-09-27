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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.localization.Localization
import com.example.data.model.AppLanguage
import com.example.data.model.UserAccount
import com.example.data.repository.SyncStatus
import com.example.ui.components.TQPrimaryButton

@Composable
fun MainMenuScreen(
  user: UserAccount?,
  language: AppLanguage,
  syncStatus: SyncStatus? = null,
  onPlay: (Int) -> Unit,
  onOpenRanking: () -> Unit,
  onOpenProfile: () -> Unit,
  onOpenSettings: () -> Unit,
  modifier: Modifier = Modifier
) {
  val currentStage = user?.unlockedStage ?: 1
  val currentXp = user?.xp ?: 0L
  val totalStars = user?.totalStars ?: 0

  Surface(
    modifier = modifier.fillMaxSize(),
    color = Color(0xFF090D18)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 24.dp, vertical = 20.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceBetween
    ) {
      // Top Bar: Brand & Quick Action Icons
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        // App Title Small Brand
        Row(verticalAlignment = Alignment.CenterVertically) {
          Image(
            painter = painterResource(id = R.drawable.app_launcher_icon_1790422476071),
            contentDescription = null,
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(10.dp))
          )
          Spacer(modifier = Modifier.width(10.dp))
          Text(
            text = "TEN QUESTIONS",
            style = MaterialTheme.typography.titleSmall.copy(
              fontWeight = FontWeight.Black,
              letterSpacing = 2.sp
            ),
            color = Color.White
          )
        }

        // Secondary access: Profile, Global Ranking, Settings
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          IconButton(
            onClick = onOpenProfile,
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(Color(0xFF13192B))
              .testTag("nav_profile_button")
          ) {
            Icon(
              imageVector = Icons.Default.Person,
              contentDescription = "Profile",
              tint = Color(0xFF94A3B8),
              modifier = Modifier.size(20.dp)
            )
          }

          IconButton(
            onClick = onOpenRanking,
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(Color(0xFF13192B))
              .testTag("nav_ranking_button")
          ) {
            Icon(
              imageVector = Icons.Default.Leaderboard,
              contentDescription = "Global Ranking",
              tint = Color(0xFFFBBF24),
              modifier = Modifier.size(20.dp)
            )
          }

          IconButton(
            onClick = onOpenSettings,
            modifier = Modifier
              .size(42.dp)
              .clip(CircleShape)
              .background(Color(0xFF13192B))
              .testTag("nav_settings_button")
          ) {
            Icon(
              imageVector = Icons.Default.Settings,
              contentDescription = "Settings",
              tint = Color(0xFF94A3B8),
              modifier = Modifier.size(20.dp)
            )
          }
        }
      }

      // Center Core Section: Prominent Game Status & Title
      Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Spacer(modifier = Modifier.height(10.dp))

        // Large Badge Emblem
        Box(
          modifier = Modifier
            .size(130.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(
              Brush.radialGradient(
                colors = listOf(Color(0xFF6366F1).copy(alpha = 0.35f), Color(0xFF11172A))
              )
            )
            .border(2.dp, Color(0xFF4F46E5), RoundedCornerShape(32.dp)),
          contentAlignment = Alignment.Center
        ) {
          Image(
            painter = painterResource(id = R.drawable.app_launcher_icon_1790422476071),
            contentDescription = "Game Emblem",
            modifier = Modifier
              .size(95.dp)
              .clip(RoundedCornerShape(24.dp))
          )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
          text = "TEN QUESTIONS",
          style = MaterialTheme.typography.displaySmall.copy(
            fontWeight = FontWeight.Black,
            letterSpacing = 3.sp,
            fontSize = 32.sp
          ),
          color = Color.White,
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
          text = "10 Questions • Infinite Stages • Pure Trivia",
          style = MaterialTheme.typography.bodyMedium.copy(letterSpacing = 1.sp),
          color = Color(0xFF94A3B8),
          textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Prominent Stats Banner: Current Stage, Current XP, Total Stars
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF11172A))
            .border(1.dp, Color(0xFF1E2846), RoundedCornerShape(24.dp))
            .padding(vertical = 20.dp, horizontal = 16.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
          ) {
            // Current Stage
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "STAGE",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp),
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "$currentStage",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                color = Color(0xFF38BDF8)
              )
            }

            Box(modifier = Modifier.width(1.dp).height(36.dp).background(Color(0xFF1E293B)))

            // Current XP
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "TOTAL XP",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp),
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "$currentXp",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                color = Color.White
              )
            }

            Box(modifier = Modifier.width(1.dp).height(36.dp).background(Color(0xFF1E293B)))

            // Total Stars
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Text(
                text = "STARS",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.5.sp),
                color = Color(0xFF64748B),
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(4.dp))
              Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                  imageVector = Icons.Default.Star,
                  contentDescription = null,
                  tint = Color(0xFFFBBF24),
                  modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                  text = "$totalStars",
                  style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black),
                  color = Color(0xFFFBBF24)
                )
              }
            }
          }
        }
      }

      // Bottom Area: Large Primary PLAY Button
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        TQPrimaryButton(
          text = "PLAY",
          onClick = { onPlay(currentStage) },
          testTag = "main_play_button"
        )

        Spacer(modifier = Modifier.height(14.dp))

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          val dotColor = when {
            syncStatus?.isCloudSynced == true -> Color(0xFF10B981)
            syncStatus?.hasPendingSync == true -> Color(0xFFF59E0B)
            syncStatus?.isOnline == false -> Color(0xFF94A3B8)
            else -> Color(0xFF38BDF8)
          }
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(CircleShape)
              .background(dotColor)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = syncStatus?.label ?: "Local Storage Active",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF94A3B8)
          )
        }
      }
    }
  }
}
