package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.SportsSoccer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.localization.Localization
import com.example.data.model.AppLanguage

@Composable
fun CategoryChip(
  category: String,
  language: AppLanguage,
  modifier: Modifier = Modifier
) {
  val icon = when (category.lowercase()) {
    "geography" -> Icons.Default.Public
    "science" -> Icons.Default.Science
    "history" -> Icons.Default.History
    "art" -> Icons.Default.Palette
    "music" -> Icons.Default.MusicNote
    "movies" -> Icons.Default.Movie
    "sports" -> Icons.Default.SportsSoccer
    "space" -> Icons.Default.RocketLaunch
    "animals" -> Icons.Default.Pets
    "food" -> Icons.Default.Restaurant
    "literature" -> Icons.Default.Book
    else -> Icons.Default.AutoAwesome
  }

  val localizedCategory = when (category.lowercase()) {
    "geography" -> Localization.get("cat_geography", language)
    "science" -> Localization.get("cat_science", language)
    "history" -> Localization.get("cat_history", language)
    "sports" -> Localization.get("cat_sports", language)
    "movies" -> Localization.get("cat_movies", language)
    "music" -> Localization.get("cat_music", language)
    "animals" -> Localization.get("cat_animals", language)
    "space" -> Localization.get("cat_space", language)
    "food" -> Localization.get("cat_food", language)
    else -> Localization.get("cat_general", language)
  }

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(16.dp))
      .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f))
      .padding(horizontal = 12.dp, vertical = 6.dp)
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = localizedCategory,
        style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}
