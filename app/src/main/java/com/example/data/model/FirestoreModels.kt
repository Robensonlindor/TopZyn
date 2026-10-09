package com.example.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class FirestoreUser(
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val balanceGds: Double = 0.0,
    val currency: String = "GDS",
    val isTransferEnabled: Boolean = true,
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

data class FirestoreTransaction(
    val id: String = "",
    val userId: String = "",
    val type: String = "DEPOSIT",
    val amountGds: Double = 0.0,
    val feeGds: Double = 0.0,
    val recipient: String = "",
    val method: String = "",
    val status: String = "SUCCESS",
    val description: String = "",
    @ServerTimestamp val createdAt: Timestamp? = null
)

data class FirestoreOrder(
    val id: String = "",
    val userId: String = "",
    val gameId: String = "",
    val gameTitle: String = "",
    val playerId: String = "",
    val zoneId: String = "",
    val packageId: String = "",
    val packageName: String = "",
    val priceGds: Double = 0.0,
    val status: String = "PENDING",
    @ServerTimestamp val createdAt: Timestamp? = null,
    @ServerTimestamp val updatedAt: Timestamp? = null
)

data class FirestoreNotification(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val message: String = "",
    val type: String = "WALLET",
    val isRead: Boolean = false,
    @ServerTimestamp val createdAt: Timestamp? = null
)
