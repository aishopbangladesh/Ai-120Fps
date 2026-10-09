package com.example.ui.screens

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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.NetworkCheck
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.example.data.models.CrosshairStyle
import com.example.ui.ApexViewModel
import com.example.ui.components.CustomCrosshairCanvas
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
fun SensitivityAimLabScreen(
  viewModel: ApexViewModel,
  modifier: Modifier = Modifier
) {
  val sensitivityState by viewModel.sensitivityState.collectAsState()
  val isPingTesting by viewModel.isPingTesting.collectAsState()
  val isRecoil by viewModel.isCrosshairRecoilActive.collectAsState()
  var showImportCodeDialog by remember { mutableStateOf(false) }
  var screenDropdownExpanded by remember { mutableStateOf(false) }
  var dnsDropdownExpanded by remember { mutableStateOf(false) }
  val scrollState = rememberScrollState()

  val phoneSizes = listOf("Phone 6.1\"", "Phone 6.7\"", "Tablet 11\"", "iPad Pro 12.9\"")
  val dnsOptions = listOf(
    "Cloudflare 1.1.1.1 Gaming DNS",
    "Google 8.8.8.8 Low Latency",
    "Quad9 9.9.9.9 Secure Esports",
    "AdGuard DNS (Anti-Lag)"
  )

  Column(
    modifier = modifier
      .fillMaxSize()
      .background(CyberBlack)
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    // --- Header & Aspect Ratio Converter ---
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Sensitivity Calculator",
          color = CyberTextPrimary,
          fontSize = 17.sp,
          fontWeight = FontWeight.Black
        )
        Text(
          text = "Aspect ratio converter",
          color = CyberTextSecondary,
          fontSize = 11.sp
        )
      }

      // Device selector dropdown
      Box {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CyberCard)
            .border(1.dp, CyberCardBorder, RoundedCornerShape(8.dp))
            .clickable { screenDropdownExpanded = true }
            .padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
          Icon(
            imageVector = Icons.Default.PhoneAndroid,
            contentDescription = "Screen Size",
            tint = CyberNeonCyan,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = sensitivityState.deviceScreenSize,
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
          expanded = screenDropdownExpanded,
          onDismissRequest = { screenDropdownExpanded = false },
          modifier = Modifier.background(CyberCard)
        ) {
          phoneSizes.forEach { size ->
            DropdownMenuItem(
              text = { Text(size, color = CyberTextPrimary, fontSize = 11.sp) },
              onClick = {
                viewModel.updateDeviceScreenSize(size)
                screenDropdownExpanded = false
              }
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(14.dp))

    // --- Scope Sensitivity Sliders Card ---
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp)),
      colors = CardDefaults.cardColors(containerColor = CyberCard)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        ScopeSliderRow("No Scope (Camera)", sensitivityState.noScopeCamera, 50..200) {
          viewModel.updateNoScope(it)
        }
        ScopeSliderRow("Red Dot / Holographic", sensitivityState.redDotHolo, 20..150) {
          viewModel.updateRedDot(it)
        }
        ScopeSliderRow("3x Scope", sensitivityState.scope3x, 10..100) {
          viewModel.updateScope3x(it)
        }
        ScopeSliderRow("4x Scope", sensitivityState.scope4x, 10..80) {
          viewModel.updateScope4x(it)
        }
        ScopeSliderRow("6x Scope", sensitivityState.scope6x, 5..50) {
          viewModel.updateScope6x(it)
        }
        ScopeSliderRow("8x Scope", sensitivityState.scope8x, 5..40) {
          viewModel.updateScope8x(it)
        }

        Spacer(modifier = Modifier.height(6.dp))
        Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(CyberCardBorder))
        Spacer(modifier = Modifier.height(8.dp))

        // Gyroscope Always On Switch
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Sensors,
              contentDescription = "Gyro",
              tint = CyberNeonGreen,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "Gyroscope Always On",
              color = CyberTextPrimary,
              fontSize = 12.sp,
              fontWeight = FontWeight.SemiBold
            )
          }
          Switch(
            checked = sensitivityState.gyroscopeAlwaysOn,
            onCheckedChange = { viewModel.toggleGyroscope(it) },
            colors = SwitchDefaults.colors(
              checkedThumbColor = CyberNeonGreen,
              checkedTrackColor = CyberNeonGreen.copy(alpha = 0.35f),
              uncheckedThumbColor = CyberTextTertiary,
              uncheckedTrackColor = Color(0xFF1E293B)
            )
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // --- Share Code / Import Pro Player Code ---
    Text(
      text = "Share Code / Import Pro Player Code",
      color = CyberTextSecondary,
      fontSize = 12.sp,
      fontWeight = FontWeight.SemiBold
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      listOf("Jonathan", "Mortal", "Scout").forEach { proName ->
        Box(
          modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(CyberCard)
            .border(1.dp, CyberCardBorder, RoundedCornerShape(8.dp))
            .clickable { viewModel.loadProPlayerPreset(proName) }
            .padding(vertical = 8.dp),
          contentAlignment = Alignment.Center
        ) {
          Text(
            text = proName,
            color = CyberNeonCyan,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(8.dp))

    // Code copy/import row
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(8.dp))
        .background(CyberCardSecondary)
        .border(0.5.dp, CyberCardBorder, RoundedCornerShape(8.dp))
        .padding(horizontal = 10.dp, vertical = 6.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween
    ) {
      Text(
        text = sensitivityState.currentCloudCode,
        color = CyberTextSecondary,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace
      )
      Row {
        TextButton(onClick = { viewModel.copyCloudCode() }) {
          Text("Copy", color = CyberNeonGreen, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
        TextButton(onClick = { showImportCodeDialog = true }) {
          Text("Import", color = CyberNeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // --- Custom Crosshair Overlay Lab ---
    Text(
      text = "Custom Crosshair Overlay Lab",
      color = CyberTextPrimary,
      fontSize = 14.sp,
      fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(8.dp))

    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp)),
      colors = CardDefaults.cardColors(containerColor = CyberCard)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          // Left Column: Controls (Style, Color, Scale, Opacity)
          Column(modifier = Modifier.weight(1.1f)) {
            Text("Style", color = CyberTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
              listOf(
                CrosshairStyle.CROSS to "+ Cross",
                CrosshairStyle.CIRCLE to "◯",
                CrosshairStyle.DOT to "•"
              ).forEach { (st, label) ->
                val sel = sensitivityState.crosshairStyle == st
                Box(
                  modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(if (sel) CyberNeonGreen.copy(alpha = 0.2f) else CyberCardSecondary)
                    .border(
                      0.5.dp,
                      if (sel) CyberNeonGreen else CyberCardBorder,
                      RoundedCornerShape(6.dp)
                    )
                    .clickable { viewModel.updateCrosshairStyle(st) }
                    .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                  Text(
                    text = label,
                    color = if (sel) CyberNeonGreen else CyberTextSecondary,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Color", color = CyberTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
              listOf(
                "#00FF66" to CyberNeonGreen,
                "#00E5FF" to CyberNeonCyan,
                "#FF2A55" to CyberRed,
                "#FF9900" to CyberOrange
              ).forEach { (hex, clr) ->
                val sel = sensitivityState.crosshairColorHex == hex
                Box(
                  modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(clr)
                    .border(
                      width = if (sel) 2.dp else 0.dp,
                      color = if (sel) CyberTextPrimary else Color.Transparent,
                      shape = CircleShape
                    )
                    .clickable { viewModel.updateCrosshairColor(hex) }
                )
              }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Scale", color = CyberTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Slider(
              value = sensitivityState.crosshairScaleDp,
              onValueChange = { viewModel.updateCrosshairScale(it) },
              valueRange = 16f..44f,
              colors = SliderDefaults.colors(
                thumbColor = CyberNeonGreen,
                activeTrackColor = CyberNeonGreen,
                inactiveTrackColor = Color(0xFF1E293B)
              ),
              modifier = Modifier.height(28.dp)
            )

            Text("Opacity", color = CyberTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Slider(
              value = sensitivityState.crosshairOpacity,
              onValueChange = { viewModel.updateCrosshairOpacity(it) },
              valueRange = 0.3f..1.0f,
              colors = SliderDefaults.colors(
                thumbColor = CyberNeonCyan,
                activeTrackColor = CyberNeonCyan,
                inactiveTrackColor = Color(0xFF1E293B)
              ),
              modifier = Modifier.height(28.dp)
            )
          }

          // Right Column: Live Target Box & Recoil Test
          Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(0.9f)
          ) {
            CustomCrosshairCanvas(
              style = sensitivityState.crosshairStyle,
              colorHex = sensitivityState.crosshairColorHex,
              scaleDp = sensitivityState.crosshairScaleDp,
              opacity = sensitivityState.crosshairOpacity,
              isRecoil = isRecoil,
              modifier = Modifier
                .size(110.dp)
                .clickable { viewModel.triggerRecoilTest() }
            )

            Spacer(modifier = Modifier.height(6.dp))

            Button(
              onClick = { viewModel.triggerRecoilTest() },
              modifier = Modifier.fillMaxWidth().height(32.dp),
              shape = RoundedCornerShape(6.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = CyberCardSecondary,
                contentColor = CyberNeonGreen
              )
            ) {
              Text("Tap to Recoil", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    // --- Network Ping Stabilizer ---
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(14.dp))
        .border(1.dp, CyberCardBorder, RoundedCornerShape(14.dp)),
      colors = CardDefaults.cardColors(containerColor = CyberCard)
    ) {
      Column(modifier = Modifier.padding(14.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.NetworkCheck,
              contentDescription = "Ping",
              tint = CyberNeonGreen,
              modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Network Ping Stabilizer",
              color = CyberTextPrimary,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold
            )
          }

          Text(
            text = "Current: ${sensitivityState.currentPingMs}ms (Stable Green)",
            color = CyberNeonGreen,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
          )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // DNS Switcher Dropdown
        Text("DNS Switcher", color = CyberTextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Box {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(8.dp))
              .background(CyberCardSecondary)
              .border(0.5.dp, CyberCardBorder, RoundedCornerShape(8.dp))
              .clickable { dnsDropdownExpanded = true }
              .padding(horizontal = 10.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = sensitivityState.dnsProvider,
              color = CyberNeonCyan,
              fontSize = 11.sp,
              fontWeight = FontWeight.SemiBold
            )
            Icon(Icons.Default.ArrowDropDown, contentDescription = "Select DNS", tint = CyberTextSecondary)
          }

          DropdownMenu(
            expanded = dnsDropdownExpanded,
            onDismissRequest = { dnsDropdownExpanded = false },
            modifier = Modifier.background(CyberCard)
          ) {
            dnsOptions.forEach { dns ->
              DropdownMenuItem(
                text = { Text(dns, color = CyberTextPrimary, fontSize = 11.sp) },
                onClick = {
                  viewModel.updateDnsProvider(dns)
                  dnsDropdownExpanded = false
                }
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Action Buttons: Toggle Floating Crosshair & Test Ping
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          OutlinedButton(
            onClick = { viewModel.toggleFloatingCrosshair() },
            modifier = Modifier.weight(1f).height(44.dp),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(
              1.dp,
              if (sensitivityState.isFloatingCrosshairActive) CyberNeonGreen else CyberCardBorder
            ),
            colors = ButtonDefaults.outlinedButtonColors(
              contentColor = if (sensitivityState.isFloatingCrosshairActive) CyberNeonGreen else CyberTextPrimary
            )
          ) {
            Text("Toggle Floating Crosshair", fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
          }

          Button(
            onClick = { viewModel.testPingAndJitter() },
            enabled = !isPingTesting,
            modifier = Modifier.weight(1f).height(44.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
              containerColor = CyberNeonGreen,
              contentColor = CyberBlack
            )
          ) {
            Text(
              text = if (isPingTesting) "Pinging..." else "Test Ping & Jitter",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }

  // --- Import Code Dialog ---
  if (showImportCodeDialog) {
    var inputCode by remember { mutableStateOf("") }
    AlertDialog(
      onDismissRequest = { showImportCodeDialog = false },
      containerColor = CyberCard,
      title = { Text("Import Sensitivity Code", color = CyberTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
      text = {
        Column {
          Text("Paste cloud code (e.g. 7120-4491-9230-1082-901):", color = CyberTextSecondary, fontSize = 11.sp)
          Spacer(modifier = Modifier.height(8.dp))
          OutlinedTextField(
            value = inputCode,
            onValueChange = { inputCode = it },
            placeholder = { Text("7120-4491-xxxx-xxxx-xxx", color = CyberTextTertiary, fontSize = 11.sp) },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
              focusedBorderColor = CyberNeonGreen,
              unfocusedBorderColor = CyberCardBorder,
              focusedTextColor = CyberTextPrimary,
              unfocusedTextColor = CyberTextPrimary
            )
          )
        }
      },
      confirmButton = {
        Button(
          onClick = {
            viewModel.importCloudCode(inputCode)
            showImportCodeDialog = false
          },
          colors = ButtonDefaults.buttonColors(containerColor = CyberNeonGreen, contentColor = CyberBlack)
        ) {
          Text("Apply", fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
      },
      dismissButton = {
        TextButton(onClick = { showImportCodeDialog = false }) {
          Text("Cancel", color = CyberTextSecondary, fontSize = 11.sp)
        }
      }
    )
  }
}

@Composable
fun ScopeSliderRow(
  label: String,
  currentValue: Int,
  range: IntRange,
  onValueChange: (Int) -> Unit
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 2.dp),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(
      text = label,
      color = CyberTextPrimary,
      fontSize = 11.sp,
      fontWeight = FontWeight.Medium,
      modifier = Modifier.width(135.dp)
    )

    Slider(
      value = currentValue.toFloat(),
      onValueChange = { onValueChange(it.toInt()) },
      valueRange = range.first.toFloat()..range.last.toFloat(),
      colors = SliderDefaults.colors(
        thumbColor = CyberNeonGreen,
        activeTrackColor = CyberNeonGreen,
        inactiveTrackColor = Color(0xFF1E293B)
      ),
      modifier = Modifier
        .weight(1f)
        .height(28.dp)
        .padding(horizontal = 4.dp)
    )

    Text(
      text = "$currentValue%",
      color = CyberNeonGreen,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.width(36.dp),
      textAlign = androidx.compose.ui.text.style.TextAlign.End
    )
  }
}
