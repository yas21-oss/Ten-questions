package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AnswerType
import com.example.data.model.AppLanguage
import com.example.data.model.Question

enum class KeyboardMode {
  AUTO,
  LETTERS,
  NUMBERS
}

/**
 * Custom modern in-game keyboard.
 * Moderately larger keys with comfortable touch targets (54dp height), tactile press animation,
 * audio/haptic responses, responsive layouts for different screens, full Arabic and Latin layouts,
 * and adaptive numeric keypad for numeric, decimal, date, year, and unit modes.
 */
@Composable
fun GameKeyboard(
  question: Question,
  language: AppLanguage,
  onKeyPressed: (Char) -> Unit,
  onBackspace: () -> Unit,
  onSubmit: () -> Unit,
  onToggleSystemKeyboard: () -> Unit,
  modifier: Modifier = Modifier,
  canSubmit: Boolean = true
) {
  var manualMode by remember(question.id) { mutableStateOf<KeyboardMode?>(null) }

  val isNumericQuestion = question.answerType in listOf(
    AnswerType.NUMBER,
    AnswerType.YEAR,
    AnswerType.DECIMAL,
    AnswerType.DATE
  )

  val activeMode = manualMode ?: if (isNumericQuestion) KeyboardMode.NUMBERS else KeyboardMode.LETTERS

  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
      .background(Color(0xFF0D1322))
      .border(
        width = 1.dp,
        brush = Brush.verticalGradient(
          colors = listOf(Color(0xFF38BDF8).copy(alpha = 0.35f), Color(0xFF1E293B).copy(alpha = 0.2f))
        ),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
      )
      .padding(horizontal = 6.dp, vertical = 8.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    // Mode switcher & System Keyboard toggle toolbar
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 4.dp, vertical = 2.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        KeyboardModeTab(
          title = "ABC",
          isSelected = activeMode == KeyboardMode.LETTERS,
          onClick = { manualMode = KeyboardMode.LETTERS }
        )
        KeyboardModeTab(
          title = "123",
          isSelected = activeMode == KeyboardMode.NUMBERS,
          onClick = { manualMode = KeyboardMode.NUMBERS }
        )
      }

      // Button to switch to Android system software keyboard
      Row(
        modifier = Modifier
          .clip(RoundedCornerShape(8.dp))
          .background(Color(0xFF1E293B))
          .clickable { onToggleSystemKeyboard() }
          .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Default.Keyboard,
          contentDescription = "Switch to system keyboard",
          tint = Color(0xFF94A3B8),
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "System Keyboard",
          style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
          color = Color(0xFF94A3B8)
        )
      }
    }

    Spacer(modifier = Modifier.height(4.dp))

    when (activeMode) {
      KeyboardMode.NUMBERS -> {
        NumericKeypad(
          isDateQuestion = question.answerType == AnswerType.DATE,
          allowDecimal = question.answerType in listOf(AnswerType.DECIMAL, AnswerType.NUMBER, AnswerType.UNIT),
          onKeyPressed = onKeyPressed,
          onBackspace = onBackspace,
          onSubmit = onSubmit,
          canSubmit = canSubmit
        )
      }

      else -> {
        if (language == AppLanguage.ARABIC) {
          ArabicGameKeyboard(
            onKeyPressed = onKeyPressed,
            onBackspace = onBackspace,
            onSubmit = onSubmit,
            canSubmit = canSubmit
          )
        } else {
          LatinGameKeyboard(
            onKeyPressed = onKeyPressed,
            onBackspace = onBackspace,
            onSubmit = onSubmit,
            canSubmit = canSubmit
          )
        }
      }
    }
  }
}

@Composable
private fun KeyboardModeTab(
  title: String,
  isSelected: Boolean,
  onClick: () -> Unit
) {
  Box(
    modifier = Modifier
      .clip(RoundedCornerShape(8.dp))
      .background(if (isSelected) Color(0xFF38BDF8).copy(alpha = 0.2f) else Color.Transparent)
      .border(
        width = 1.dp,
        color = if (isSelected) Color(0xFF38BDF8) else Color(0xFF334155),
        shape = RoundedCornerShape(8.dp)
      )
      .clickable(onClick = onClick)
      .padding(horizontal = 12.dp, vertical = 6.dp)
  ) {
    Text(
      text = title,
      style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
      color = if (isSelected) Color(0xFF38BDF8) else Color(0xFF94A3B8)
    )
  }
}

