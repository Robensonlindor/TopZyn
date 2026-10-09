package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_account")
data class UserAccount(
    @PrimaryKey val id: String = "primary_user",
    val fullName: String = "Jean-Marc Baptiste",
    val email: String = "jmbaptiste@topzyn.com",
    val phone: String = "+509 3412 8890",
    val balanceGds: Double = 2500.0,
    val isBalanceVisible: Boolean = true,
    val role: String = "UTILISATEUR", // "UTILISATEUR" or "ADMIN"
    val isLoggedIn: Boolean = true,
    val avatarId: Int = 1,
    val biometricEnabled: Boolean = true,
    val pinCode: String = "1234",
    val language: String = "Français",
    val notifyRecharge: Boolean = true,
    val notifyPromo: Boolean = true,
    val notifySecurity: Boolean = true
)

@Entity(tableName = "games")
data class GameItem(
    @PrimaryKey val id: String,
    val name: String,
    val category: String, // "Battle Royale", "MOBA", "FPS / Action", "Cartes Cadeaux"
    val imageDrawableName: String,
    val minPriceGds: Double,
    val currencyName: String, // "Diamants", "UC", "CP", "Robux"
    val isAvailable: Boolean = true,
    val description: String = "",
    val idPlaceholder: String = "ID Joueur (UID)",
    val requiresZoneId: Boolean = false,
    val zonePlaceholder: String = "Zone / Serveur ID"
)

@Entity(tableName = "game_packages")
data class GamePackage(
    @PrimaryKey val id: String,
    val gameId: String,
    val title: String, // e.g. "100 Diamants"
    val quantity: Int, // 100
    val priceGds: Double, // 150.0
    val bonus: String? = null, // "+10 Bonus"
    val isPopular: Boolean = false
)

@Entity(tableName = "orders")
data class OrderRecord(
    @PrimaryKey val id: String,
    val orderNumber: String, // e.g. "TZ-2026-9921"
    val gameId: String,
    val gameName: String,
    val packageTitle: String,
    val playerId: String,
    val zoneId: String? = null,
    val priceGds: Double,
    val paymentMethod: String, // "WALLET", "MONCASH", "NATCASH"
    val status: String = "REUSSIE", // "EN_ATTENTE", "EN_TRAITEMENT", "REUSSIE", "ECHOUEE"
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = "Livraison automatique instantanée"
)

@Entity(tableName = "transactions")
data class TransactionRecord(
    @PrimaryKey val id: String,
    val title: String,
    val type: String, // "DEPOT", "RECHARGE", "TRANSFERT", "RETRAIT"
    val amountGds: Double,
    val timestamp: Long,
    val status: String = "COMPLÉTÉ",
    val details: String,
    val referenceCode: String
)

@Entity(tableName = "api_services")
data class ApiServiceModel(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val status: String, // "Disponible", "Bientôt", "Non activé"
    val iconType: String, // "topzyn_api", "moncash_api", "natcash_api", "webhooks"
    val apiKey: String = "tz_live_9921_84821a8c90",
    val secretKey: String = "tz_sec_secret_key_prod_878",
    val webhookUrl: String = "https://votresite.com/api/topzyn/webhook"
)

@Entity(tableName = "notifications")
data class NotificationItem(
    @PrimaryKey val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean = false,
    val type: String = "INFO" // "RECHARGE", "WALLET", "PROMO", "SECURITY"
)

data class UserSessionItem(
    val id: String,
    val deviceName: String,
    val clientType: String, // "Android Mobile", "Web Chrome", "Tablette"
    val location: String,
    val ipAddress: String,
    val lastActive: String,
    val isCurrent: Boolean
)

data class FaqItem(
    val id: String,
    val category: String,
    val question: String,
    val answer: String
)
