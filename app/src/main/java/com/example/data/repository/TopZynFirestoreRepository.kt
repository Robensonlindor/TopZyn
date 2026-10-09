package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.FirestoreNotification
import com.example.data.model.FirestoreOrder
import com.example.data.model.FirestoreTransaction
import com.example.data.model.FirestoreUser
import com.example.util.OperationType
import com.example.util.handleFirestoreError
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import java.util.UUID

class TopZynFirestoreRepository(
    val db: FirebaseFirestore,
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    // Secondary constructor that resolves the named database ID from string resources
    constructor(context: Context) : this(
        FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        ),
        FirebaseAuth.getInstance()
    )

    fun requireUserId(): String {
        return auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    fun observeUser(userId: String): Flow<FirestoreUser?> = callbackFlow {
        val docRef = db.collection("users").document(userId)
        val registration = docRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                handleFirestoreError(error, OperationType.GET, docRef.path)
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val user = snapshot.toObject(FirestoreUser::class.java)
                trySend(user)
            } else {
                trySend(null)
            }
        }
        awaitClose { registration.remove() }
    }.catch { error ->
        if (error is Exception) {
            handleFirestoreError(error, OperationType.GET, "users/$userId")
        }
        emit(null)
    }

    fun observeTransactions(userId: String): Flow<List<FirestoreTransaction>> = callbackFlow {
        val colRef = db.collection("users").document(userId).collection("transactions")
        val registration = colRef.orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    handleFirestoreError(error, OperationType.LIST, colRef.path)
                    close(error)
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { it.toObject(FirestoreTransaction::class.java) } ?: emptyList()
                trySend(list)
            }
        awaitClose { registration.remove() }
    }.catch { error ->
        if (error is Exception) {
            handleFirestoreError(error, OperationType.LIST, "users/$userId/transactions")
        }
        emit(emptyList())
    }

    fun observeOrders(userId: String): Flow<List<FirestoreOrder>> = callbackFlow {
        val colRef = db.collection("users").document(userId).collection("orders")
        val registration = colRef.orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    handleFirestoreError(error, OperationType.LIST, colRef.path)
                    close(error)
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { it.toObject(FirestoreOrder::class.java) } ?: emptyList()
                trySend(list)
            }
        awaitClose { registration.remove() }
    }.catch { error ->
        if (error is Exception) {
            handleFirestoreError(error, OperationType.LIST, "users/$userId/orders")
        }
        emit(emptyList())
    }

    suspend fun createOrUpdateUser(user: FirestoreUser): Result<Unit> {
        val uid = requireUserId()
        val userRef = db.collection("users").document(uid)
        return try {
            val payload = hashMapOf<String, Any>(
                "userId" to uid,
                "fullName" to user.fullName,
                "email" to user.email,
                "balanceGds" to user.balanceGds,
                "currency" to user.currency,
                "isTransferEnabled" to user.isTransferEnabled,
                "phoneNumber" to user.phoneNumber,
                "updatedAt" to FieldValue.serverTimestamp()
            )
            val doc = userRef.get().await()
            if (!doc.exists()) {
                payload["createdAt"] = FieldValue.serverTimestamp()
            }
            userRef.set(payload, com.google.firebase.firestore.SetOptions.merge()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, userRef.path)
            Result.failure(e)
        }
    }

    suspend fun recordDeposit(
        amountGds: Double,
        method: String,
        reference: String
    ): Result<FirestoreTransaction> {
        val uid = requireUserId()
        val userRef = db.collection("users").document(uid)
        val txId = UUID.randomUUID().toString()
        val txRef = userRef.collection("transactions").document(txId)

        return try {
            db.runTransaction { transaction ->
                val userDoc = transaction.get(userRef)
                val currentBalance = userDoc.getDouble("balanceGds") ?: 0.0
                val newBalance = currentBalance + amountGds

                transaction.update(userRef, mapOf(
                    "balanceGds" to newBalance,
                    "updatedAt" to FieldValue.serverTimestamp()
                ))

                val txData = hashMapOf(
                    "id" to txId,
                    "userId" to uid,
                    "type" to "DEPOSIT",
                    "amountGds" to amountGds,
                    "feeGds" to 0.0,
                    "recipient" to "TopZyn Wallet",
                    "method" to method,
                    "status" to "SUCCESS",
                    "description" to "Dépôt via $method (Réf: $reference)",
                    "createdAt" to FieldValue.serverTimestamp()
                )
                transaction.set(txRef, txData)
            }.await()

            Result.success(
                FirestoreTransaction(
                    id = txId,
                    userId = uid,
                    type = "DEPOSIT",
                    amountGds = amountGds,
                    feeGds = 0.0,
                    recipient = "TopZyn Wallet",
                    method = method,
                    status = "SUCCESS",
                    description = "Dépôt via $method (Réf: $reference)"
                )
            )
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, txRef.path)
            Result.failure(e)
        }
    }

    suspend fun recordSendTransfer(
        recipient: String,
        amountGds: Double,
        note: String
    ): Result<FirestoreTransaction> {
        val uid = requireUserId()
        val userRef = db.collection("users").document(uid)
        val txId = UUID.randomUUID().toString()
        val txRef = userRef.collection("transactions").document(txId)

        return try {
            db.runTransaction { transaction ->
                val userDoc = transaction.get(userRef)
                val currentBalance = userDoc.getDouble("balanceGds") ?: 0.0
                if (currentBalance < amountGds) {
                    throw IllegalStateException("Solde insuffisant pour effectuer ce transfert")
                }
                val newBalance = currentBalance - amountGds

                transaction.update(userRef, mapOf(
                    "balanceGds" to newBalance,
                    "updatedAt" to FieldValue.serverTimestamp()
                ))

                val txData = hashMapOf(
                    "id" to txId,
                    "userId" to uid,
                    "type" to "SEND",
                    "amountGds" to amountGds,
                    "feeGds" to 0.0,
                    "recipient" to recipient,
                    "method" to "TRANSFERT",
                    "status" to "SUCCESS",
                    "description" to if (note.isNotBlank()) "Envoi à $recipient: $note" else "Envoi à $recipient",
                    "createdAt" to FieldValue.serverTimestamp()
                )
                transaction.set(txRef, txData)
            }.await()

            Result.success(
                FirestoreTransaction(
                    id = txId,
                    userId = uid,
                    type = "SEND",
                    amountGds = amountGds,
                    feeGds = 0.0,
                    recipient = recipient,
                    method = "TRANSFERT",
                    status = "SUCCESS",
                    description = if (note.isNotBlank()) "Envoi à $recipient: $note" else "Envoi à $recipient"
                )
            )
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, txRef.path)
            Result.failure(e)
        }
    }

    suspend fun recordRechargeOrder(order: FirestoreOrder): Result<FirestoreOrder> {
        val uid = requireUserId()
        val userRef = db.collection("users").document(uid)
        val orderId = if (order.id.isNotBlank()) order.id else UUID.randomUUID().toString()
        val orderRef = userRef.collection("orders").document(orderId)
        val txId = UUID.randomUUID().toString()
        val txRef = userRef.collection("transactions").document(txId)

        return try {
            db.runTransaction { transaction ->
                val userDoc = transaction.get(userRef)
                val currentBalance = userDoc.getDouble("balanceGds") ?: 0.0
                if (currentBalance < order.priceGds) {
                    throw IllegalStateException("Solde insuffisant pour payer cette recharge")
                }
                val newBalance = currentBalance - order.priceGds

                transaction.update(userRef, mapOf(
                    "balanceGds" to newBalance,
                    "updatedAt" to FieldValue.serverTimestamp()
                ))

                val orderData = hashMapOf(
                    "id" to orderId,
                    "userId" to uid,
                    "gameId" to order.gameId,
                    "gameTitle" to order.gameTitle,
                    "playerId" to order.playerId,
                    "zoneId" to order.zoneId,
                    "packageId" to order.packageId,
                    "packageName" to order.packageName,
                    "priceGds" to order.priceGds,
                    "status" to "PROCESSING",
                    "createdAt" to FieldValue.serverTimestamp(),
                    "updatedAt" to FieldValue.serverTimestamp()
                )
                transaction.set(orderRef, orderData)

                val txData = hashMapOf(
                    "id" to txId,
                    "userId" to uid,
                    "type" to "RECHARGE",
                    "amountGds" to order.priceGds,
                    "feeGds" to 0.0,
                    "recipient" to "${order.gameTitle} (${order.playerId})",
                    "method" to "WALLET",
                    "status" to "SUCCESS",
                    "description" to "Recharge ${order.packageName} pour ${order.gameTitle} (${order.playerId})",
                    "createdAt" to FieldValue.serverTimestamp()
                )
                transaction.set(txRef, txData)
            }.await()

            Result.success(order.copy(id = orderId, status = "PROCESSING"))
        } catch (e: Exception) {
            handleFirestoreError(e, OperationType.WRITE, orderRef.path)
            Result.failure(e)
        }
    }
}
