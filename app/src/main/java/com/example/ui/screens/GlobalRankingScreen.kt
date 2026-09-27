package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.UserAccount
import com.example.ui.components.TQRankingRow

@Composable
fun GlobalRankingScreen(
  rankedUsers: List<UserAccount>,
  currentUser: UserAccount?,
  language: AppLanguage,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  // If the list is empty, include the current user if present
  val effectiveUsers = if (rankedUsers.isEmpty() && currentUser != null) {
    listOf(currentUser)
  } else {
    rankedUsers
  }

  Surface(
    modifier = modifier.fillMaxSize(),
    color = Color(0xFF090D18)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
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
          modifier = Modifier.testTag("ranking_back_button")
        ) {
          Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = Color(0xFF94A3B8)
          )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "GLOBAL RANKING",
          style = MaterialTheme.typography.titleLarge.copy(
            fontWeight = FontWeight.Black,
            letterSpacing = 1.5.sp
          ),
          color = Color.White
        )
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Ranking info badge: Ranked by TOTAL STARS
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(Color(0xFF13192B))
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.EmojiEvents,
              contentDescription = null,
              tint = Color(0xFFFBBF24),
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Ranked by Total Stars",
              style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
              color = Color.White
            )
          }

          Text(
            text = "${effectiveUsers.size} Registered",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF94A3B8)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Column Header: Rank | Player | Stars
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 20.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "RANK",
          style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
          color = Color(0xFF64748B),
          fontWeight = FontWeight.Bold,
          modifier = Modifier.width(36.dp)
        )
        Text(
          text = "PLAYER",
          style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
          color = Color(0xFF64748B),
          fontWeight = FontWeight.Bold,
          modifier = Modifier.weight(1f)
        )
        Text(
          text = "STARS",
          style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 1.sp),
          color = Color(0xFF64748B),
          fontWeight = FontWeight.Bold
        )
      }

      Spacer(modifier = Modifier.height(6.dp))

      if (effectiveUsers.isEmpty()) {
        Box(
          modifier = Modifier.fillMaxSize(),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = "No ranked players yet.\nComplete stages to earn stars!",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF64748B),
            textAlign = TextAlign.Center
          )
        }
      } else {
        LazyColumn(
          modifier = Modifier.fillMaxWidth().weight(1f),
          contentPadding = PaddingValues(bottom = 24.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          itemsIndexed(effectiveUsers) { index, player ->
            val rank = index + 1
            val isCurrent = player.uid == currentUser?.uid
            TQRankingRow(
              rank = rank,
              playerName = player.displayName,
              stars = player.totalStars,
              isCurrentUser = isCurrent,
              modifier = Modifier.testTag("ranking_row_$rank")
            )
          }
        }
      }
    }
  }
}
