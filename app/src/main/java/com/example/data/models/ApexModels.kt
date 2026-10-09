package com.example.data.models

import androidx.compose.ui.graphics.Color

data class TurboData(
  val chipsetName: String = "Snapdragon 8 Gen 2 / 12GB RAM",
  val totalRamGb: Double = 12.0,
  val freeRamGb: Double = 5.8,
  val temperatureC: Double = 37.4,
  val temperatureStatus: String = "Cool",
  val refreshRate: Int = 120,
  val fpsLocked: Int = 120,
  val isTurboActive: Boolean = false,
  val forcePeakRefresh: Boolean = true,
  val gameFocusShield: Boolean = true,
  val backgroundSyncThrottling: Boolean = false,
  val fpsFloatMonitorHud: Boolean = false,
  val boostScore: Int = 94
)

data class QuickGame(
  val id: String,
  val title: String,
  val subtitle: String = "",
  val badgeText: String = "",
  val primaryColor: Long = 0xFF10B981,
  val packageHint: String = "com.tencent.ig"
)

enum class GfxPresetType(val label: String) {
  FPS_120("120FPS\nUltra Smooth"),
  FPS_90("90FPS\nCrisp"),
  BALANCED_HDR("Balanced\nHDR"),
  POTATO("Potato\nAnti-Lag")
}

data class GfxConfig(
  val selectedPreset: GfxPresetType = GfxPresetType.FPS_120,
  val gameVersion: String = "Global 3.4",
  val resolution: String = "1080p FHD Native",
  val graphicsQuality: String = "Smooth",
  val frameRate: String = "120 FPS Extreme",
  val antiAliasing: String = "Disabled (Max FPS)",
  val renderingQuality: String = "Low",
  val shadowsEffects: String = "Disabled for Zero Stutter",
  val soundQuality: String = "Low (Less CPU)",
  val stabilityRating: Int = 98
)

data class AdbCommandItem(
  val id: String,
  val category: String,
  val title: String,
  val description: String,
  val commandText: String,
  val isSelected: Boolean = false
)

enum class CrosshairStyle {
  CROSS, CIRCLE, DOT, T_SHAPE, CHEVRON
}

data class SensitivityConfig(
  val deviceScreenSize: String = "Phone 6.7\"",
  val noScopeCamera: Int = 120,
  val redDotHolo: Int = 60,
  val scope3x: Int = 35,
  val scope4x: Int = 27,
  val scope6x: Int = 18,
  val scope8x: Int = 12,
  val gyroscopeAlwaysOn: Boolean = true,
  val crosshairStyle: CrosshairStyle = CrosshairStyle.CROSS,
  val crosshairColorHex: String = "#00FF66",
  val crosshairScaleDp: Float = 32f,
  val crosshairOpacity: Float = 0.95f,
  val currentPingMs: Int = 24,
  val dnsProvider: String = "Cloudflare 1.1.1.1 Gaming DNS",
  val isFloatingCrosshairActive: Boolean = false,
  val currentCloudCode: String = "7120-4491-9230-1082-901"
)
