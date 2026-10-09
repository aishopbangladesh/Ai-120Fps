package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SyncDisabled
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.QuickGame
import com.example.ui.ApexViewModel
import com.example.ui.components.CircularGaugeDial
import com.example.ui.components.RamStatusBar
import com.example.ui.components.TurboActivateButton
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardSecondary
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import com.example.ui.theme.CyberOrange
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.CyberTextTertiary

@Composable
fun TurboDashboardScreen(
  viewModel: ApexViewModel,
  modifier: Modifier = Modifier
) {
  val turboState by viewModel.turboState.collectAsState()
  val quickGames by viewModel.quickGames.collectAsState()
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    // --- Top Chipset & Temperature Card ---
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp)),
      colors = CardDefaults.cardColors(containerColor = CyberCard)
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 14.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Box(
            modifier = Modifier
              .size(36.dp)
              .clip(RoundedCornerShape(8.dp))
              .background(CyberNeonGreen.copy(alpha = 0.15f))
              .border(1.dp, CyberNeonGreen.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = "Ai Shield",
              tint = CyberNeonGreen,
              modifier = Modifier.size(20.dp)
            )
          }

          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Ai 120Fps",
                color = CyberTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black
              )
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(4.dp))
                  .background(CyberNeonGreen)
                  .padding(horizontal = 5.dp, vertical = 1.dp)
              ) {
                Text(
                  text = "TURBO",
                  color = CyberBlack,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Black
                )
              }
            }
            Text(
              text = turboState.chipsetName,
              color = CyberTextSecondary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Medium
            )
          }
        }

        // Temperature status
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(CyberCardSecondary)
            .border(1.dp, CyberCardBorder, RoundedCornerShape(20.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp)
        ) {
          Icon(
            imageVector = Icons.Default.Thermostat,
            contentDescription = "Temperature",
            tint = if (turboState.temperatureC < 40.0) CyberNeonGreen else CyberOrange,
            modifier = Modifier.size(16.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = "${turboState.temperatureC}°C - ${turboState.temperatureStatus}",
            color = if (turboState.temperatureC < 40.0) CyberNeonGreen else CyberOrange,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // --- Dual Circular Gauges ---
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceEvenly,
      verticalAlignment = Alignment.CenterVertically
    ) {
      CircularGaugeDial(
        valueText = "${turboState.refreshRate}Hz",
        labelText = if (turboState.forcePeakRefresh) "Forced" else "Adaptive",
        progress = 1.0f,
        glowColor = CyberNeonGreen
      )

      CircularGaugeDial(
        valueText = "${turboState.fpsLocked} FPS",
        labelText = "Locked",
        progress = if (turboState.isTurboActive) 1.0f else 0.85f,
        glowColor = CyberNeonCyan
      )
    }

    Spacer(modifier = Modifier.height(14.dp))

    // RAM Free Status bar
    RamStatusBar(
      freeRamGb = turboState.freeRamGb,
      totalRamGb = turboState.totalRamGb
    )

    Spacer(modifier = Modifier.height(16.dp))

    // --- Big Glow Button: ACTIVATE 120 FPS TURBO ---
    TurboActivateButton(
      isActive = turboState.isTurboActive,
      onClick = { viewModel.toggleTurbo() }
    )

    Spacer(modifier = Modifier.height(20.dp))

    // --- Hardware Optimization Toggles ---
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp)),
      colors = CardDefaults.cardColors(containerColor = CyberCard)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        TurboToggleRow(
          icon = Icons.Default.Refresh,
          title = "Force Peak Refresh (120Hz)",
          subtitle = "Bypasses OEM display frequency drop in games",
          checked = turboState.forcePeakRefresh,
          onCheckedChange = { viewModel.toggleForcePeakRefresh(it) }
        )

        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CyberCardBorder))
        Spacer(modifier = Modifier.height(10.dp))

        TurboToggleRow(
          icon = Icons.Default.NotificationsOff,
          title = "Game Focus Shield (DND & RAM Lock)",
          subtitle = "Blocks incoming alerts & pins game process in memory",
          checked = turboState.gameFocusShield,
          onCheckedChange = { viewModel.toggleGameFocusShield(it) }
        )

        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CyberCardBorder))
        Spacer(modifier = Modifier.height(10.dp))

        TurboToggleRow(
          icon = Icons.Default.SyncDisabled,
          title = "Background Sync Throttling",
          subtitle = "Suspends background telemetry to prevent packet ping spikes",
          checked = turboState.backgroundSyncThrottling,
          onCheckedChange = { viewModel.toggleBackgroundSync(it) }
        )

        Spacer(modifier = Modifier.height(10.dp))
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(CyberCardBorder))
        Spacer(modifier = Modifier.height(10.dp))

        TurboToggleRow(
          icon = Icons.Default.Tv,
          title = "FPS Float Monitor HUD",
          subtitle = "Zero-overhead overlay displaying live fps & frametime",
          checked = turboState.fpsFloatMonitorHud,
          onCheckedChange = { viewModel.toggleFpsFloatMonitor(it) }
        )
      }
    }

    Spacer(modifier = Modifier.height(20.dp))

    // --- Quick Game Launcher ---
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
          imageVector = Icons.Default.Games,
          contentDescription = "Games",
          tint = CyberNeonCyan,
          modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
          text = "Quick Game Launcher",
          color = CyberTextPrimary,
          fontSize = 14.sp,
          fontWeight = FontWeight.Bold
        )
      }

      Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = "More",
        tint = CyberTextTertiary,
        modifier = Modifier.size(18.dp)
      )
    }

    Spacer(modifier = Modifier.height(12.dp))

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(12.dp),
      contentPadding = PaddingValues(horizontal = 2.dp)
    ) {
      items(quickGames) { game ->
        GameLauncherCard(
          game = game,
          onClick = { viewModel.launchGame(game) }
        )
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

@Composable
fun TurboToggleRow(
  icon: ImageVector,
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.weight(1f)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = if (checked) CyberNeonGreen else CyberTextSecondary,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(12.dp))
      Column {
        Text(
          text = title,
          color = CyberTextPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.SemiBold
        )
        Text(
          text = subtitle,
          color = CyberTextTertiary,
          fontSize = 10.sp
        )
      }
    }

    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = CyberNeonGreen,
        checkedTrackColor = CyberNeonGreen.copy(alpha = 0.35f),
        uncheckedThumbColor = CyberTextTertiary,
        uncheckedTrackColor = Color(0xFF1E293B)
      ),
      modifier = Modifier.testTag("toggle_${title.take(10)}")
    )
  }
}

