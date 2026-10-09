package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.ApiServiceModel
import com.example.data.model.GameItem
import com.example.data.model.GamePackage
import com.example.data.model.NotificationItem
import com.example.data.model.OrderRecord
import com.example.data.model.TransactionRecord
import com.example.data.model.UserAccount
import com.example.data.repository.TopZynRepository
import com.example.navigation.AppScreen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TopZynUiState(
    val user: UserAccount? = null,
    val games: List<GameItem> = emptyList(),
    val orders: List<OrderRecord> = emptyList(),
    val transactions: List<TransactionRecord> = emptyList(),
    val apiServices: List<ApiServiceModel> = emptyList(),
    val notifications: List<NotificationItem> = emptyList(),
    val unreadNotificationsCount: Int = 0,
    val isLoading: Boolean = false
)

sealed class TopZynUiEvent {
    data class ShowSnackbar(val message: String) : TopZynUiEvent()
    data class ShowTransactionSuccess(val transaction: TransactionRecord) : TopZynUiEvent()
}

class TopZynViewModel(private val repository: TopZynRepository) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<TopZynUiEvent>()
    val eventFlow: SharedFlow<TopZynUiEvent> = _eventFlow.asSharedFlow()

    // Navigation Stack
    private val backStack = mutableListOf<AppScreen>()
    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Main(0))
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    // Main 4-Tab Navigation State
    private val _selectedTab = MutableStateFlow(0) // 0: Accueil, 1: Boutique, 2: Wallet, 3: Profil
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    // Game Packages Cache
    private val _packagesForSelectedGame = MutableStateFlow<List<GamePackage>>(emptyList())
    val packagesForSelectedGame: StateFlow<List<GamePackage>> = _packagesForSelectedGame.asStateFlow()

    // Active Developer API Detail Dialog
    private val _selectedApiService = MutableStateFlow<ApiServiceModel?>(null)
    val selectedApiService: StateFlow<ApiServiceModel?> = _selectedApiService.asStateFlow()

    // Dialog Visibilities
    private val _showLogoutDialog = MutableStateFlow(false)
    val showLogoutDialog: StateFlow<Boolean> = _showLogoutDialog.asStateFlow()

    private val _recentCompletedTransaction = MutableStateFlow<TransactionRecord?>(null)
    val recentCompletedTransaction: StateFlow<TransactionRecord?> = _recentCompletedTransaction.asStateFlow()

    private val _recentCompletedOrder = MutableStateFlow<OrderRecord?>(null)
    val recentCompletedOrder: StateFlow<OrderRecord?> = _recentCompletedOrder.asStateFlow()

    init {
        viewModelScope.launch {
            repository.checkAndSeedDatabase()
        }
    }

    private data class BaseData(
        val user: UserAccount?,
        val games: List<GameItem>,
        val orders: List<OrderRecord>,
        val transactions: List<TransactionRecord>
    )

    private val baseDataFlow = combine(
        repository.userAccountFlow,
        repository.allGamesFlow,
        repository.allOrdersFlow,
        repository.allTransactionsFlow
    ) { user, games, orders, txs ->
        BaseData(user, games, orders, txs)
    }

    val uiState: StateFlow<TopZynUiState> = combine(
        baseDataFlow,
        repository.allApiServicesFlow,
        repository.allNotificationsFlow
    ) { base, apis, notifs ->
        TopZynUiState(
            user = base.user,
            games = base.games,
            orders = base.orders,
            transactions = base.transactions,
            apiServices = apis,
            notifications = notifs,
            unreadNotificationsCount = notifs.count { !it.isRead },
            isLoading = false
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TopZynUiState(isLoading = true)
    )

    // Navigation Methods
    fun navigateTo(screen: AppScreen) {
        backStack.add(_currentScreen.value)
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        if (backStack.isNotEmpty()) {
            val previous = backStack.removeAt(backStack.size - 1)
            _currentScreen.value = previous
            if (previous is AppScreen.Main) {
                _selectedTab.value = previous.tabIndex
            }
            return true
        } else if (_currentScreen.value !is AppScreen.Main || (_currentScreen.value as AppScreen.Main).tabIndex != 0) {
            _currentScreen.value = AppScreen.Main(0)
            _selectedTab.value = 0
            return true
        }
        return false
    }

    fun onTabSelected(tabIndex: Int) {
        backStack.clear()
        _selectedTab.value = tabIndex
        _currentScreen.value = AppScreen.Main(tabIndex)
    }

    fun loadPackagesForGame(gameId: String) {
        viewModelScope.launch {
            repository.getPackagesForGameFlow(gameId).collect { pkgs ->
                _packagesForSelectedGame.value = pkgs
            }
        }
    }

    fun toggleBalanceVisibility() {
        val current = uiState.value.user ?: return
        viewModelScope.launch {
            repository.toggleBalanceVisibility(current.id, !current.isBalanceVisible)
        }
    }

    fun openLogoutDialog() {
        _showLogoutDialog.value = true
    }

    fun closeLogoutDialog() {
        _showLogoutDialog.value = false
    }

    fun performLogout() {
        viewModelScope.launch {
            repository.setLoginState(false)
            _showLogoutDialog.value = false
            backStack.clear()
            _currentScreen.value = AppScreen.Welcome
            _eventFlow.emit(TopZynUiEvent.ShowSnackbar("Déconnexion réussie"))
        }
    }

    fun performLogin() {
        viewModelScope.launch {
            repository.setLoginState(true)
            backStack.clear()
            _currentScreen.value = AppScreen.Main(0)
            _selectedTab.value = 0
            _eventFlow.emit(TopZynUiEvent.ShowSnackbar("Connexion réussie ! Bon retour sur TopZyn."))
        }
    }

    fun dismissTransactionReceipt() {
        _recentCompletedTransaction.value = null
    }

    fun depositFunds(amountGds: Double, method: String, phoneOrRef: String) {
        viewModelScope.launch {
            val result = repository.performDeposit(amountGds, method, phoneOrRef)
            result.onSuccess { tx ->
                _recentCompletedTransaction.value = tx
                _currentScreen.value = AppScreen.Receipt(tx.id)
                _eventFlow.emit(TopZynUiEvent.ShowSnackbar("Dépôt de ${"%.2f".format(amountGds)} GDS validé avec succès !"))
            }.onFailure { err ->
                _eventFlow.emit(TopZynUiEvent.ShowSnackbar(err.message ?: "Erreur lors du dépôt"))
            }
        }
    }

    fun withdrawFunds(amountGds: Double, method: String, account: String) {
        viewModelScope.launch {
            val result = repository.performWithdrawal(amountGds, method, account)
            result.onSuccess { tx ->
                _recentCompletedTransaction.value = tx
                _currentScreen.value = AppScreen.Receipt(tx.id)
                _eventFlow.emit(TopZynUiEvent.ShowSnackbar("Retrait de ${"%.2f".format(amountGds)} GDS initié avec succès !"))
            }.onFailure { err ->
                _eventFlow.emit(TopZynUiEvent.ShowSnackbar(err.message ?: "Erreur lors du retrait"))
            }
        }
    }

    fun sendFunds(recipient: String, amountGds: Double, note: String) {
        viewModelScope.launch {
            val result = repository.performSendTransfer(recipient, amountGds, note)
            result.onSuccess { tx ->
                _recentCompletedTransaction.value = tx
                _currentScreen.value = AppScreen.Receipt(tx.id)
                _eventFlow.emit(TopZynUiEvent.ShowSnackbar("Transfert de ${"%.2f".format(amountGds)} GDS envoyé avec succès !"))
            }.onFailure { err ->
                _eventFlow.emit(TopZynUiEvent.ShowSnackbar(err.message ?: "Erreur de transfert"))
            }
        }
    }

    fun rechargeGame(game: GameItem, pkg: GamePackage, playerId: String, zoneId: String?, paymentMethod: String) {
        viewModelScope.launch {
            val result = repository.performRecharge(game, pkg, playerId, zoneId, paymentMethod)
            result.onSuccess { order ->
                _recentCompletedOrder.value = order
                _currentScreen.value = AppScreen.OrderTracking(order.id)
                _eventFlow.emit(TopZynUiEvent.ShowSnackbar("Recharge ${game.name} confirmée pour l'ID $playerId !"))
            }.onFailure { err ->
                _eventFlow.emit(TopZynUiEvent.ShowSnackbar(err.message ?: "Échec de la recharge"))
            }
        }
    }

    fun updateProfile(name: String, email: String, phone: String) {
        val current = uiState.value.user ?: return
        viewModelScope.launch {
            repository.updateUserProfile(
                current.copy(
                    fullName = name,
                    email = email,
                    phone = phone
                )
            )
            _eventFlow.emit(TopZynUiEvent.ShowSnackbar("Profil mis à jour !"))
            navigateBack()
        }
    }

    fun updateLanguage(lang: String) {
        viewModelScope.launch {
            repository.updateLanguage(lang)
            _eventFlow.emit(TopZynUiEvent.ShowSnackbar("Langue changée en $lang"))
            navigateBack()
        }
    }

    fun updateBiometric(enabled: Boolean) {
        viewModelScope.launch {
            repository.updateBiometric(enabled)
            _eventFlow.emit(TopZynUiEvent.ShowSnackbar("Sécurité biométrique mise à jour"))
        }
    }

    fun updateNotificationPrefs(recharge: Boolean, promo: Boolean, security: Boolean) {
        viewModelScope.launch {
            repository.updateNotificationPrefs(recharge, promo, security)
            _eventFlow.emit(TopZynUiEvent.ShowSnackbar("Préférences de notifications enregistrées"))
        }
    }

    fun markAllNotificationsAsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsAsRead()
        }
    }

    fun adminAddGame(name: String, category: String, minPrice: Double, currency: String, drawableName: String) {
        viewModelScope.launch {
            val id = name.lowercase().replace(" ", "_").replace("-", "_")
            val newGame = GameItem(
                id = id,
                name = name,
                category = category,
                imageDrawableName = drawableName,
                minPriceGds = minPrice,
                currencyName = currency,
                isAvailable = true,
                description = "Recharge officielle TopZyn pour $name.",
                idPlaceholder = "ID Joueur (UID)"
            )
            repository.addOrUpdateGame(newGame)
            repository.addPackage(
                GamePackage(
                    id = "${id}_1",
                    gameId = id,
                    title = "Pack Découverte ($currency)",
                    quantity = 100,
                    priceGds = minPrice,
                    isPopular = true
                )
            )
            _eventFlow.emit(TopZynUiEvent.ShowSnackbar("Nouveau jeu $name ajouté au catalogue !"))
        }
    }

    fun adminToggleApiStatus(serviceId: String, currentStatus: String) {
        viewModelScope.launch {
            val newStatus = when (currentStatus) {
                "Disponible" -> "Bientôt"
                "Bientôt" -> "Non activé"
                else -> "Disponible"
            }
            repository.updateApiServiceStatus(serviceId, newStatus)
            _eventFlow.emit(TopZynUiEvent.ShowSnackbar("Statut API mis à jour : $newStatus"))
        }
    }
}

class TopZynViewModelFactory(private val repository: TopZynRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopZynViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TopZynViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
