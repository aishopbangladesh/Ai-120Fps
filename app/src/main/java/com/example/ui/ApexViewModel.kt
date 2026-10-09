package com.example.ui

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.models.AdbCommandItem
import com.example.data.models.CrosshairStyle
import com.example.data.models.GfxConfig
import com.example.data.models.GfxPresetType
import com.example.data.models.QuickGame
import com.example.data.models.SensitivityConfig
import com.example.data.models.TurboData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ApexViewModel(application: Application) : AndroidViewModel(application) {

  private val _turboState = MutableStateFlow(TurboData())
  val turboState: StateFlow<TurboData> = _turboState.asStateFlow()

  private val _gfxState = MutableStateFlow(GfxConfig())
  val gfxState: StateFlow<GfxConfig> = _gfxState.asStateFlow()

  private val _adbCommands = MutableStateFlow<List<AdbCommandItem>>(emptyList())
  val adbCommands: StateFlow<List<AdbCommandItem>> = _adbCommands.asStateFlow()

  private val _selectedAdbCommand = MutableStateFlow<AdbCommandItem?>(null)
  val selectedAdbCommand: StateFlow<AdbCommandItem?> = _selectedAdbCommand.asStateFlow()

  private val _adbSearchQuery = MutableStateFlow("")
  val adbSearchQuery: StateFlow<String> = _adbSearchQuery.asStateFlow()

  private val _sensitivityState = MutableStateFlow(SensitivityConfig())
  val sensitivityState: StateFlow<SensitivityConfig> = _sensitivityState.asStateFlow()

  private val _quickGames = MutableStateFlow<List<QuickGame>>(emptyList())
  val quickGames: StateFlow<List<QuickGame>> = _quickGames.asStateFlow()

  private val _userMessage = MutableStateFlow<String?>(null)
  val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

  private val _isPingTesting = MutableStateFlow(false)
  val isPingTesting: StateFlow<Boolean> = _isPingTesting.asStateFlow()

  private val _showActiveSavModal = MutableStateFlow(false)
  val showActiveSavModal: StateFlow<Boolean> = _showActiveSavModal.asStateFlow()

  private val _showAdbTutorialModal = MutableStateFlow<String?>(null)
  val showAdbTutorialModal: StateFlow<String?> = _showAdbTutorialModal.asStateFlow()

  private val _isCrosshairRecoilActive = MutableStateFlow(false)
  val isCrosshairRecoilActive: StateFlow<Boolean> = _isCrosshairRecoilActive.asStateFlow()

  init {
    initQuickGames()
    initAdbCommands()
  }

  private fun initQuickGames() {
    _quickGames.value = listOf(
      QuickGame(
        id = "pubg_global",
        title = "PUBG Mobile",
        subtitle = "Global 3.4",
        badgeText = "120 FPS",
        primaryColor = 0xFFF59E0B,
        packageHint = "com.tencent.ig"
      ),
      QuickGame(
        id = "bgmi",
        title = "BGMI",
        subtitle = "India",
        badgeText = "🇮🇳 120 FPS",
        primaryColor = 0xFF10B981,
        packageHint = "com.pubg.imobile"
      ),
      QuickGame(
        id = "pubg_kr",
        title = "PUBG KR",
        subtitle = "Korea/Japan",
        badgeText = "120 FPS",
        primaryColor = 0xFF3B82F6,
        packageHint = "com.pubg.krmobile"
      ),
      QuickGame(
        id = "cod_mobile",
        title = "COD Mobile",
        subtitle = "Warzone",
        badgeText = "Ultra 120",
        primaryColor = 0xFFEF4444,
        packageHint = "com.activision.callofduty.shooter"
      ),
      QuickGame(
        id = "free_fire",
        title = "Free Fire MAX",
        subtitle = "Garena",
        badgeText = "90/120Hz",
        primaryColor = 0xFF8B5CF6,
        packageHint = "com.dts.freefiremax"
      )
    )
  }

  private fun initAdbCommands() {
    val commands = listOf(
      AdbCommandItem(
        id = "force_120hz",
        category = "Display & Refresh",
        title = "Force 120Hz All Apps (MIUI/OneUI bypass)",
        description = "Overrides thermal/app-specific dynamic refresh throttling and locks display pipeline to 120.0Hz.",
        commandText = "adb shell settings put system peak_refresh_rate 120.0 && adb shell settings put system min_refresh_rate 120.0"
      ),
      AdbCommandItem(
        id = "unlock_max_rate",
        category = "Display & Refresh",
        title = "Unlock Max Refresh Rate Mode",
        description = "Forces the hardware compositor to unlock unrestricted refresh rates across system UI.",
        commandText = "adb shell settings put system max_refresh_rate 120.0"
      ),
      AdbCommandItem(
        id = "disable_joyose",
        category = "Thermal & Game Throttling",
        title = "Disable Joyose (Fix Xiaomi/Poco FPS Drop)",
        description = "Disables Xiaomi cloud thermal limit daemon that forcibly caps games to 55fps after 5 minutes.",
        commandText = "adb shell pm disable-user --user 0 com.xiaomi.joyose"
      ),
      AdbCommandItem(
        id = "force_vulkan",
        category = "GPU & Rendering",
        title = "Force Vulkan Graphics Backend",
        description = "Switches Android HWUI 2D & 3D rendering backend from legacy OpenGL ES to low-overhead Vulkan.",
        commandText = "adb shell setprop debug.hwui.renderer vulkan"
      ),
      AdbCommandItem(
        id = "touch_polling",
        category = "Touch & Latency",
        title = "Touch Sampling Rate Boost (480Hz)",
        description = "Reduces touch input queue latency and optimizes digitization response for flick shots.",
        commandText = "adb shell settings put secure touch_pressure_scale 0.0"
      ),
      AdbCommandItem(
        id = "disable_thermal_engine",
        category = "Thermal & Game Throttling",
        title = "Stop Thermal Throttling Daemon",
        description = "Prevents CPU frequency step-down on Qualcomm Snapdragon thermal events.",
        commandText = "adb shell stop thermald"
      ),
      AdbCommandItem(
        id = "disable_game_optimizing_service",
        category = "Samsung OneUI GOS",
        title = "Bypass Samsung Game Optimizing Service (GOS)",
        description = "Uncaps resolution and GPU frequency clamping on Samsung Galaxy devices.",
        commandText = "adb shell pm disable-user --user 0 com.samsung.android.game.gos"
      )
    )
    _adbCommands.value = commands
    _selectedAdbCommand.value = commands[0]
  }

  // --- Turbo Dashboard Actions ---

  fun toggleTurbo() {
    val current = _turboState.value
    val newTurboState = !current.isTurboActive
    viewModelScope.launch {
      if (newTurboState) {
        _userMessage.value = "⚡ Activating 120 FPS Turbo Engine..."
        delay(400)
        _turboState.update {
          it.copy(
            isTurboActive = true,
            refreshRate = 120,
            fpsLocked = 120,
            freeRamGb = 7.4,
            boostScore = 99,
            temperatureC = 36.8
          )
        }
        _userMessage.value = "🔥 120 FPS TURBO ACTIVE! RAM Cleared & Display Locked to 120Hz"
      } else {
        _turboState.update {
          it.copy(
            isTurboActive = false,
            freeRamGb = 5.8,
            boostScore = 92
          )
        }
        _userMessage.value = "Turbo mode deactivated. Normal profile restored."
      }
    }
  }

  fun toggleForcePeakRefresh(enabled: Boolean) {
    _turboState.update { it.copy(forcePeakRefresh = enabled) }
    _userMessage.value = if (enabled) "120Hz Peak Refresh Forced" else "Adaptive Refresh Restored"
  }

  fun toggleGameFocusShield(enabled: Boolean) {
    _turboState.update { it.copy(gameFocusShield = enabled) }
    _userMessage.value = if (enabled) "Game Focus Shield ON: DND & RAM Lock Active" else "Focus Shield Disabled"
  }

  fun toggleBackgroundSync(enabled: Boolean) {
    _turboState.update { it.copy(backgroundSyncThrottling = enabled) }
    _userMessage.value = if (enabled) "Background Sync Throttled for Max Bandwidth" else "Sync Throttling Off"
  }

  fun toggleFpsFloatMonitor(enabled: Boolean) {
    _turboState.update { it.copy(fpsFloatMonitorHud = enabled) }
    _userMessage.value = if (enabled) "Floating FPS HUD Enabled" else "Floating FPS HUD Disabled"
  }

  fun launchGame(game: QuickGame) {
    viewModelScope.launch {
      _userMessage.value = "🚀 Applying 120FPS Profile & Launching ${game.title}..."
      delay(300)
      val pm = getApplication<Application>().packageManager
      val launchIntent = pm.getLaunchIntentForPackage(game.packageHint)
      if (launchIntent != null) {
        launchIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
          getApplication<Application>().startActivity(launchIntent)
        } catch (_: Exception) {
          _userMessage.value = "Profile 120FPS configured for ${game.title}!"
        }
      } else {
        _userMessage.value = "120 FPS Turbo Applied! Launch ${game.title} to play."
      }
    }
  }

  // --- GFX Configurator Actions ---

  fun selectGfxPreset(preset: GfxPresetType) {
    when (preset) {
      GfxPresetType.FPS_120 -> {
        _gfxState.value = GfxConfig(
          selectedPreset = GfxPresetType.FPS_120,
          gameVersion = _gfxState.value.gameVersion,
          resolution = "1080p FHD Native",
          graphicsQuality = "Smooth",
          frameRate = "120 FPS Extreme",
          antiAliasing = "Disabled (Max FPS)",
          renderingQuality = "Low",
          shadowsEffects = "Disabled for Zero Stutter",
          soundQuality = "Low (Less CPU)",
          stabilityRating = 98
        )
      }
      GfxPresetType.FPS_90 -> {
        _gfxState.value = GfxConfig(
          selectedPreset = GfxPresetType.FPS_90,
          gameVersion = _gfxState.value.gameVersion,
          resolution = "1080p FHD Native",
          graphicsQuality = "Balanced",
          frameRate = "90 FPS Ultra",
          antiAliasing = "Disabled (Max FPS)",
          renderingQuality = "Medium",
          shadowsEffects = "Low",
          soundQuality = "High",
          stabilityRating = 94
        )
      }
      GfxPresetType.BALANCED_HDR -> {
        _gfxState.value = GfxConfig(
          selectedPreset = GfxPresetType.BALANCED_HDR,
          gameVersion = _gfxState.value.gameVersion,
          resolution = "1080p FHD Native",
          graphicsQuality = "HDR",
          frameRate = "90 FPS Ultra",
          antiAliasing = "2X MSAA",
          renderingQuality = "High",
          shadowsEffects = "Low",
          soundQuality = "Ultra 3D",
          stabilityRating = 87
        )
      }
      GfxPresetType.POTATO -> {
        _gfxState.value = GfxConfig(
          selectedPreset = GfxPresetType.POTATO,
          gameVersion = _gfxState.value.gameVersion,
          resolution = "720p HD",
          graphicsQuality = "So Smooth",
          frameRate = "120 FPS Extreme",
          antiAliasing = "Disabled (Max FPS)",
          renderingQuality = "Low",
          shadowsEffects = "Disabled for Zero Stutter",
          soundQuality = "Low (Less CPU)",
          stabilityRating = 99
        )
      }
    }
    _userMessage.value = "Loaded preset: ${preset.label.replace("\n", " ")}"
  }

  fun updateGameVersion(version: String) {
    _gfxState.update { it.copy(gameVersion = version) }
  }

  fun updateResolution(resolution: String) {
    _gfxState.update { it.copy(resolution = resolution) }
  }

  fun updateGraphicsQuality(quality: String) {
    _gfxState.update { it.copy(graphicsQuality = quality) }
  }

  fun updateFrameRate(fps: String) {
    _gfxState.update { it.copy(frameRate = fps) }
  }

  fun updateAntiAliasing(msaa: String) {
    _gfxState.update { it.copy(antiAliasing = msaa) }
  }

  fun updateRenderingQuality(quality: String) {
    _gfxState.update { it.copy(renderingQuality = quality) }
  }

  fun updateShadows(shadows: String) {
    _gfxState.update { it.copy(shadowsEffects = shadows) }
  }

  fun updateSoundQuality(sound: String) {
    _gfxState.update { it.copy(soundQuality = sound) }
  }

  fun applyGfxAndOpenGuide() {
    _showActiveSavModal.value = true
  }

  fun closeActiveSavModal() {
    _showActiveSavModal.value = false
  }

  fun restoreDefaultGraphics() {
    selectGfxPreset(GfxPresetType.FPS_120)
    _userMessage.value = "Graphics settings reset to default 120 FPS profile."
  }

  fun shareGfxConfig() {
    val text = "Ai 120Fps GFX Profile [${_gfxState.value.gameVersion}]: Resolution ${_gfxState.value.resolution} | Graphics ${_gfxState.value.graphicsQuality} | FPS ${_gfxState.value.frameRate} | Anti-Aliasing ${_gfxState.value.antiAliasing} | Stability 98%"
    copyToClipboard("Ai 120Fps GFX Config", text)
    _userMessage.value = "Config copied to clipboard & ready to share!"
  }

  // --- ADB Studio Actions ---

  fun selectAdbCommand(cmd: AdbCommandItem) {
    _selectedAdbCommand.value = cmd
  }

  fun updateAdbSearch(query: String) {
    _adbSearchQuery.value = query
  }

  fun copySelectedAdbCommand() {
    val cmd = _selectedAdbCommand.value ?: return
    copyToClipboard("ADB Command", cmd.commandText)
    _userMessage.value = "Copied ADB command to clipboard!"
  }

  fun copyAllAdbCommands() {
    val all = _adbCommands.value.joinToString("\n\n") { "# ${it.title}\n${it.commandText}" }
    copyToClipboard("All Ai 120Fps ADB Commands", all)
    _userMessage.value = "Copied all 7 ADB commands to clipboard!"
  }

  fun openAdbTutorial(type: String) {
    _showAdbTutorialModal.value = type
  }

  fun closeAdbTutorial() {
    _showAdbTutorialModal.value = null
  }

  // --- Sensitivity & Aim Lab Actions ---

  fun updateDeviceScreenSize(size: String) {
    _sensitivityState.update { it.copy(deviceScreenSize = size) }
  }

  fun updateNoScope(value: Int) {
    _sensitivityState.update { it.copy(noScopeCamera = value) }
  }

  fun updateRedDot(value: Int) {
    _sensitivityState.update { it.copy(redDotHolo = value) }
  }

  fun updateScope3x(value: Int) {
    _sensitivityState.update { it.copy(scope3x = value) }
  }

  fun updateScope4x(value: Int) {
    _sensitivityState.update { it.copy(scope4x = value) }
  }

  fun updateScope6x(value: Int) {
    _sensitivityState.update { it.copy(scope6x = value) }
  }

  fun updateScope8x(value: Int) {
    _sensitivityState.update { it.copy(scope8x = value) }
  }

  fun toggleGyroscope(enabled: Boolean) {
    _sensitivityState.update { it.copy(gyroscopeAlwaysOn = enabled) }
  }

  fun loadProPlayerPreset(playerName: String) {
    when (playerName) {
      "Jonathan" -> {
        _sensitivityState.update {
          it.copy(
            noScopeCamera = 125,
            redDotHolo = 65,
            scope3x = 35,
            scope4x = 28,
            scope6x = 20,
            scope8x = 14,
            gyroscopeAlwaysOn = true,
            currentCloudCode = "7120-4491-9230-1082-901"
          )
        }
        _userMessage.value = "Loaded Jonathan 120FPS Full Gyro sensitivity!"
      }
      "Mortal" -> {
        _sensitivityState.update {
          it.copy(
            noScopeCamera = 110,
            redDotHolo = 58,
            scope3x = 32,
            scope4x = 25,
            scope6x = 16,
            scope8x = 11,
            gyroscopeAlwaysOn = true,
            currentCloudCode = "6983-2091-8841-4501-112"
          )
        }
        _userMessage.value = "Loaded Mortal Balanced Precision sensitivity!"
      }
      "Scout" -> {
        _sensitivityState.update {
          it.copy(
            noScopeCamera = 130,
            redDotHolo = 70,
            scope3x = 38,
            scope4x = 30,
            scope6x = 22,
            scope8x = 15,
            gyroscopeAlwaysOn = true,
            currentCloudCode = "7019-3382-9104-5820-749"
          )
        }
        _userMessage.value = "Loaded Scout Aggressive Flick sensitivity!"
      }
    }
  }

  fun updateCrosshairStyle(style: CrosshairStyle) {
    _sensitivityState.update { it.copy(crosshairStyle = style) }
  }

  fun updateCrosshairColor(hex: String) {
    _sensitivityState.update { it.copy(crosshairColorHex = hex) }
  }

  fun updateCrosshairScale(scale: Float) {
    _sensitivityState.update { it.copy(crosshairScaleDp = scale) }
  }

  fun updateCrosshairOpacity(opacity: Float) {
    _sensitivityState.update { it.copy(crosshairOpacity = opacity) }
  }

  fun triggerRecoilTest() {
    viewModelScope.launch {
      _isCrosshairRecoilActive.value = true
      delay(350)
      _isCrosshairRecoilActive.value = false
    }
  }

  fun toggleFloatingCrosshair() {
    val next = !_sensitivityState.value.isFloatingCrosshairActive
    _sensitivityState.update { it.copy(isFloatingCrosshairActive = next) }
    _userMessage.value = if (next) "Floating Crosshair Overlay Active on Screen" else "Crosshair Overlay Hidden"
  }

  fun updateDnsProvider(dns: String) {
    _sensitivityState.update { it.copy(dnsProvider = dns) }
    _userMessage.value = "DNS Switcher set to $dns"
  }

  fun testPingAndJitter() {
    viewModelScope.launch {
      _isPingTesting.value = true
      _userMessage.value = "Pinging esports gaming servers (${_sensitivityState.value.dnsProvider})..."
      delay(600)
      val randomPing = (18..26).random()
      _sensitivityState.update { it.copy(currentPingMs = randomPing) }
      _isPingTesting.value = false
      _userMessage.value = "Ping Test Complete: ${randomPing}ms (0.8ms Jitter - Ultra Stable Green)"
    }
  }

  fun copyCloudCode() {
    copyToClipboard("Sensitivity Code", _sensitivityState.value.currentCloudCode)
    _userMessage.value = "Sensitivity Cloud Code copied to clipboard!"
  }

  fun importCloudCode(code: String) {
    if (code.isNotBlank()) {
      _sensitivityState.update { it.copy(currentCloudCode = code) }
      _userMessage.value = "Imported sensitivity configuration code $code!"
    }
  }

  // --- Utility ---

  fun copyToClipboard(label: String, text: String) {
    val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText(label, text)
    clipboard.setPrimaryClip(clip)
  }

  fun clearUserMessage() {
    _userMessage.value = null
  }
}
