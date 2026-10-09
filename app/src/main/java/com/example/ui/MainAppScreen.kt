package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CustomCrosshairCanvas
import com.example.ui.screens.AdbStudioScreen
import com.example.ui.screens.GfxConfiguratorScreen
import com.example.ui.screens.SensitivityAimLabScreen
import com.example.ui.screens.TurboDashboardScreen
import com.example.ui.theme.CyberBlack
import com.example.ui.theme.CyberCard
import com.example.ui.theme.CyberCardBorder
import com.example.ui.theme.CyberNeonCyan
import com.example.ui.theme.CyberNeonGreen
import com.example.ui.theme.CyberSurface
import com.example.ui.theme.CyberTextPrimary
import com.example.ui.theme.CyberTextSecondary
import com.example.ui.theme.CyberTextTertiary

@Composable
fun MainAppScreen(
  viewModel: ApexViewModel,
  modifier: Modifier = Modifier
) {
  var activeTab by remember { mutableIntStateOf(0) }
  val userMessage by viewModel.userMessage.collectAsState()
  val sensitivityState by viewModel.sensitivityState.collectAsState()
  val isRecoil by viewModel.isCrosshairRecoilActive.collectAsState()
  val snackbarHostState = remember { SnackbarHostState() }

  LaunchedEffect(userMessage) {
    userMessage?.let {
      snackbarHostState.showSnackbar(it)
      viewModel.clearUserMessage()
    }
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    snackbarHost = { SnackbarHost(snackbarHostState) },
    bottomBar = {
      NavigationBar(
        containerColor = CyberSurface,
        tonalElevation = 8.dp,
        modifier = Modifier
          .navigationBarsPadding()
          .testTag("main_bottom_nav")
      ) {
        // Tab 0: Turbo
        NavigationBarItem(
          selected = activeTab == 0,
          onClick = { activeTab = 0 },
          icon = {
            Icon(
              imageVector = Icons.Default.RocketLaunch,
              contentDescription = "Turbo",
              modifier = Modifier.size(20.dp)
            )
          },
          label = {
            Text(
              text = "Turbo",
              fontSize = 10.sp,
              fontWeight = if (activeTab == 0) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = CyberBlack,
            selectedTextColor = CyberNeonGreen,
            indicatorColor = CyberNeonGreen,
            unselectedIconColor = CyberTextTertiary,
            unselectedTextColor = CyberTextTertiary
          ),
          modifier = Modifier.testTag("nav_tab_turbo")
        )

        // Tab 1: GFX
        NavigationBarItem(
          selected = activeTab == 1,
          onClick = { activeTab = 1 },
          icon = {
            Icon(
              imageVector = Icons.Default.Tune,
              contentDescription = "GFX",
              modifier = Modifier.size(20.dp)
            )
          },
          label = {
            Text(
              text = "GFX",
              fontSize = 10.sp,
              fontWeight = if (activeTab == 1) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = CyberBlack,
            selectedTextColor = CyberNeonCyan,
            indicatorColor = CyberNeonCyan,
            unselectedIconColor = CyberTextTertiary,
            unselectedTextColor = CyberTextTertiary
          ),
          modifier = Modifier.testTag("nav_tab_gfx")
        )

        // Tab 2: ADB
        NavigationBarItem(
          selected = activeTab == 2,
          onClick = { activeTab = 2 },
          icon = {
            Icon(
              imageVector = Icons.Default.Terminal,
              contentDescription = "ADB",
              modifier = Modifier.size(20.dp)
            )
          },
          label = {
            Text(
              text = "ADB",
              fontSize = 10.sp,
              fontWeight = if (activeTab == 2) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = CyberBlack,
            selectedTextColor = CyberNeonCyan,
            indicatorColor = CyberNeonCyan,
            unselectedIconColor = CyberTextTertiary,
            unselectedTextColor = CyberTextTertiary
          ),
          modifier = Modifier.testTag("nav_tab_adb")
        )

        // Tab 3: Sensitivity
        NavigationBarItem(
          selected = activeTab == 3,
          onClick = { activeTab = 3 },
          icon = {
            Icon(
              imageVector = Icons.Default.TrackChanges,
              contentDescription = "Sensitivity",
              modifier = Modifier.size(20.dp)
            )
          },
          label = {
            Text(
              text = "Sensitivity",
              fontSize = 10.sp,
              fontWeight = if (activeTab == 3) FontWeight.Bold else FontWeight.Normal
            )
          },
          colors = NavigationBarItemDefaults.colors(
            selectedIconColor = CyberBlack,
            selectedTextColor = CyberNeonGreen,
            indicatorColor = CyberNeonGreen,
            unselectedIconColor = CyberTextTertiary,
            unselectedTextColor = CyberTextTertiary
          ),
          modifier = Modifier.testTag("nav_tab_sensitivity")
        )
      }
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(CyberBlack)
        .padding(innerPadding)
    ) {
      Crossfade(targetState = activeTab, label = "tab_fade") { tab ->
        when (tab) {
          0 -> TurboDashboardScreen(viewModel = viewModel)
          1 -> GfxConfiguratorScreen(viewModel = viewModel)
          2 -> AdbStudioScreen(viewModel = viewModel)
          3 -> SensitivityAimLabScreen(viewModel = viewModel)
        }
      }

      // Floating crosshair overlay when enabled in Aim Lab
      if (sensitivityState.isFloatingCrosshairActive) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .align(Alignment.Center),
          contentAlignment = Alignment.Center
        ) {
          CustomCrosshairCanvas(
            style = sensitivityState.crosshairStyle,
            colorHex = sensitivityState.crosshairColorHex,
            scaleDp = sensitivityState.crosshairScaleDp,
            opacity = sensitivityState.crosshairOpacity,
            isRecoil = isRecoil,
            modifier = Modifier.size(sensitivityState.crosshairScaleDp.dp * 2f)
          )
        }
      }
    }
  }
}
