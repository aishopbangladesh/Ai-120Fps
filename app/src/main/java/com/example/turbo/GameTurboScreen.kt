package com.example.turbo

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SyncDisabled
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardSecondary
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import com.example.ui.theme.CyberOrange
import com.example.ui.theme.CyberRed
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.CyberTextTertiary

@Composable
fun GameTurboScreen(
  viewModel: GameTurboViewModel,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val state by viewModel.turboState.collectAsState()
  val authState by viewModel.authUiState.collectAsState()
  val scrollState = rememberScrollState()

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 12.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // --- Header & Cloud Auth Banner ---
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
              imageVector = Icons.Default.RocketLaunch,
              contentDescription = "Game Turbo",
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
                  text = "GAME TURBO",
                  color = CyberBlack,
                  fontSize = 9.sp,
                  fontWeight = FontWeight.Black
                )
              }
            }
            Text(
              text = "Snapdragon 8 Gen 2 • Low Latency Mode",
              color = CyberTextSecondary,
              fontSize = 11.sp
            )
          }
        }

        // Auth status or Sign in with Google
        when (val auth = authState) {
          is AuthUiState.Authenticated -> {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(4.dp),
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(CyberNeonGreen.copy(alpha = 0.12f))
                .clickable { viewModel.signOut(context) }
                .padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
              Icon(Icons.Default.CloudDone, contentDescription = "Synced", tint = CyberNeonGreen, modifier = Modifier.size(14.dp))
              Text(
                text = auth.user.displayName?.take(8) ?: "Cloud Synced",
                color = CyberNeonGreen,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
          is AuthUiState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.size(18.dp), color = CyberNeonGreen, strokeWidth = 2.dp)
          }
          else -> {
            Box(
              modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(CyberNeonCyan.copy(alpha = 0.15f))
                .border(1.dp, CyberNeonCyan.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                .clickable { viewModel.signInWithGoogle(context) }
                .padding(horizontal = 8.dp, vertical = 5.dp)
                .testTag("google_signin_button")
            ) {
              Text(
                text = "Google Sign-In",
                color = CyberNeonCyan,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      }
    }

    // --- Big Master Toggle: Turbo Booster ---
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .border(
          1.dp,
          if (state.turboModeActive) CyberNeonGreen.copy(alpha = 0.8f) else CyberCardBorder,
          RoundedCornerShape(16.dp)
        ),
      colors = CardDefaults.cardColors(
        containerColor = if (state.turboModeActive) CyberCardSecondary else CyberCard
      )
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column {
            Text(
              text = if (state.turboModeActive) "120 FPS GAME TURBO: ACTIVE" else "120 FPS GAME TURBO: STANDBY",
              color = if (state.turboModeActive) CyberNeonGreen else CyberTextSecondary,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              letterSpacing = 0.5.sp
            )
            Text(
              text = "CPU governor & GPU pipeline locked to 120Hz peak",
              color = CyberTextTertiary,
              fontSize = 11.sp
            )
          }

          Switch(
            checked = state.turboModeActive,
            onCheckedChange = { viewModel.toggleTurboMode() },
            colors = SwitchDefaults.colors(
              checkedThumbColor = CyberBlack,
              checkedTrackColor = CyberNeonGreen,
              uncheckedThumbColor = CyberTextSecondary,
              uncheckedTrackColor = CyberCardSecondary
            ),
            modifier = Modifier.testTag("game_turbo_master_switch")
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Hardware Gauges Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceEvenly
        ) {
          StatusMetric(title = "Display", value = "120Hz", subtitle = "Locked", color = CyberNeonCyan)
          StatusMetric(title = "Target FPS", value = "120", subtitle = "Esports", color = CyberNeonGreen)
          StatusMetric(title = "Battery Temp", value = "${state.batteryTempCelsius}°C", subtitle = "Optimal", color = CyberOrange)
        }
      }
    }

    // --- RAM Monitor & Clear RAM Action ---
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .border(1.dp, CyberCardBorder, RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = CyberCard)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.Memory, contentDescription = "Memory", tint = CyberNeonGreen, modifier = Modifier.size(20.dp))
            Text(
              text = "RAM Performance Monitor",
              color = CyberTextPrimary,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Text(
            text = "${(state.availableRamMb / 1024f).toString().take(3)} GB / ${(state.totalRamMb / 1024f).toString().take(2)} GB Free",
            color = CyberNeonGreen,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
          )
        }

        LinearProgressIndicator(
          progress = { state.ramUsagePercentage },
          modifier = Modifier
            .fillMaxWidth()
            .height(8.dp)
            .clip(RoundedCornerShape(4.dp)),
          color = if (state.ramUsagePercentage > 0.8f) CyberRed else CyberNeonGreen,
          trackColor = CyberBlack,
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "Used: ${state.usedRamMb}MB (${(state.ramUsagePercentage * 100).toInt()}%)",
            color = CyberTextSecondary,
            fontSize = 11.sp
          )
          if (state.lastFreedMb > 0) {
            Text(
              text = "+${state.lastFreedMb}MB freed recently",
              color = CyberNeonCyan,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        // Clear RAM Button
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(
              Brush.horizontalGradient(listOf(CyberNeonGreen, CyberNeonCyan))
            )
            .clickable(enabled = !state.isCleaningRam) { viewModel.cleanRam() }
            .padding(vertical = 12.dp)
            .testTag("clear_ram_button"),
          contentAlignment = Alignment.Center
        ) {
          if (state.isCleaningRam) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              CircularProgressIndicator(modifier = Modifier.size(16.dp), color = CyberBlack, strokeWidth = 2.dp)
              Text("KILLING BACKGROUND PROCESSES...", color = CyberBlack, fontSize = 12.sp, fontWeight = FontWeight.Black)
            }
          } else {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Icon(Icons.Default.CleaningServices, contentDescription = "Clear RAM", tint = CyberBlack, modifier = Modifier.size(18.dp))
              Text("OPTIMIZE & CLEAR RAM NOW", color = CyberBlack, fontSize = 13.sp, fontWeight = FontWeight.Black)
            }
          }
        }
      }
    }

    // --- Background Processes Inspector ---
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .border(1.dp, CyberCardBorder, RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = CyberCard)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.DeveloperBoard, contentDescription = "Processes", tint = CyberNeonCyan, modifier = Modifier.size(18.dp))
            Text(
              text = "Monitored Background Processes (${state.backgroundProcesses.size})",
              color = CyberTextPrimary,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }

          IconButtonSmall(icon = Icons.Default.Refresh, onClick = { viewModel.scanBackgroundProcesses() })
        }

        if (state.backgroundProcesses.isEmpty()) {
          Text("No heavy background processes detected.", color = CyberTextSecondary, fontSize = 11.sp)
        } else {
          state.backgroundProcesses.take(8).forEach { proc ->
            ProcessRowItem(proc)
          }
        }
      }
    }

    // --- Gaming Turbo Optimization Toggles ---
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(16.dp))
        .border(1.dp, CyberCardBorder, RoundedCornerShape(16.dp)),
      colors = CardDefaults.cardColors(containerColor = CyberCard)
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        Text(
          text = "Turbo Gaming Rules",
          color = CyberTextPrimary,
          fontSize = 13.sp,
          fontWeight = FontWeight.Bold
        )

        TurboToggleRow(
          title = "Auto-Clear RAM on Turbo Launch",
          subtitle = "Automatically kill inactive background tasks upon starting game",
          checked = state.autoRamCleanOnLaunch,
          onCheckedChange = { viewModel.toggleAutoRamClean(it) }
        )

        TurboToggleRow(
          title = "Force 120Hz Peak Refresh Rate",
          subtitle = "Bypass vendor dynamic scaling to prevent 60Hz drops",
          checked = state.forcePeakRefreshRate,
          onCheckedChange = { viewModel.toggleForcePeakRefresh(it) }
        )

        TurboToggleRow(
          title = "Game Focus & DND Priority",
          subtitle = "Suppress floating banners and system popups",
          checked = state.gameFocusPriority,
          onCheckedChange = { viewModel.toggleGameFocusPriority(it) }
        )

        TurboToggleRow(
          title = "Background Sync Throttling",
          subtitle = "Reduce network bufferbloat & prevent ping spikes",
          checked = state.backgroundSyncThrottling,
          onCheckedChange = { viewModel.toggleBackgroundSyncThrottling(it) }
        )
      }
    }
  }
}

