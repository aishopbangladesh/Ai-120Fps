package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.models.GfxPresetType
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
fun GfxConfiguratorScreen(
  viewModel: ApexViewModel,
  modifier: Modifier = Modifier
) {
  val gfxState by viewModel.gfxState.collectAsState()
  val showActiveSavModal by viewModel.showActiveSavModal.collectAsState()
  val scrollState = rememberScrollState()

  val gameVersions = listOf("Global 3.4", "BGMI", "KR / JP", "VNG", "GameLoop")

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    // --- Header ---
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = "GFX 120FPS",
        color = CyberTextPrimary,
        fontSize = 20.sp,
        fontWeight = FontWeight.Black,
        letterSpacing = (-0.5).sp
      )

      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        // Save Button
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CyberNeonGreen.copy(alpha = 0.15f))
            .border(1.dp, CyberNeonGreen.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .clickable { viewModel.shareGfxConfig() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Save,
              contentDescription = "Save",
              tint = CyberNeonGreen,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Save",
              color = CyberNeonGreen,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }

        // Share Button
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CyberCardSecondary)
            .border(1.dp, CyberCardBorder, RoundedCornerShape(8.dp))
            .clickable { viewModel.shareGfxConfig() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Share,
              contentDescription = "Share",
              tint = CyberTextSecondary,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "Share",
              color = CyberTextSecondary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // --- Quick Presets Row ---
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      GfxPresetType.values().forEach { preset ->
        val isSelected = gfxState.selectedPreset == preset
        Box(
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) CyberNeonGreen.copy(alpha = 0.18f) else CyberCard)
            .border(
              width = if (isSelected) 1.5.dp else 1.dp,
              color = if (isSelected) CyberNeonGreen else CyberCardBorder,
              shape = RoundedCornerShape(10.dp)
            )
            .clickable { viewModel.selectGfxPreset(preset) }
            .padding(4.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = preset.label,
            color = if (isSelected) CyberNeonGreen else CyberTextSecondary,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.SemiBold,
            lineHeight = 12.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // --- Game Version Selector ---
    Text(
      text = "Game Version",
      color = CyberTextSecondary,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold
    )

    Spacer(modifier = Modifier.height(8.dp))

    LazyRow(
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      items(gameVersions) { version ->
        val isSelected = gfxState.gameVersion == version
        Box(
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) CyberCardSecondary else CyberCard)
            .border(
              1.dp,
              if (isSelected) CyberNeonCyan else CyberCardBorder,
              RoundedCornerShape(8.dp)
            )
            .clickable { viewModel.updateGameVersion(version) }
            .padding(horizontal = 14.dp, vertical = 7.dp)
        ) {
          Text(
            text = version,
            color = if (isSelected) CyberNeonCyan else CyberTextTertiary,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // --- Config Dropdown Cards ---
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp)),
      colors = CardDefaults.cardColors(containerColor = CyberCard)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        GfxDropdownItem(
          label = "Resolution",
          currentValue = gfxState.resolution,
          options = listOf("540p qHD (Potato)", "720p HD Smooth", "1080p FHD Native", "1440p 2K WQHD"),
          onSelect = { viewModel.updateResolution(it) }
        )

        GfxDivider()

        GfxDropdownItem(
          label = "Graphics Quality",
          currentValue = gfxState.graphicsQuality,
          options = listOf("So Smooth", "Smooth", "Balanced", "HD", "HDR", "Ultra HD"),
          onSelect = { viewModel.updateGraphicsQuality(it) }
        )

        GfxDivider()

        GfxDropdownItem(
          label = "Frame Rate",
          currentValue = gfxState.frameRate,
          options = listOf("40 FPS Ultra", "60 FPS Extreme", "90 FPS Ultra", "120 FPS Extreme"),
          onSelect = { viewModel.updateFrameRate(it) }
        )

        GfxDivider()

        GfxDropdownItem(
          label = "Anti-Aliasing (MSAA)",
          currentValue = gfxState.antiAliasing,
          options = listOf("Disabled (Max FPS)", "2X MSAA Balanced", "4X MSAA Ultra Smooth"),
          onSelect = { viewModel.updateAntiAliasing(it) }
        )

        GfxDivider()

        GfxDropdownItem(
          label = "Rendering Quality",
          currentValue = gfxState.renderingQuality,
          options = listOf("Low", "Medium", "High"),
          onSelect = { viewModel.updateRenderingQuality(it) }
        )

        GfxDivider()

        GfxDropdownItem(
          label = "Shadows & Effects",
          currentValue = gfxState.shadowsEffects,
          options = listOf("Disabled for Zero Stutter", "Low Distance", "Medium Dynamic"),
          onSelect = { viewModel.updateShadows(it) }
        )

        GfxDivider()

        GfxDropdownItem(
          label = "Sound Quality",
          currentValue = gfxState.soundQuality,
          options = listOf("Low (Less CPU)", "High Quality", "Ultra 3D Spatial"),
          onSelect = { viewModel.updateSoundQuality(it) }
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // --- Generated Active Config Summary Card ---
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(12.dp))
        .border(1.dp, CyberNeonGreen.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
      colors = CardDefaults.cardColors(containerColor = CyberCardSecondary)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Text(
          text = "Generated Active config Summary",
          color = CyberTextSecondary,
          fontSize = 11.sp,
          fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Bolt,
            contentDescription = "Lightning",
            tint = CyberNeonGreen,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(6.dp))
          Text(
            text = "${gfxState.stabilityRating}% FPS Stability rating",
            color = CyberNeonGreen,
            fontSize = 15.sp,
            fontWeight = FontWeight.Black
          )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
          text = "Profile: ${gfxState.resolution} • ${gfxState.graphicsQuality} • ${gfxState.frameRate}",
          color = CyberTextTertiary,
          fontSize = 10.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // --- Action Buttons ---
    Button(
      onClick = { viewModel.applyGfxAndOpenGuide() },
      modifier = Modifier
        .fillMaxWidth()
        .height(52.dp)
        .testTag("apply_gfx_button"),
      shape = RoundedCornerShape(10.dp),
      colors = ButtonDefaults.buttonColors(
        containerColor = CyberNeonGreen,
        contentColor = CyberBlack
      )
    ) {
      Text(
        text = "Apply GFX & Generate Active.sav Guide",
        fontSize = 13.sp,
        fontWeight = FontWeight.Black
      )
    }

    Spacer(modifier = Modifier.height(10.dp))

    OutlinedButton(
      onClick = { viewModel.restoreDefaultGraphics() },
      modifier = Modifier
        .fillMaxWidth()
        .height(48.dp),
      shape = RoundedCornerShape(10.dp),
      border = androidx.compose.foundation.BorderStroke(1.dp, CyberCardBorder),
      colors = ButtonDefaults.outlinedButtonColors(
        contentColor = CyberTextSecondary
      )
    ) {
      Icon(
        imageVector = Icons.Default.Restore,
        contentDescription = "Restore",
        tint = CyberTextSecondary,
        modifier = Modifier.size(16.dp)
      )
      Spacer(modifier = Modifier.width(6.dp))
      Text(
        text = "Restore Default Graphics",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold
      )
    }

    Spacer(modifier = Modifier.height(24.dp))
  }

  // --- Active.sav Generator Guide Dialog ---
  if (showActiveSavModal) {
    AlertDialog(
      onDismissRequest = { viewModel.closeActiveSavModal() },
      containerColor = CyberCard,
      title = {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Bolt,
            contentDescription = "Active.sav",
            tint = CyberNeonGreen,
            modifier = Modifier.size(20.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = "Active.sav Generated",
            color = CyberTextPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
          )
        }
      },
      text = {
        Column {
          Text(
            text = "Your 120 FPS Active.sav configuration file has been generated with 120FPS unlock parameters.",
            color = CyberTextSecondary,
            fontSize = 12.sp
          )
          Spacer(modifier = Modifier.height(10.dp))
          Text(
            text = "Installation Directory:",
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
              .border(0.5.dp, CyberCardBorder, RoundedCornerShape(6.dp))
              .padding(8.dp)
          ) {
            Text(
              text = "Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/SaveGames/Active.sav",
              color = CyberTextTertiary,
              fontSize = 9.sp,
              fontFamily = FontFamily.Monospace
            )
          }
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = "Tip: Use Shizuku or FV File Explorer on Android 11+ to grant read/write access to Android/data folder.",
            color = CyberTextSecondary,
            fontSize = 10.sp
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.copyToClipboard(
              "Active.sav Path",
              "Android/data/com.tencent.ig/files/UE4Game/ShadowTrackerExtra/ShadowTrackerExtra/Saved/SaveGames/"
            )
            viewModel.closeActiveSavModal()
          },
          colors = ButtonDefaults.buttonColors(containerColor = CyberNeonGreen, contentColor = CyberBlack)
        ) {
          Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(14.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Copy Path & Close", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { viewModel.closeActiveSavModal() }) {
          Text("Close", color = CyberTextSecondary, fontSize = 11.sp)
        }
      }
    )
  }
}

