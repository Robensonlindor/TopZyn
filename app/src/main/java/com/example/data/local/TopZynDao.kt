package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ApiServiceModel
import com.example.data.model.GameItem
import com.example.data.model.GamePackage
import com.example.data.model.NotificationItem
import com.example.data.model.OrderRecord
import com.example.data.model.TransactionRecord
import com.example.data.model.UserAccount
import kotlinx.coroutines.flow.Flow

@Dao
interface TopZynDao {
    // User Account
    @Query("SELECT * FROM user_account WHERE id = :userId LIMIT 1")
    fun getUserAccountFlow(userId: String = "primary_user"): Flow<UserAccount?>

    @Query("SELECT * FROM user_account WHERE id = :userId LIMIT 1")
    suspend fun getUserAccount(userId: String = "primary_user"): UserAccount?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserAccount(user: UserAccount)

    @Update
    suspend fun updateUserAccount(user: UserAccount)

    @Query("UPDATE user_account SET balanceGds = :newBalance WHERE id = :userId")
    suspend fun updateBalance(userId: String, newBalance: Double)

    @Query("UPDATE user_account SET isBalanceVisible = :isVisible WHERE id = :userId")
    suspend fun toggleBalanceVisibility(userId: String, isVisible: Boolean)

    @Query("UPDATE user_account SET isLoggedIn = :isLoggedIn WHERE id = :userId")
    suspend fun updateLoginState(userId: String, isLoggedIn: Boolean)

    @Query("UPDATE user_account SET pinCode = :newPin WHERE id = :userId")
    suspend fun updatePinCode(userId: String, newPin: String)

    @Query("UPDATE user_account SET language = :lang WHERE id = :userId")
    suspend fun updateLanguage(userId: String, lang: String)

    @Query("UPDATE user_account SET biometricEnabled = :enabled WHERE id = :userId")
    suspend fun updateBiometric(userId: String, enabled: Boolean)

    @Query("UPDATE user_account SET notifyRecharge = :recharge, notifyPromo = :promo, notifySecurity = :sec WHERE id = :userId")
    suspend fun updateNotificationPrefs(userId: String, recharge: Boolean, promo: Boolean, sec: Boolean)

    // Games
    @Query("SELECT * FROM games ORDER BY name ASC")
    fun getAllGamesFlow(): Flow<List<GameItem>>

    @Query("SELECT * FROM games WHERE id = :gameId LIMIT 1")
    suspend fun getGameById(gameId: String): GameItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<GameItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: GameItem)

    @Update
    suspend fun updateGame(game: GameItem)

    // Game Packages
    @Query("SELECT * FROM game_packages WHERE gameId = :gameId ORDER BY priceGds ASC")
    fun getPackagesForGameFlow(gameId: String): Flow<List<GamePackage>>

    @Query("SELECT * FROM game_packages WHERE gameId = :gameId ORDER BY priceGds ASC")
    suspend fun getPackagesForGame(gameId: String): List<GamePackage>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackages(packages: List<GamePackage>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPackage(pkg: GamePackage)

    @Update
    suspend fun updatePackage(pkg: GamePackage)

    // Orders
    @Query("SELECT * FROM orders ORDER BY timestamp DESC")
    fun getAllOrdersFlow(): Flow<List<OrderRecord>>

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    suspend fun getOrderById(orderId: String): OrderRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: OrderRecord)

    @Query("UPDATE orders SET status = :status WHERE id = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)

    // Transactions
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactionsFlow(): Flow<List<TransactionRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionRecord)

    // API Services
    @Query("SELECT * FROM api_services")
    fun getAllApiServicesFlow(): Flow<List<ApiServiceModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApiServices(services: List<ApiServiceModel>)

    @Query("UPDATE api_services SET status = :status WHERE id = :id")
    suspend fun updateApiServiceStatus(id: String, status: String)

    @Query("UPDATE api_services SET apiKey = :apiKey, webhookUrl = :webhookUrl WHERE id = :id")
    suspend fun updateApiKeys(id: String, apiKey: String, webhookUrl: String)

    // Notifications
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotificationsFlow(): Flow<List<NotificationItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationItem)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationAsRead(id: String)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllNotificationsAsRead()
}
