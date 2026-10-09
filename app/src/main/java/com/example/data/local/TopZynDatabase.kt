package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.ApiServiceModel
import com.example.data.model.GameItem
import com.example.data.model.GamePackage
import com.example.data.model.NotificationItem
import com.example.data.model.OrderRecord
import com.example.data.model.TransactionRecord
import com.example.data.model.UserAccount
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        UserAccount::class,
        GameItem::class,
        GamePackage::class,
        OrderRecord::class,
        TransactionRecord::class,
        ApiServiceModel::class,
        NotificationItem::class
    ],
    version = 2,
    exportSchema = false
)
abstract class TopZynDatabase : RoomDatabase() {
    abstract fun topZynDao(): TopZynDao

    companion object {
        @Volatile
        private var INSTANCE: TopZynDatabase? = null

        fun getInstance(context: Context): TopZynDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TopZynDatabase::class.java,
                    "topzyn_database.db"
                )
                .fallbackToDestructiveMigration(true)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val dao = getInstance(context).topZynDao()
                            seedInitialData(dao)
                        }
                    }
                })
                .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun seedInitialData(dao: TopZynDao) {
            // Seed user
            val initialUser = UserAccount(
                id = "primary_user",
                fullName = "Jean-Marc Baptiste",
                email = "jmbaptiste@topzyn.com",
                phone = "+509 3412 8890",
                balanceGds = 2500.0,
                isBalanceVisible = true,
                role = "UTILISATEUR",
                isLoggedIn = true,
                biometricEnabled = true,
                pinCode = "1234",
                language = "Français",
                notifyRecharge = true,
                notifyPromo = true,
                notifySecurity = true
            )
            dao.insertUserAccount(initialUser)

            // Seed 4 initial popular games
            val games = listOf(
                GameItem(
                    id = "free_fire",
                    name = "Free Fire",
                    category = "Battle Royale",
                    imageDrawableName = "img_free_fire",
                    minPriceGds = 150.0,
                    currencyName = "Diamants",
                    isAvailable = true,
                    description = "Recharge instantanée de diamants Garena Free Fire via ID joueur.",
                    idPlaceholder = "Player ID (Ex: 298410298)",
                    requiresZoneId = false
                ),
                GameItem(
                    id = "pubg_mobile",
                    name = "PUBG Mobile",
                    category = "Battle Royale",
                    imageDrawableName = "img_pubg_mobile",
                    minPriceGds = 180.0,
                    currencyName = "UC",
                    isAvailable = true,
                    description = "Achat direct d'Unknown Cash (UC) PUBG Mobile officiel.",
                    idPlaceholder = "Character ID (Ex: 51298402)",
                    requiresZoneId = false
                ),
                GameItem(
                    id = "mobile_legends",
                    name = "Mobile Legends",
                    category = "MOBA",
                    imageDrawableName = "img_mobile_legends",
                    minPriceGds = 140.0,
                    currencyName = "Diamants",
                    isAvailable = true,
                    description = "Recharges de diamants et passes Starlight pour MLBB.",
                    idPlaceholder = "User ID (Ex: 12984920)",
                    requiresZoneId = true,
                    zonePlaceholder = "Zone ID (Ex: 2049)"
                ),
                GameItem(
                    id = "cod_mobile",
                    name = "Call of Duty Mobile",
                    category = "FPS / Action",
                    imageDrawableName = "img_cod_mobile",
                    minPriceGds = 160.0,
                    currencyName = "CP",
                    isAvailable = true,
                    description = "Points COD (CP) livrés directement sur votre compte Activision.",
                    idPlaceholder = "UID du joueur (Ex: 67489201948)",
                    requiresZoneId = false
                )
            )
            dao.insertGames(games)

            // Seed packages for each game
            val packages = listOf(
                // Free Fire
                GamePackage("ff_1", "free_fire", "100 Diamants", 100, 150.0, "+10 Bonus"),
                GamePackage("ff_2", "free_fire", "310 Diamants", 310, 425.0, "+31 Bonus", isPopular = true),
                GamePackage("ff_3", "free_fire", "520 Diamants", 520, 710.0, "+52 Bonus"),
                GamePackage("ff_4", "free_fire", "1060 Diamants", 1060, 1400.0, "+106 Bonus"),
                GamePackage("ff_5", "free_fire", "Passe Hebdomadaire", 1, 290.0, "VIP"),

                // PUBG Mobile
                GamePackage("pubg_1", "pubg_mobile", "60 UC", 60, 180.0),
                GamePackage("pubg_2", "pubg_mobile", "325 UC", 325, 680.0, "+25 Bonus", isPopular = true),
                GamePackage("pubg_3", "pubg_mobile", "660 UC", 660, 1350.0, "+60 Bonus"),
                GamePackage("pubg_4", "pubg_mobile", "1800 UC", 1800, 3600.0, "+300 Bonus"),

                // Mobile Legends
                GamePackage("ml_1", "mobile_legends", "86 Diamants", 86, 140.0),
                GamePackage("ml_2", "mobile_legends", "172 Diamants", 172, 275.0, isPopular = true),
                GamePackage("ml_3", "mobile_legends", "257 Diamants", 257, 410.0),
                GamePackage("ml_4", "mobile_legends", "706 Diamants", 706, 1100.0, "+66 Bonus"),

                // COD Mobile
                GamePackage("cod_1", "cod_mobile", "80 CP", 80, 160.0),
                GamePackage("cod_2", "cod_mobile", "420 CP", 420, 750.0, "+30 Bonus", isPopular = true),
                GamePackage("cod_3", "cod_mobile", "880 CP", 880, 1550.0, "+80 Bonus"),
                GamePackage("cod_4", "cod_mobile", "2400 CP", 2400, 4200.0, "+300 Bonus")
            )
            dao.insertPackages(packages)

            // Seed API Services
            val apiServices = listOf(
                ApiServiceModel(
                    id = "topzyn_api",
                    name = "TopZyn API",
                    description = "Recharge automatique pour vos bots et applications tierces.",
                    status = "Disponible",
                    iconType = "topzyn_api",
                    apiKey = "tz_live_9921_84821a8c90",
                    secretKey = "tz_sec_secret_key_prod_878",
                    webhookUrl = "https://votresite.com/api/topzyn/webhook"
                ),
                ApiServiceModel(
                    id = "moncash_api",
                    name = "MonCash API",
                    description = "Passerelle de paiement marchand MonCash Haïti.",
                    status = "Bientôt",
                    iconType = "moncash_api"
                ),
                ApiServiceModel(
                    id = "natcash_api",
                    name = "Natcash API",
                    description = "Intégration directe des transferts et paiements Natcash.",
                    status = "Bientôt",
                    iconType = "natcash_api"
                ),
                ApiServiceModel(
                    id = "webhooks_api",
                    name = "Webhooks TopZyn",
                    description = "Notifications en temps réel du statut des commandes.",
                    status = "Disponible",
                    iconType = "webhooks",
                    apiKey = "wh_live_2026_webhook_active",
                    secretKey = "wh_sec_signing_secret_9941",
                    webhookUrl = "https://votresite.com/api/topzyn/events"
                )
            )
            dao.insertApiServices(apiServices)

            // Seed initial transactions
            val now = System.currentTimeMillis()
            val transactions = listOf(
                TransactionRecord(
                    id = "tx_1",
                    title = "Dépôt MonCash",
                    type = "DEPOT",
                    amountGds = 2500.0,
                    timestamp = now - (3600 * 1000 * 4),
                    status = "COMPLÉTÉ",
                    details = "Via MonCash (+509 3412 8890)",
                    referenceCode = "MC-883492"
                ),
                TransactionRecord(
                    id = "tx_2",
                    title = "Recharge Free Fire",
                    type = "RECHARGE",
                    amountGds = -425.0,
                    timestamp = now - (3600 * 1000 * 24),
                    status = "COMPLÉTÉ",
                    details = "310 Diamants — ID: 29841029",
                    referenceCode = "TZ-772109"
                ),
                TransactionRecord(
                    id = "tx_3",
                    title = "Dépôt Natcash",
                    type = "DEPOT",
                    amountGds = 1000.0,
                    timestamp = now - (3600 * 1000 * 48),
                    status = "COMPLÉTÉ",
                    details = "Via Natcash agent",
                    referenceCode = "NC-102934"
                )
            )
            transactions.forEach { dao.insertTransaction(it) }

            // Seed orders
            val orders = listOf(
                OrderRecord(
                    id = "order_1",
                    orderNumber = "TZ-2026-772109",
                    gameId = "free_fire",
                    gameName = "Free Fire",
                    packageTitle = "310 Diamants (+31 Bonus)",
                    playerId = "29841029",
                    priceGds = 425.0,
                    paymentMethod = "WALLET",
                    status = "REUSSIE",
                    timestamp = now - (3600 * 1000 * 24)
                ),
                OrderRecord(
                    id = "order_2",
                    orderNumber = "TZ-2026-881920",
                    gameId = "pubg_mobile",
                    gameName = "PUBG Mobile",
                    packageTitle = "60 UC",
                    playerId = "51298402",
                    priceGds = 180.0,
                    paymentMethod = "WALLET",
                    status = "EN_TRAITEMENT",
                    timestamp = now - (1800 * 1000)
                )
            )
            orders.forEach { dao.insertOrder(it) }

            // Seed initial notifications
            val notifications = listOf(
                NotificationItem(
                    id = "notif_1",
                    title = "Bienvenue sur TopZyn",
                    message = "Votre portefeuille en Gourdes (GDS) est prêt. Profitez des meilleurs tarifs de recharge !",
                    timestamp = now - 3600 * 1000,
                    isRead = false,
                    type = "INFO"
                ),
                NotificationItem(
                    id = "notif_2",
                    title = "Bonus Free Fire actif",
                    message = "+10% de diamants offerts sur tous les packs supérieurs à 300 diamants ce week-end.",
                    timestamp = now - 7200 * 1000,
                    isRead = false,
                    type = "PROMO"
                ),
                NotificationItem(
                    id = "notif_3",
                    title = "TopZyn API disponible",
                    message = "Connectez votre boutique ou bot Discord avec notre API officielle.",
                    timestamp = now - 86400 * 1000,
                    isRead = true,
                    type = "INFO"
                )
            )
            notifications.forEach { dao.insertNotification(it) }
        }
    }
}