@Composable
fun GfxDivider() {
  Spacer(modifier = Modifier.height(8.dp))
  Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(CyberCardBorder))
  Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun GfxDropdownItem(
  label: String,
  currentValue: String,
  options: List<String>,
  onSelect: (String) -> Unit
) {
  var expanded by remember { mutableStateOf(false) }

  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { expanded = true }
      .padding(vertical = 4.dp),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Text(
      text = label,
      color = CyberTextPrimary,
      fontSize = 12.sp,
      fontWeight = FontWeight.Medium
    )

    Box {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clip(RoundedCornerShape(6.dp))
          .background(CyberCardSecondary)
          .border(0.5.dp, CyberCardBorder, RoundedCornerShape(6.dp))
          .padding(horizontal = 8.dp, vertical = 4.dp)
      ) {
        Text(
          text = currentValue,
          color = CyberNeonCyan,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
        Icon(
          imageVector = Icons.Default.ArrowDropDown,
          contentDescription = "Dropdown",
          tint = CyberTextSecondary,
          modifier = Modifier.size(16.dp)
        )
      }

      DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false },
        modifier = Modifier.background(CyberCard)
      ) {
        options.forEach { option ->
          DropdownMenuItem(
            text = {
              Text(
                text = option,
                color = if (option == currentValue) CyberNeonGreen else CyberTextPrimary,
                fontSize = 12.sp
              )
            },
            onClick = {
              onSelect(option)
              expanded = false
            }
          )
        }
      }
    }
  }
}