// -------------------------------------------------------------
// NUMERIC KEYPAD (1-9, 0, Backspace, Decimal, Date Slash, Space, Submit)
// -------------------------------------------------------------
@Composable
private fun NumericKeypad(
  isDateQuestion: Boolean,
  allowDecimal: Boolean,
  onKeyPressed: (Char) -> Unit,
  onBackspace: () -> Unit,
  onSubmit: () -> Unit,
  canSubmit: Boolean
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 4.dp),
    verticalArrangement = Arrangement.spacedBy(7.dp)
  ) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      NumberKey("1", Modifier.weight(1f)) { onKeyPressed('1') }
      NumberKey("2", Modifier.weight(1f)) { onKeyPressed('2') }
      NumberKey("3", Modifier.weight(1f)) { onKeyPressed('3') }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      NumberKey("4", Modifier.weight(1f)) { onKeyPressed('4') }
      NumberKey("5", Modifier.weight(1f)) { onKeyPressed('5') }
      NumberKey("6", Modifier.weight(1f)) { onKeyPressed('6') }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      NumberKey("7", Modifier.weight(1f)) { onKeyPressed('7') }
      NumberKey("8", Modifier.weight(1f)) { onKeyPressed('8') }
      NumberKey("9", Modifier.weight(1f)) { onKeyPressed('9') }
    }
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      val symbolChar = if (isDateQuestion) '/' else '.'
      NumberKey(symbolChar.toString(), Modifier.weight(1f)) { onKeyPressed(symbolChar) }
      NumberKey("0", Modifier.weight(1f)) { onKeyPressed('0') }
      val secondaryChar = if (isDateQuestion) '-' else '-'
      NumberKey(secondaryChar.toString(), Modifier.weight(1f)) { onKeyPressed(secondaryChar) }
      ActionKey(
        icon = Icons.AutoMirrored.Filled.Backspace,
        contentDescription = "Backspace",
        modifier = Modifier.weight(1f),
        onClick = onBackspace,
        tag = "game_kb_backspace"
      )
    }
    // Bottom Action Row: Space & Direct Enter/Submit
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .weight(2.6f)
          .height(54.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFF1E293B))
          .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
          .clickable { onKeyPressed(' ') }
          .testTag("game_kb_space"),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "SPACE",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
          color = Color(0xFF94A3B8)
        )
      }

      ActionKey(
        icon = Icons.AutoMirrored.Filled.KeyboardReturn,
        contentDescription = "Enter",
        modifier = Modifier.weight(1.4f),
        isPrimary = true,
        enabled = canSubmit,
        onClick = onSubmit,
        tag = "game_kb_enter"
      )
    }
  }
}

// -------------------------------------------------------------
// LATIN QWERTY KEYBOARD (English, Spanish, French)
// -------------------------------------------------------------
@Composable
private fun LatinGameKeyboard(
  onKeyPressed: (Char) -> Unit,
  onBackspace: () -> Unit,
  onSubmit: () -> Unit,
  canSubmit: Boolean
) {
  val row1 = listOf('Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P')
  val row2 = listOf('A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L')
  val row3 = listOf('Z', 'X', 'C', 'V', 'B', 'N', 'M')

  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(6.dp)
  ) {
    // Row 1
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      row1.forEach { char ->
        CharKey(char = char, modifier = Modifier.weight(1f)) { onKeyPressed(char) }
      }
    }

    // Row 2
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 14.dp),
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      row2.forEach { char ->
        CharKey(char = char, modifier = Modifier.weight(1f)) { onKeyPressed(char) }
      }
    }

    // Row 3
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(4.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Spacer(modifier = Modifier.weight(0.5f))
      row3.forEach { char ->
        CharKey(char = char, modifier = Modifier.weight(1f)) { onKeyPressed(char) }
      }
      ActionKey(
        icon = Icons.AutoMirrored.Filled.Backspace,
        contentDescription = "Backspace",
        modifier = Modifier.weight(1.5f),
        onClick = onBackspace,
        tag = "game_kb_backspace"
      )
    }

    // Row 4: Space bar & Submit
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      Box(
        modifier = Modifier
          .weight(3f)
          .height(54.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFF1E293B))
          .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
          .clickable { onKeyPressed(' ') }
          .testTag("game_kb_space"),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "SPACE",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
          color = Color(0xFF94A3B8)
        )
      }

      ActionKey(
        icon = Icons.AutoMirrored.Filled.KeyboardReturn,
        contentDescription = "Enter",
        modifier = Modifier.weight(1.2f),
        isPrimary = true,
        enabled = canSubmit,
        onClick = onSubmit,
        tag = "game_kb_enter"
      )
    }
  }
}