@Composable
private fun StatusMetric(title: String, value: String, subtitle: String, color: Color) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Text(text = title, color = CyberTextSecondary, fontSize = 10.sp)
    Text(text = value, color = color, fontSize = 18.sp, fontWeight = FontWeight.Black)
    Text(text = subtitle, color = CyberTextTertiary, fontSize = 9.sp)
  }
}

@Composable
private fun ProcessRowItem(proc: BackgroundProcessInfo) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clip(RoundedCornerShape(8.dp))
      .background(CyberCardSecondary)
      .padding(horizontal = 10.dp, vertical = 7.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = proc.appName,
        color = CyberTextPrimary,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = proc.packageName,
        color = CyberTextTertiary,
        fontSize = 9.sp,
        fontFamily = FontFamily.Monospace
      )
    }

    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = "${proc.memoryMb} MB",
        color = CyberOrange,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      )

      if (proc.isKilled) {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(CyberNeonGreen.copy(alpha = 0.2f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text("FREED", color = CyberNeonGreen, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
      } else {
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(CyberRed.copy(alpha = 0.2f))
            .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
          Text("ACTIVE", color = CyberRed, fontSize = 9.sp, fontWeight = FontWeight.Bold)
        }
      }
    }
  }
}

@Composable
private fun TurboToggleRow(
  title: String,
  subtitle: String,
  checked: Boolean,
  onCheckedChange: (Boolean) -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(text = title, color = CyberTextPrimary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
      Text(text = subtitle, color = CyberTextTertiary, fontSize = 10.sp)
    }
    Switch(
      checked = checked,
      onCheckedChange = onCheckedChange,
      colors = SwitchDefaults.colors(
        checkedThumbColor = CyberBlack,
        checkedTrackColor = CyberNeonGreen,
        uncheckedThumbColor = CyberTextSecondary,
        uncheckedTrackColor = CyberCardSecondary
      )
    )
  }
}

@Composable
private fun IconButtonSmall(icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
  Box(
    modifier = Modifier
      .size(28.dp)
      .clip(RoundedCornerShape(6.dp))
      .background(CyberCardSecondary)
      .clickable { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Icon(icon, contentDescription = "Action", tint = CyberNeonGreen, modifier = Modifier.size(16.dp))
  }
}
