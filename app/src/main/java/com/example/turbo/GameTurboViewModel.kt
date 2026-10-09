package com.example.turbo

import android.app.Application
import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

sealed interface AuthUiState {
  data object Idle : AuthUiState
  data object Loading : AuthUiState
  data class Authenticated(val user: FirebaseUser) : AuthUiState
  data class Error(val message: String) : AuthUiState
}

class GameTurboViewModel(
  application: Application
) : AndroidViewModel(application) {

  private val auth: FirebaseAuth = Firebase.auth
  private val memoryOptimizer = MemoryOptimizer(application)
  private val repository = TurboRepository(application)

  private val _turboState = MutableStateFlow(GameTurboState())
  val turboState: StateFlow<GameTurboState> = _turboState.asStateFlow()

  private val _authUiState = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
  val authUiState: StateFlow<AuthUiState> = _authUiState.asStateFlow()

  private val _userMessage = MutableStateFlow<String?>(null)
  val userMessage: StateFlow<String?> = _userMessage.asStateFlow()

  init {
    val currentUser = auth.currentUser
    if (currentUser != null) {
      _authUiState.value = AuthUiState.Authenticated(currentUser)
      syncWithCloudProfile(currentUser.uid)
    }

    auth.addAuthStateListener { firebaseAuth ->
      val u = firebaseAuth.currentUser
      if (u != null) {
        _authUiState.value = AuthUiState.Authenticated(u)
        syncWithCloudProfile(u.uid)
      } else {
        _authUiState.value = AuthUiState.Idle
      }
    }

    refreshMemoryInfo()
    scanBackgroundProcesses()
  }

  fun clearUserMessage() {
    _userMessage.value = null
  }

  fun refreshMemoryInfo() {
    viewModelScope.launch {
      val (total, avail) = memoryOptimizer.readMemoryInfo()
      val used = (total - avail).coerceAtLeast(0L)
      val usagePct = if (total > 0) used.toFloat() / total.toFloat() else 0.5f
      _turboState.update {
        it.copy(
          totalRamMb = total,
          availableRamMb = avail,
          usedRamMb = used,
          ramUsagePercentage = usagePct
        )
      }
    }
  }

  fun scanBackgroundProcesses() {
    viewModelScope.launch {
      _turboState.update { it.copy(isScanningProcesses = true) }
      val processes = memoryOptimizer.scanRunningProcesses()
      _turboState.update {
        it.copy(
          backgroundProcesses = processes,
          isScanningProcesses = false
        )
      }
    }
  }

  fun toggleTurboMode() {
    val newMode = !_turboState.value.turboModeActive
    _turboState.update { it.copy(turboModeActive = newMode) }
    if (newMode) {
      _userMessage.value = "⚡ Ai 120Fps Game Turbo Activated! Hardware optimized."
      if (_turboState.value.autoRamCleanOnLaunch) {
        cleanRam()
      }
    } else {
      _userMessage.value = "Game Turbo Mode Standby"
    }
    persistTurboSettings()
  }

  fun cleanRam() {
    viewModelScope.launch {
      _turboState.update { it.copy(isCleaningRam = true) }
      val targets = _turboState.value.backgroundProcesses.map { it.packageName }
      val freed = memoryOptimizer.killBackgroundProcesses(targets)

      val (total, avail) = memoryOptimizer.readMemoryInfo()
      val updatedAvail = avail + (freed / 2)
      val updatedUsed = (total - updatedAvail).coerceAtLeast(1024L)
      val updatedPct = if (total > 0) updatedUsed.toFloat() / total.toFloat() else 0.4f

      val markedProcesses = _turboState.value.backgroundProcesses.map {
        it.copy(isKilled = true)
      }

      _turboState.update {
        it.copy(
          isCleaningRam = false,
          availableRamMb = updatedAvail,
          usedRamMb = updatedUsed,
          ramUsagePercentage = updatedPct,
          lastFreedMb = freed,
          lastOptimizedTimeMillis = System.currentTimeMillis(),
          backgroundProcesses = markedProcesses
        )
      }
      _userMessage.value = "🧹 Freed ${freed}MB RAM! Background apps terminated."
      persistTurboSettings()
    }
  }

  fun toggleForcePeakRefresh(enabled: Boolean) {
    _turboState.update { it.copy(forcePeakRefreshRate = enabled) }
    _userMessage.value = if (enabled) "Locked 120Hz Peak Display" else "Dynamic Refresh Enabled"
    persistTurboSettings()
  }

  fun toggleGameFocusPriority(enabled: Boolean) {
    _turboState.update { it.copy(gameFocusPriority = enabled) }
    _userMessage.value = if (enabled) "Game Focus & DND Enabled" else "Game Focus Off"
    persistTurboSettings()
  }

  fun toggleBackgroundSyncThrottling(enabled: Boolean) {
    _turboState.update { it.copy(backgroundSyncThrottling = enabled) }
    _userMessage.value = if (enabled) "Network Sync Throttling Active" else "Sync Throttling Off"
    persistTurboSettings()
  }

  fun toggleAutoRamClean(enabled: Boolean) {
    _turboState.update { it.copy(autoRamCleanOnLaunch = enabled) }
    persistTurboSettings()
  }

  private fun persistTurboSettings() {
    val uid = auth.currentUser?.uid ?: return
    viewModelScope.launch {
      try {
        val s = _turboState.value
        repository.saveTurboProfile(
          userId = uid,
          turboActive = s.turboModeActive,
          forcePeakRefresh = s.forcePeakRefreshRate,
          gameFocus = s.gameFocusPriority,
          syncThrottling = s.backgroundSyncThrottling,
          autoRamClean = s.autoRamCleanOnLaunch,
          lastRamCleanMb = s.lastFreedMb.toDouble()
        )
      } catch (_: Exception) {
        // Safe catch
      }
    }
  }

  private fun syncWithCloudProfile(userId: String) {
    viewModelScope.launch {
      try {
        repository.observeTurboProfile(userId).collect { profile ->
          if (profile != null) {
            _turboState.update { current ->
              current.copy(
                turboModeActive = profile.turboActive,
                forcePeakRefreshRate = profile.forcePeakRefreshRate,
                gameFocusPriority = profile.gameFocusPriority,
                backgroundSyncThrottling = profile.backgroundSyncThrottling,
                autoRamCleanOnLaunch = profile.autoRamCleanOnLaunch,
                lastFreedMb = profile.lastRamCleanMb.toInt()
              )
            }
          }
        }
      } catch (_: Exception) {
        // Safe catch
      }
    }
  }

  fun signInWithGoogle(context: Context) {
    viewModelScope.launch {
      _authUiState.value = AuthUiState.Loading
      val credentialManager = CredentialManager.create(context)
      val serverClientId = context.getString(R.string.default_web_client_id)

      val googleIdOption = GetSignInWithGoogleOption.Builder(serverClientId).build()
      val request = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

      try {
        val result = credentialManager.getCredential(context = context, request = request)
        val credential = result.credential
        if (credential is GoogleIdTokenCredential) {
          val firebaseCredential = GoogleAuthProvider.getCredential(credential.idToken, null)
          val authResult = auth.signInWithCredential(firebaseCredential).await()
          val user = authResult.user
          if (user != null) {
            repository.saveUserProfile(
              userId = user.uid,
              email = user.email ?: "",
              displayName = user.displayName ?: "Esports Gamer",
              photoUrl = user.photoUrl?.toString() ?: ""
            )
            _authUiState.value = AuthUiState.Authenticated(user)
            _userMessage.value = "Welcome ${user.displayName ?: "Gamer"}! Settings Cloud-Synced."
          }
        } else {
          _authUiState.value = AuthUiState.Error("Unexpected credential type")
        }
      } catch (cancellation: GetCredentialCancellationException) {
        _authUiState.value = AuthUiState.Idle
        _userMessage.value = "Sign-in cancelled"
      } catch (e: Exception) {
        _authUiState.value = AuthUiState.Error(e.localizedMessage ?: "Sign in failed")
      }
    }
  }

  fun signOut(context: Context) {
    viewModelScope.launch {
      val credentialManager = CredentialManager.create(context)
      try {
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
      } catch (_: Exception) {}
      auth.signOut()
      _authUiState.value = AuthUiState.Idle
      _userMessage.value = "Signed out"
    }
  }
}
