package com.example.turbo

import com.google.firebase.Timestamp

data class UserProfileEntity(
  val userId: String = "",
  val email: String = "",
  val displayName: String = "",
  val photoUrl: String = "",
  val createdAt: Timestamp? = null,
  val updatedAt: Timestamp? = null
)

data class GameTurboProfileEntity(
  val userId: String = "",
  val turboActive: Boolean = false,
  val forcePeakRefreshRate: Boolean = true,
  val gameFocusPriority: Boolean = true,
  val backgroundSyncThrottling: Boolean = true,
  val autoRamCleanOnLaunch: Boolean = true,
  val lastRamCleanMb: Double = 0.0,
  val updatedAt: Timestamp? = null
)
