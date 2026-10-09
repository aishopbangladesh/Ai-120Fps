package com.example.turbo

data class BackgroundProcessInfo(
  val packageName: String = "",
  val appName: String = "",
  val memoryMb: Int = 0,
  val isImportant: Boolean = false,
  val isKilled: Boolean = false
)

data class GameTurboState(
  val turboModeActive: Boolean = false,
  val forcePeakRefreshRate: Boolean = true,
  val gameFocusPriority: Boolean = true,
  val backgroundSyncThrottling: Boolean = true,
  val autoRamCleanOnLaunch: Boolean = true,
  val totalRamMb: Long = 12288L,
  val usedRamMb: Long = 6340L,
  val availableRamMb: Long = 5948L,
  val ramUsagePercentage: Float = 0.52f,
  val backgroundProcesses: List<BackgroundProcessInfo> = emptyList(),
  val isScanningProcesses: Boolean = false,
  val isCleaningRam: Boolean = false,
  val lastFreedMb: Int = 0,
  val lastOptimizedTimeMillis: Long = 0L,
  val currentFps: Int = 120,
  val currentRefreshRate: Int = 120,
  val batteryTempCelsius: Float = 36.8f
)
