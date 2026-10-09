package com.example.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeveloperMode
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.AdbCommandItem
import com.example.ui.ApexViewModel
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberCardSecondary
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.CyberTextTertiary

@Composable
fun AdbStudioScreen(
  viewModel: ApexViewModel,
  modifier: Modifier = Modifier
) {
  val adbCommands by viewModel.adbCommands.collectAsState()
  val selectedCommand by viewModel.selectedAdbCommand.collectAsState()
  val searchQuery by viewModel.adbSearchQuery.collectAsState()
  val tutorialModal by viewModel.showAdbTutorialModal.collectAsState()
  var showCustomBuilderDialog by remember { mutableStateOf(false) }
  val scrollState = rememberScrollState()

  val filteredCommands = adbCommands.filter {
    searchQuery.isBlank() ||
      it.title.contains(searchQuery, ignoreCase = true) ||
      it.commandText.contains(searchQuery, ignoreCase = true) ||
      it.category.contains(searchQuery, ignoreCase = true)
  }

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    // --- Search Bar ---
    OutlinedTextField(
      value = searchQuery,
      onValueChange = { viewModel.updateAdbSearch(it) },
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .background(CyberCard),
      placeholder = {
        Text("Search for Command Categories...", color = CyberTextTertiary, fontSize = 12.sp)
      },
      leadingIcon = {
        Icon(Icons.Default.Search, contentDescription = "Search", tint = CyberNeonCyan, modifier = Modifier.size(18.dp))
      },
      singleLine = true,
      colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = CyberNeonCyan,
        unfocusedBorderColor = CyberCardBorder,
        focusedTextColor = CyberTextPrimary,
        unfocusedTextColor = CyberTextPrimary,
        cursorColor = CyberNeonGreen
      )
    )

    Spacer(modifier = Modifier.height(16.dp))

    // --- Quick Connection Guide ---
    Text(
      text = "Quick Connection Guide",
      color = CyberTextSecondary,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      ConnectionGuideCard(
        title = "PC USB\nDebugging",
        icon = Icons.Default.Computer,
        onClick = { viewModel.openAdbTutorial("PC USB Debugging") },
        modifier = Modifier.weight(1f)
      )

      ConnectionGuideCard(
        title = "LADB\nOn-Device",
        icon = Icons.Default.Terminal,
        onClick = { viewModel.openAdbTutorial("LADB On-Device") },
        modifier = Modifier.weight(1f)
      )

      ConnectionGuideCard(
        title = "Wireless ADB\n/ Shizuku",
        icon = Icons.Default.Wifi,
        onClick = { viewModel.openAdbTutorial("Wireless ADB / Shizuku") },
        modifier = Modifier.weight(1f)
      )
    }

    Spacer(modifier = Modifier.height(18.dp))

    // --- Command Library Header ---
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "Command Library",
        color = CyberTextPrimary,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
      )
      Text(
        text = "${filteredCommands.size} Tweaks",
        color = CyberNeonGreen,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    // --- Command Inspector (Showing active command with copy button) ---
    selectedCommand?.let { cmd ->
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(12.dp))
          .border(1.dp, CyberNeonGreen.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = CyberCardSecondary)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Terminal,
                contentDescription = "Terminal",
                tint = CyberNeonGreen,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "Command Inspector",
                color = CyberTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Text(
              text = cmd.category,
              color = CyberNeonCyan,
              fontSize = 10.sp,
              fontWeight = FontWeight.SemiBold
            )
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Code box
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(CyberBlack)
              .border(0.5.dp, CyberCardBorder, RoundedCornerShape(8.dp))
              .padding(10.dp)
          ) {
            Text(
              text = cmd.commandText,
              color = CyberNeonGreen,
              fontSize = 11.sp,
              fontFamily = FontFamily.Monospace,
              lineHeight = 15.sp
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          Button(
            onClick = { viewModel.copySelectedAdbCommand() },
            modifier = Modifier
              .fillMaxWidth()
              .height(44.dp)
              .testTag("copy_selected_adb"),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = CyberNeonGreen,
              contentColor = CyberBlack
            )
          ) {
            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(15.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Copy Command", fontSize = 12.sp, fontWeight = FontWeight.Black)
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))
    }

    // --- Command List Cards ---
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
      filteredCommands.forEach { cmd ->
        val isSelected = selectedCommand?.id == cmd.id
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .border(
              1.dp,
              if (isSelected) CyberNeonCyan else CyberCardBorder,
              RoundedCornerShape(10.dp)
            )
            .clickable { viewModel.selectAdbCommand(cmd) },
          colors = CardDefaults.cardColors(
            containerColor = if (isSelected) CyberCardSecondary else CyberCard
          )
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = cmd.title,
                color = if (isSelected) CyberNeonCyan else CyberTextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = cmd.commandText,
                color = CyberTextTertiary,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                maxLines = 1
              )
            }

            Icon(
              imageVector = Icons.Default.ChevronRight,
              contentDescription = "Select",
              tint = if (isSelected) CyberNeonCyan else CyberTextTertiary,
              modifier = Modifier.size(16.dp)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // --- Copy All & Tutorial Buttons ---
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      OutlinedButton(
        onClick = { viewModel.copyAllAdbCommands() },
        modifier = Modifier
          .weight(1f)
          .height(46.dp),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberTextPrimary)
      ) {
        Icon(Icons.Default.ContentCopy, contentDescription = "Copy All", modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Copy All Commands", fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }

      OutlinedButton(
        onClick = { viewModel.openAdbTutorial("Connection & Tutorial") },
        modifier = Modifier
          .weight(1f)
          .height(46.dp),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CyberNeonCyan.copy(alpha = 0.5f)),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberNeonCyan)
      ) {
        Icon(Icons.Default.ElectricBolt, contentDescription = "Tutorial", modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text("Test Connection", fontSize = 11.sp, fontWeight = FontWeight.Bold)
      }
    }

    Spacer(modifier = Modifier.height(10.dp))

    // Custom Command Builder Button
    OutlinedButton(
      onClick = { showCustomBuilderDialog = true },
      modifier = Modifier
        .fillMaxWidth()
        .height(44.dp),
      shape = RoundedCornerShape(8.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
      colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberTextSecondary)
    ) {
      Icon(Icons.Default.Build, contentDescription = "Builder", modifier = Modifier.size(14.dp))
      Spacer(modifier = Modifier.width(6.dp))
      Text("Custom Command Builder", fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }

    Spacer(modifier = Modifier.height(24.dp))
  }

  // --- Tutorial Dialog ---
  tutorialModal?.let { title ->
    AlertDialog(
      onDismissRequest = { viewModel.closeAdbTutorial() },
      containerColor = CyberCard,
      title = {
        Text(text = title, color = CyberNeonCyan, fontSize = 16.sp, fontWeight = FontWeight.Bold)
      },
      text = {
        Column {
          when (title) {
            "PC USB Debugging" -> {
              Text("1. Enable Developer Options: Tap Build Number 7 times.", color = CyberTextPrimary, fontSize = 12.sp)
              Spacer(modifier = Modifier.height(4.dp))
              Text("2. Turn ON 'USB Debugging' in Developer Options.", color = CyberTextPrimary, fontSize = 12.sp)
              Spacer(modifier = Modifier.height(4.dp))
              Text("3. Connect to PC via USB and execute command: `adb devices`.", color = CyberTextSecondary, fontSize = 11.sp)
            }
            "LADB On-Device" -> {
              Text("1. Install LADB or Brevent on your phone.", color = CyberTextPrimary, fontSize = 12.sp)
              Spacer(modifier = Modifier.height(4.dp))
              Text("2. Enable 'Wireless Debugging' in split-screen mode.", color = CyberTextPrimary, fontSize = 12.sp)
              Spacer(modifier = Modifier.height(4.dp))
              Text("3. Enter the 5-digit port & 6-digit pairing code.", color = CyberTextSecondary, fontSize = 11.sp)
            }
            else -> {
              Text("Shizuku connects directly to system APIs without PC reboots. Simply run the copied commands into Termux or Shizuku Runner.", color = CyberTextPrimary, fontSize = 12.sp)
            }
          }
        }
      },
      confirmButton = {
        Button(
          onClick = { viewModel.closeAdbTutorial() },
          colors = ButtonDefaults.buttonColors(containerColor = CyberNeonGreen, contentColor = CyberBlack)
        ) {
          Text("Got It", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    )
  }

  // --- Custom Command Builder Dialog ---
  if (showCustomBuilderDialog) {
    var customTargetRate by remember { mutableStateOf("120") }
    AlertDialog(
      onDismissRequest = { showCustomBuilderDialog = false },
      containerColor = CyberCard,
      title = {
        Text("Custom Command Builder", color = CyberTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
      },
      text = {
        Column {
          Text("Select Target Refresh Rate (Hz):", color = CyberTextSecondary, fontSize = 12.sp)
          Spacer(modifier = Modifier.height(8.dp))
          Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("90", "120", "144", "165").forEach { rate ->
              val sel = customTargetRate == rate
              Box(
                modifier = Modifier
                  .clip(RoundedCornerShape(6.dp))
                  .background(if (sel) CyberNeonGreen else CyberCardSecondary)
                  .clickable { customTargetRate = rate }
                  .padding(horizontal = 10.dp, vertical = 6.dp)
              ) {
                Text(
                  text = "${rate}Hz",
                  color = if (sel) CyberBlack else CyberTextPrimary,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }
          }
          Spacer(modifier = Modifier.height(12.dp))
          Text(
            text = "Generated Command:",
            color = CyberNeonCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
          Spacer(modifier = Modifier.height(4.dp))
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(6.dp))
              .background(CyberBlack)
              .padding(8.dp)
          ) {
            Text(
              text = "adb shell settings put system peak_refresh_rate $customTargetRate.0 && adb shell settings put system min_refresh_rate $customTargetRate.0",
              color = CyberNeonGreen,
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace
            )
          }
        }
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.copyToClipboard(
              "Custom ADB Command",
              "adb shell settings put system peak_refresh_rate $customTargetRate.0 && adb shell settings put system min_refresh_rate $customTargetRate.0"
            )
            showCustomBuilderDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = CyberNeonGreen, contentColor = CyberBlack)
        ) {
          Text("Copy & Save", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showCustomBuilderDialog = false }) {
          Text("Cancel", color = CyberTextSecondary, fontSize = 11.sp)
        }
      }
    )
  }
}

@Composable
fun ConnectionGuideCard(
  title: String,
  icon: ImageVector,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .height(64.dp)
      .clip(RoundedCornerShape(10.dp))
      .background(CyberCard)
      .border(1.dp, CyberCardBorder, RoundedCornerShape(10.dp))
      .clickable(onClick = onClick)
      .padding(8.dp),
    contentAlignment = Alignment.Center
  ) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = icon,
        contentDescription = title,
        tint = CyberNeonCyan,
        modifier = Modifier.size(18.dp)
      )
      Spacer(modifier = Modifier.height(4.dp))
      Text(
        text = title,
        color = CyberTextPrimary,
        fontSize = 9.sp,
        fontWeight = FontWeight.Bold,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
        lineHeight = 11.sp
      )
    }
  }
}
