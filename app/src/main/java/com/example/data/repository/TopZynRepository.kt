package com.example.data.repository

import com.example.data.local.TopZynDao
import com.example.data.local.TopZynDatabase
import com.example.data.model.ApiServiceModel
import com.example.data.model.GameItem
import com.example.data.model.GamePackage
import com.example.data.model.NotificationItem
import com.example.data.model.OrderRecord
import com.example.data.model.TransactionRecord
import com.example.data.model.UserAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.UUID

class TopZynRepository(private val dao: TopZynDao) {

    val userAccountFlow: Flow<UserAccount?> = dao.getUserAccountFlow()
    val allGamesFlow: Flow<List<GameItem>> = dao.getAllGamesFlow()
    val allOrdersFlow: Flow<List<OrderRecord>> = dao.getAllOrdersFlow()
    val allTransactionsFlow: Flow<List<TransactionRecord>> = dao.getAllTransactionsFlow()
    val allApiServicesFlow: Flow<List<ApiServiceModel>> = dao.getAllApiServicesFlow()
    val allNotificationsFlow: Flow<List<NotificationItem>> = dao.getAllNotificationsFlow()

    suspend fun checkAndSeedDatabase() = withContext(Dispatchers.IO) {
        val user = dao.getUserAccount()
        if (user == null) {
            TopZynDatabase.seedInitialData(dao)
        }
    }

    fun getPackagesForGameFlow(gameId: String): Flow<List<GamePackage>> {
        return dao.getPackagesForGameFlow(gameId)
    }

    suspend fun toggleBalanceVisibility(userId: String = "primary_user", isVisible: Boolean) {
        withContext(Dispatchers.IO) {
            dao.toggleBalanceVisibility(userId, isVisible)
        }
    }

    suspend fun updateUserProfile(user: UserAccount) {
        withContext(Dispatchers.IO) {
            dao.updateUserAccount(user)
        }
    }

    suspend fun insertOrUpdateUser(user: UserAccount) = withContext(Dispatchers.IO) {
        dao.insertUserAccount(user)
    }

    suspend fun setLoginState(isLoggedIn: Boolean) = withContext(Dispatchers.IO) {
        dao.updateLoginState("primary_user", isLoggedIn)
    }

    suspend fun updatePin(newPin: String) = withContext(Dispatchers.IO) {
        dao.updatePinCode("primary_user", newPin)
    }

    suspend fun updateLanguage(lang: String) = withContext(Dispatchers.IO) {
        dao.updateLanguage("primary_user", lang)
    }

    suspend fun updateBiometric(enabled: Boolean) = withContext(Dispatchers.IO) {
        dao.updateBiometric("primary_user", enabled)
    }

    suspend fun updateNotificationPrefs(recharge: Boolean, promo: Boolean, sec: Boolean) = withContext(Dispatchers.IO) {
        dao.updateNotificationPrefs("primary_user", recharge, promo, sec)
    }

    suspend fun performDeposit(
        amountGds: Double,
        methodName: String,
        referenceOrPhone: String
    ): Result<TransactionRecord> = withContext(Dispatchers.IO) {
        try {
            val user = dao.getUserAccount() ?: return@withContext Result.failure(Exception("Utilisateur non trouvé"))
            val newBalance = user.balanceGds + amountGds
            dao.updateBalance(user.id, newBalance)

            val refCode = "DEP-" + UUID.randomUUID().toString().take(6).uppercase()
            val tx = TransactionRecord(
                id = UUID.randomUUID().toString(),
                title = "Dépôt $methodName",
                type = "DEPOT",
                amountGds = amountGds,
                timestamp = System.currentTimeMillis(),
                status = "COMPLÉTÉ",
                details = "Méthode: $methodName ($referenceOrPhone)",
                referenceCode = refCode
            )
            dao.insertTransaction(tx)

            dao.insertNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    title = "Dépôt validé",
                    message = "Votre compte a été crédité de ${"%.2f".format(amountGds)} GDS via $methodName.",
                    timestamp = System.currentTimeMillis(),
                    isRead = false,
                    type = "WALLET"
                )
            )

            Result.success(tx)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun performWithdrawal(
        amountGds: Double,
        methodName: String,
        accountNumber: String
    ): Result<TransactionRecord> = withContext(Dispatchers.IO) {
        try {
            val user = dao.getUserAccount() ?: return@withContext Result.failure(Exception("Utilisateur non trouvé"))
            if (user.balanceGds < amountGds) {
                return@withContext Result.failure(Exception("Solde insuffisant pour ce retrait"))
            }

            val newBalance = user.balanceGds - amountGds
            dao.updateBalance(user.id, newBalance)

            val refCode = "RET-" + UUID.randomUUID().toString().take(6).uppercase()
            val tx = TransactionRecord(
                id = UUID.randomUUID().toString(),
                title = "Retrait $methodName",
                type = "RETRAIT",
                amountGds = -amountGds,
                timestamp = System.currentTimeMillis(),
                status = "COMPLÉTÉ",
                details = "Vers: $accountNumber ($methodName)",
                referenceCode = refCode
            )
            dao.insertTransaction(tx)

            dao.insertNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    title = "Retrait effectué",
                    message = "Un retrait de ${"%.2f".format(amountGds)} GDS a été envoyé sur $accountNumber.",
                    timestamp = System.currentTimeMillis(),
                    isRead = false,
                    type = "WALLET"
                )
            )

