package com.example.turbo

import androidx.test.core.app.ApplicationProvider
import com.example.base.FirestoreEmulatorTestBase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class TurboRepositoryRuleTest : FirestoreEmulatorTestBase() {

  @Test
  fun testSaveAndReadTurboProfile() = runBlocking {
    val email = "turbo_gamer@example.com"
    val uid = signInTestUser(email)
    val repository = TurboRepository(firestore)

    // Save profile
    repository.saveUserProfile(
      userId = uid,
      email = email,
      displayName = "Ai Pro",
      photoUrl = ""
    )

    repository.saveTurboProfile(
      userId = uid,
      turboActive = true,
      forcePeakRefresh = true,
      gameFocus = true,
      syncThrottling = true,
      autoRamClean = true,
      lastRamCleanMb = 480.0
    )

    // Observe saved profile
    val profile = repository.observeTurboProfile(uid).first()
    assertNotNull(profile)
    assertEquals(uid, profile?.userId)
    assertTrue(profile?.turboActive == true)
    assertEquals(480.0, profile?.lastRamCleanMb ?: 0.0, 0.01)
  }
}