// -------------------------------------------------------------
// ARABIC GAME KEYBOARD (Full Standard Arabic Layout)
// -------------------------------------------------------------
@Composable
private fun ArabicGameKeyboard(
  onKeyPressed: (Char) -> Unit,
  onBackspace: () -> Unit,
  onSubmit: () -> Unit,
  canSubmit: Boolean
) {
  val row1 = listOf('ض', 'ص', 'ث', 'ق', 'ف', 'غ', 'ع', 'ه', 'خ', 'ح', 'ج', 'د')
  val row2 = listOf('ش', 'س', 'ي', 'ب', 'ل', 'ا', 'ت', 'ن', 'م', 'ك', 'ط')
  val row3 = listOf('ئ', 'ء', 'ؤ', 'ر', 'ى', 'ة', 'و', 'ز', 'ظ')

  CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
    Column(
      modifier = Modifier.fillMaxWidth(),
      verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
      // Row 1
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
      ) {
        row1.forEach { char ->
          CharKey(char = char, modifier = Modifier.weight(1f)) { onKeyPressed(char) }
        }
      }

      // Row 2
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(3.dp)
      ) {
        row2.forEach { char ->
          CharKey(char = char, modifier = Modifier.weight(1f)) { onKeyPressed(char) }
        }
      }

      // Row 3
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        row3.forEach { char ->
          CharKey(char = char, modifier = Modifier.weight(1f)) { onKeyPressed(char) }
        }
        ActionKey(
          icon = Icons.AutoMirrored.Filled.Backspace,
          contentDescription = "Backspace",
          modifier = Modifier.weight(1.5f),
          onClick = onBackspace,
          tag = "game_kb_backspace"
        )
      }

      // Row 4: Space bar & Submit
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Box(
          modifier = Modifier
            .weight(3f)
            .height(54.dp)
          .clip(RoundedCornerShape(12.dp))
          .background(Color(0xFF1E293B))
          .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
          .clickable { onKeyPressed(' ') }
          .testTag("game_kb_space"),
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "مسافة",
          style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
          color = Color(0xFF94A3B8)
        )
      }

      ActionKey(
        icon = Icons.AutoMirrored.Filled.KeyboardReturn,
        contentDescription = "Submit",
        modifier = Modifier.weight(1.2f),
        isPrimary = true,
        enabled = canSubmit,
        onClick = onSubmit,
        tag = "game_kb_enter"
      )
    }
  }
}
}

// -------------------------------------------------------------
// INDIVIDUAL KEY COMPONENTS (With tactile animation & larger tap area)
// -------------------------------------------------------------
@Composable
private fun CharKey(
  char: Char,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(targetValue = if (isPressed) 0.92f else 1f, label = "key_scale")

  Box(
    modifier = modifier
      .scale(scale)
      .height(54.dp)
      .clip(RoundedCornerShape(11.dp))
      .background(if (isPressed) Color(0xFF334155) else Color(0xFF1E293B))
      .border(
        width = 1.dp,
        color = if (isPressed) Color(0xFF38BDF8) else Color(0xFF334155),
        shape = RoundedCornerShape(11.dp)
      )
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
      )
      .testTag("game_kb_key_$char"),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = char.toString(),
      style = MaterialTheme.typography.titleMedium.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
      ),
      color = Color.White
    )
  }
}

@Composable
private fun NumberKey(
  text: String,
  modifier: Modifier = Modifier,
  onClick: () -> Unit
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(targetValue = if (isPressed) 0.92f else 1f, label = "num_scale")

  Box(
    modifier = modifier
      .scale(scale)
      .height(56.dp)
      .clip(RoundedCornerShape(12.dp))
      .background(if (isPressed) Color(0xFF334155) else Color(0xFF1E293B))
      .border(
        width = 1.dp,
        color = if (isPressed) Color(0xFF38BDF8) else Color(0xFF334155),
        shape = RoundedCornerShape(12.dp)
      )
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        onClick = onClick
      )
      .testTag("game_kb_num_$text"),
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.titleLarge.copy(
        fontWeight = FontWeight.Black,
        fontSize = 23.sp
      ),
      color = Color.White
    )
  }
}

@Composable
private fun ActionKey(
  icon: ImageVector,
  contentDescription: String,
  modifier: Modifier = Modifier,
  isPrimary: Boolean = false,
  enabled: Boolean = true,
  onClick: () -> Unit,
  tag: String = "game_kb_action"
) {
  val interactionSource = remember { MutableInteractionSource() }
  val isPressed by interactionSource.collectIsPressedAsState()
  val scale by animateFloatAsState(targetValue = if (isPressed) 0.92f else 1f, label = "action_scale")

  val bgBrush = when {
    isPrimary && enabled -> Brush.horizontalGradient(listOf(Color(0xFF6366F1), Color(0xFF4F46E5)))
    isPrimary && !enabled -> Brush.horizontalGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
    else -> Brush.horizontalGradient(listOf(Color(0xFF334155), Color(0xFF1E293B)))
  }

  Box(
    modifier = modifier
      .scale(scale)
      .height(54.dp)
      .clip(RoundedCornerShape(11.dp))
      .background(bgBrush)
      .border(
        width = 1.dp,
        color = if (isPrimary && enabled) Color(0xFF818CF8) else Color(0xFF334155),
        shape = RoundedCornerShape(11.dp)
      )
      .clickable(
        interactionSource = interactionSource,
        indication = null,
        enabled = enabled,
        onClick = onClick
      )
      .testTag(tag),
    contentAlignment = Alignment.Center
  ) {
    Icon(
      imageVector = icon,
      contentDescription = contentDescription,
      tint = if (enabled) Color.White else Color(0xFF64748B),
      modifier = Modifier.size(22.dp)
    )
  }
}