@Composable
fun GameLauncherCard(
  game: QuickGame,
  onClick: () -> Unit
) {
  Column(
    horizontalAlignment = Alignment.CenterHorizontally,
    modifier = Modifier
      .width(78.dp)
      .clickable(onClick = onClick)
      .testTag("game_${game.id}")
  ) {
    Box(
      modifier = Modifier
        .size(62.dp)
        .clip(RoundedCornerShape(14.dp))
        .background(
          Brush.verticalGradient(
            listOf(
              Color(game.primaryColor).copy(alpha = 0.25f),
              CyberCardSecondary
            )
          )
        )
        .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp)),
      contentAlignment = Alignment.Center
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
      ) {
        Icon(
          imageVector = Icons.Default.Speed,
          contentDescription = game.title,
          tint = Color(game.primaryColor),
          modifier = Modifier.size(26.dp)
        )
        if (game.badgeText.isNotBlank()) {
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = game.badgeText,
            color = CyberTextPrimary,
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    Text(
      text = game.title,
      color = CyberTextPrimary,
      fontSize = 10.sp,
      fontWeight = FontWeight.SemiBold,
      maxLines = 1
    )
    Text(
      text = game.subtitle,
      color = CyberTextTertiary,
      fontSize = 8.sp,
      maxLines = 1
    )
  }
}