            Result.success(tx)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun performSendTransfer(
        recipient: String,
        amountGds: Double,
        note: String
    ): Result<TransactionRecord> = withContext(Dispatchers.IO) {
        try {
            val user = dao.getUserAccount() ?: return@withContext Result.failure(Exception("Utilisateur non trouvé"))
            if (user.balanceGds < amountGds) {
                return@withContext Result.failure(Exception("Solde insuffisant pour ce transfert"))
            }

            val newBalance = user.balanceGds - amountGds
            dao.updateBalance(user.id, newBalance)

            val refCode = "TRF-" + UUID.randomUUID().toString().take(6).uppercase()
            val tx = TransactionRecord(
                id = UUID.randomUUID().toString(),
                title = "Transfert vers $recipient",
                type = "TRANSFERT",
                amountGds = -amountGds,
                timestamp = System.currentTimeMillis(),
                status = "COMPLÉTÉ",
                details = if (note.isNotBlank()) "Note: $note" else "Transfert TopZyn",
                referenceCode = refCode
            )
            dao.insertTransaction(tx)

            dao.insertNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    title = "Transfert envoyé",
                    message = "Vous avez transféré ${"%.2f".format(amountGds)} GDS à $recipient.",
                    timestamp = System.currentTimeMillis(),
                    isRead = false,
                    type = "WALLET"
                )
            )

            Result.success(tx)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun performRecharge(
        game: GameItem,
        pkg: GamePackage,
        playerId: String,
        zoneId: String?,
        paymentMethod: String
    ): Result<OrderRecord> = withContext(Dispatchers.IO) {
        try {
            val user = dao.getUserAccount() ?: return@withContext Result.failure(Exception("Utilisateur non trouvé"))

            if (paymentMethod == "WALLET") {
                if (user.balanceGds < pkg.priceGds) {
                    return@withContext Result.failure(Exception("Solde Wallet insuffisant (${user.balanceGds} GDS dispo, ${pkg.priceGds} GDS requis)"))
                }
                val newBalance = user.balanceGds - pkg.priceGds
                dao.updateBalance(user.id, newBalance)
            }

            val orderNumber = "TZ-2026-" + (100000 + (Math.random() * 900000).toInt()).toString()
            val order = OrderRecord(
                id = UUID.randomUUID().toString(),
                orderNumber = orderNumber,
                gameId = game.id,
                gameName = game.name,
                packageTitle = pkg.title,
                playerId = playerId,
                zoneId = zoneId,
                priceGds = pkg.priceGds,
                paymentMethod = paymentMethod,
                status = "REUSSIE",
                timestamp = System.currentTimeMillis()
            )
            dao.insertOrder(order)

            val tx = TransactionRecord(
                id = UUID.randomUUID().toString(),
                title = "Recharge ${game.name}",
                type = "RECHARGE",
                amountGds = if (paymentMethod == "WALLET") -pkg.priceGds else 0.0,
                timestamp = System.currentTimeMillis(),
                status = "COMPLÉTÉ",
                details = "${pkg.title} • ID: $playerId • $paymentMethod",
                referenceCode = orderNumber
            )
            dao.insertTransaction(tx)

            dao.insertNotification(
                NotificationItem(
                    id = UUID.randomUUID().toString(),
                    title = "Recharge ${game.name} confirmée",
                    message = "Votre commande (${pkg.title}) pour l'ID $playerId a été livrée avec succès ! (N° $orderNumber)",
                    timestamp = System.currentTimeMillis(),
                    isRead = false,
                    type = "RECHARGE"
                )
            )

            Result.success(order)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addOrUpdateGame(game: GameItem) = withContext(Dispatchers.IO) {
        dao.insertGame(game)
    }

    suspend fun addPackage(pkg: GamePackage) = withContext(Dispatchers.IO) {
        dao.insertPackage(pkg)
    }

    suspend fun updateApiServiceStatus(id: String, status: String) = withContext(Dispatchers.IO) {
        dao.updateApiServiceStatus(id, status)
    }

    suspend fun updateApiCredentials(id: String, apiKey: String, webhookUrl: String) = withContext(Dispatchers.IO) {
        dao.updateApiKeys(id, apiKey, webhookUrl)
    }

    suspend fun markAllNotificationsAsRead() = withContext(Dispatchers.IO) {
        dao.markAllNotificationsAsRead()
    }
}
