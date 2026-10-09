package com.example.data.repository

import com.example.base.FirestoreEmulatorTestBase
import com.example.data.model.FirestoreUser
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test

class TopZynRepositoryRuleTest : FirestoreEmulatorTestBase() {

  @Test
  fun createOrUpdateUser_authenticatedOwner_persistsSuccessfully(): Unit = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val repository = TopZynFirestoreRepository(firestore, auth)

    val user = FirestoreUser(
      userId = uid,
      fullName = "Alice Tester",
      email = ALICE_EMAIL,
      balanceGds = 1250.0
    )

    val createResult = withTimeout(DEFAULT_TIMEOUT_MS) { repository.createOrUpdateUser(user) }
    assertTrue(createResult.isSuccess)

    val observed = withTimeout(FLOW_TIMEOUT_MS) {
      repository.observeUser(uid).first { u: FirestoreUser? -> u != null && u.fullName == "Alice Tester" }
    }
    assertNotNull(observed)
    assertEquals(1250.0, observed!!.balanceGds, 0.01)
  }

  @Test
  fun recordDeposit_authenticatedOwner_incrementsBalanceAndLogsTx(): Unit = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val repository = TopZynFirestoreRepository(firestore, auth)

    val user = FirestoreUser(
      userId = uid,
      fullName = "Alice Tester",
      balanceGds = 500.0
    )
    withTimeout(DEFAULT_TIMEOUT_MS) { repository.createOrUpdateUser(user) }

    val depositResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.recordDeposit(amountGds = 250.0, method = "MonCash", reference = "REF-8899")
    }
    if (depositResult.isFailure) {
      throw AssertionError("Deposit failed with: " + depositResult.exceptionOrNull()?.message, depositResult.exceptionOrNull())
    }
    assertTrue(depositResult.isSuccess)

    val updatedUser = withTimeout(FLOW_TIMEOUT_MS) {
      repository.observeUser(uid).first { u: FirestoreUser? -> u != null && u.balanceGds == 750.0 }
    }
    assertNotNull(updatedUser)
    assertEquals(750.0, updatedUser!!.balanceGds, 0.01)

    val txList = withTimeout(FLOW_TIMEOUT_MS) {
      repository.observeTransactions(uid).first { list -> list.any { it.method == "MonCash" } }
    }
    assertTrue(txList.isNotEmpty())
  }

  @Test
  fun recordSendTransfer_insufficientBalance_fails(): Unit = runBlocking {
    val uid = signInTestUser(ALICE_EMAIL)
    val repository = TopZynFirestoreRepository(firestore, auth)

    val user = FirestoreUser(
      userId = uid,
      fullName = "Alice Tester",
      balanceGds = 50.0
    )
    withTimeout(DEFAULT_TIMEOUT_MS) { repository.createOrUpdateUser(user) }

    val sendResult = withTimeout(DEFAULT_TIMEOUT_MS) {
      repository.recordSendTransfer(recipient = "+50931223344", amountGds = 500.0, note = "Cadeau")
    }
    assertTrue(sendResult.isFailure)
  }

  @Test
  fun crossUserDocumentAccess_rejectedByRules(): Unit = runBlocking {
    val aliceUid = signInTestUser(ALICE_EMAIL)
    val aliceRepo = TopZynFirestoreRepository(firestore, auth)
    val user = FirestoreUser(
      userId = aliceUid,
      fullName = "Alice Tester",
      balanceGds = 1000.0
    )
    withTimeout(DEFAULT_TIMEOUT_MS) { aliceRepo.createOrUpdateUser(user) }

    signInTestUser(BOB_EMAIL)
    // Bob attempts to directly read Alice's user document
    try {
      withTimeout(DEFAULT_TIMEOUT_MS) {
        firestore.collection("users").document(aliceUid).get().await()
      }
      fail("Expected FirebaseFirestoreException PERMISSION_DENIED")
    } catch (e: Exception) {
      val isDenied = e is FirebaseFirestoreException && e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED
      val isCauseDenied = e.cause is FirebaseFirestoreException && (e.cause as FirebaseFirestoreException).code == FirebaseFirestoreException.Code.PERMISSION_DENIED
      assertTrue(isDenied || isCauseDenied)
    }
  }

  private companion object {
    const val ALICE_EMAIL = "alice@topzyn.test"
    const val BOB_EMAIL = "bob@topzyn.test"
    const val DEFAULT_TIMEOUT_MS = 5000L
    const val FLOW_TIMEOUT_MS = 3000L
  }
}
