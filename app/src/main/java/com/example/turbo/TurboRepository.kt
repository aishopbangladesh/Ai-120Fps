package com.example.turbo

import android.content.Context
import com.example.R
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

class TurboRepository(
  private val db: FirebaseFirestore
) {
  constructor(context: Context) : this(
    FirebaseFirestore.getInstance(
      context.applicationContext.getString(R.string.firestore_database_id)
    )
  )

  suspend fun saveUserProfile(
    userId: String,
    email: String,
    displayName: String,
    photoUrl: String
  ) {
    val data = mapOf(
      "userId" to userId,
      "email" to email,
      "displayName" to displayName,
      "photoUrl" to photoUrl,
      "updatedAt" to FieldValue.serverTimestamp()
    )
    db.collection("users").document(userId).set(data, SetOptions.merge()).await()
  }

  suspend fun saveTurboProfile(
    userId: String,
    turboActive: Boolean,
    forcePeakRefresh: Boolean,
    gameFocus: Boolean,
    syncThrottling: Boolean,
    autoRamClean: Boolean,
    lastRamCleanMb: Double
  ) {
    val data = mapOf(
      "userId" to userId,
      "turboActive" to turboActive,
      "forcePeakRefreshRate" to forcePeakRefresh,
      "gameFocusPriority" to gameFocus,
      "backgroundSyncThrottling" to syncThrottling,
      "autoRamCleanOnLaunch" to autoRamClean,
      "lastRamCleanMb" to lastRamCleanMb,
      "updatedAt" to FieldValue.serverTimestamp()
    )
    db.collection("users").document(userId)
      .collection("turboProfiles").document("default")
      .set(data, SetOptions.merge())
      .await()
  }

  fun observeTurboProfile(userId: String): Flow<GameTurboProfileEntity?> {
    return db.collection("users").document(userId)
      .collection("turboProfiles").document("default")
      .snapshots()
      .map { snapshot ->
        if (snapshot.exists()) {
          snapshot.toObject(GameTurboProfileEntity::class.java)
        } else null
      }
  }
}
