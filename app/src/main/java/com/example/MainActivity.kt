package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.TopZynDatabase
import com.example.data.repository.TopZynRepository
import com.example.navigation.AppScreen
import com.example.ui.components.AdminPanelDialog
import com.example.ui.components.ApiServiceDetailDialog
import com.example.ui.components.TopZynBottomNav
import com.example.ui.screens.BoutiqueScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.screens.api.ApiDocumentationScreen
import com.example.ui.screens.api.ApiKeysManagementScreen
import com.example.ui.screens.api.ApiStatusScreen
import com.example.ui.screens.api.FullDeveloperPortalScreen
import com.example.ui.screens.auth.ForgotPasswordScreen
import com.example.ui.screens.auth.LoginScreen
import com.example.ui.screens.auth.LogoutConfirmDialog
import com.example.ui.screens.auth.RegisterScreen
import com.example.ui.screens.auth.ResetPasswordScreen
import com.example.ui.screens.auth.SplashScreen
import com.example.ui.screens.auth.VerifyOtpScreen
import com.example.ui.screens.auth.WelcomeScreen
import com.example.ui.screens.main.CategoryScreen
import com.example.ui.screens.main.FullNotificationsScreen
import com.example.ui.screens.main.SearchScreen
import com.example.ui.screens.profile.ContactSupportScreen
import com.example.ui.screens.profile.DetailedProfileScreen
import com.example.ui.screens.profile.EditProfileScreen
import com.example.ui.screens.profile.HelpFaqScreen
import com.example.ui.screens.profile.LanguageSelectionScreen
import com.example.ui.screens.profile.LegalTextScreen
import com.example.ui.screens.profile.NotificationPreferencesScreen
import com.example.ui.screens.profile.SecuritySettingsScreen
import com.example.ui.screens.profile.SessionManagementScreen
import com.example.ui.screens.recharge.DedicatedGameRechargeScreen
import com.example.ui.screens.recharge.GameDetailScreen
import com.example.ui.screens.recharge.OrderTrackingScreen
import com.example.ui.screens.system.FullConnectionErrorScreen
import com.example.ui.screens.system.FullLoadingScreen
import com.example.ui.screens.system.FullMaintenanceScreen
import com.example.ui.screens.system.FullSessionExpiredScreen
import com.example.ui.screens.wallet.FullDepositScreen
import com.example.ui.screens.wallet.FullTransactionHistoryScreen
import com.example.ui.screens.wallet.FullTransactionReceiptScreen
import com.example.ui.screens.wallet.FullTransferScreen
import com.example.ui.screens.wallet.FullWithdrawScreen
import com.example.ui.theme.TopZynTheme
import com.example.ui.viewmodel.TopZynUiEvent
import com.example.ui.viewmodel.TopZynViewModel
import com.example.ui.viewmodel.TopZynViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = TopZynDatabase.getInstance(this)
        val repository = TopZynRepository(database.topZynDao())
        val factory = TopZynViewModelFactory(repository)

        setContent {
            TopZynTheme {
                val viewModel: TopZynViewModel = viewModel(factory = factory)
                TopZynApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TopZynApp(viewModel: TopZynViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

    val packagesForSelectedGame by viewModel.packagesForSelectedGame.collectAsStateWithLifecycle()
    val selectedApiService by viewModel.selectedApiService.collectAsStateWithLifecycle()
    val showLogoutDialog by viewModel.showLogoutDialog.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is TopZynUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(event.message)
                is TopZynUiEvent.ShowTransactionSuccess -> {}
            }
        }
    }

    // Android Hardware / Gesture Back Handling
    BackHandler {
        viewModel.navigateBack()
    }

    val isMainTabScreen = currentScreen is AppScreen.Main

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        bottomBar = {
            if (isMainTabScreen) {
                TopZynBottomNav(
                    selectedTab = selectedTab,
                    onTabSelected = { viewModel.onTabSelected(it) }
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val screen = currentScreen) {
                // A. Authentification Screens
                is AppScreen.Splash -> {
                    SplashScreen(
                        onContinue = { viewModel.navigateTo(AppScreen.Welcome) }
                    )
                }
                is AppScreen.Welcome -> {
                    WelcomeScreen(
                        onNavigateLogin = { viewModel.navigateTo(AppScreen.Login) },
                        onNavigateRegister = { viewModel.navigateTo(AppScreen.Register) },
                        onContinueGuest = { viewModel.onTabSelected(0) }
                    )
                }
                is AppScreen.Login -> {
                    LoginScreen(
                        onLoginSuccess = { viewModel.performLogin() },
                        onNavigateRegister = { viewModel.navigateTo(AppScreen.Register) },
                        onNavigateForgotPassword = { viewModel.navigateTo(AppScreen.ForgotPassword) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.Register -> {
                    RegisterScreen(
                        onRegisterSuccess = { contact -> viewModel.navigateTo(AppScreen.VerifyOtp(contact)) },
                        onNavigateLogin = { viewModel.navigateTo(AppScreen.Login) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.VerifyOtp -> {
                    VerifyOtpScreen(
                        contact = screen.contact,
                        onVerifySuccess = { viewModel.performLogin() },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.ForgotPassword -> {
                    ForgotPasswordScreen(
                        onSendResetLink = { viewModel.navigateTo(AppScreen.ResetPassword) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.ResetPassword -> {
                    ResetPasswordScreen(
                        onResetSuccess = { viewModel.performLogin() },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }

                // B. Écrans principaux (4 onglets avec barre inférieure)
                is AppScreen.Main -> {
                    when (screen.tabIndex) {
                        0 -> {
                            HomeScreen(
                                user = uiState.user,
                                unreadNotificationsCount = uiState.unreadNotificationsCount,
                                games = uiState.games,
                                apiServices = uiState.apiServices,
                                onToggleBalanceVisibility = { viewModel.toggleBalanceVisibility() },
                                onDepositClick = { viewModel.navigateTo(AppScreen.Deposit) },
                                onSendClick = { viewModel.navigateTo(AppScreen.Transfer) },
                                onGameClick = { game ->
                                    viewModel.loadPackagesForGame(game.id)
                                    viewModel.navigateTo(AppScreen.GameDetail(game.id))
                                },
                                onApiServiceClick = { service ->
                                    viewModel.navigateTo(AppScreen.DeveloperPortal)
                                },
                                onNotificationsClick = { viewModel.navigateTo(AppScreen.Notifications) },
                                onProfileClick = { viewModel.onTabSelected(3) }
                            )
                        }
                        1 -> {
                            BoutiqueScreen(
                                games = uiState.games,
                                onGameClick = { game ->
                                    viewModel.loadPackagesForGame(game.id)
                                    viewModel.navigateTo(AppScreen.GameDetail(game.id))
                                }
                            )
                        }
                        2 -> {
                            WalletScreen(
                                user = uiState.user,
                                transactions = uiState.transactions,
                                onToggleBalanceVisibility = { viewModel.toggleBalanceVisibility() },
                                onDepositClick = { viewModel.navigateTo(AppScreen.Deposit) },
                                onSendClick = { viewModel.navigateTo(AppScreen.Transfer) },
                                onTransactionClick = { tx ->
                                    viewModel.navigateTo(AppScreen.Receipt(tx.id))
                                }
                            )
                        }
                        3 -> {
                            ProfileScreen(
                                user = uiState.user,
                                onNavigateDetailedProfile = { viewModel.navigateTo(AppScreen.DetailedProfile) },
                                onNavigateEditProfile = { viewModel.navigateTo(AppScreen.EditProfile) },
                                onNavigateLanguage = { viewModel.navigateTo(AppScreen.LanguageSelection) },
                                onNavigateSecurity = { viewModel.navigateTo(AppScreen.SecuritySettings) },
                                onNavigateSessions = { viewModel.navigateTo(AppScreen.SessionManagement) },
                                onNavigateNotificationPrefs = { viewModel.navigateTo(AppScreen.NotificationPreferences) },
                                onNavigateHelpFaq = { viewModel.navigateTo(AppScreen.HelpFaq) },
                                onNavigateContactSupport = { viewModel.navigateTo(AppScreen.ContactSupport) },
                                onNavigateTerms = { viewModel.navigateTo(AppScreen.TermsOfService) },
                                onNavigatePrivacy = { viewModel.navigateTo(AppScreen.PrivacyPolicy) },
                                onOpenAdminPanel = { viewModel.navigateTo(AppScreen.AdminPanel) },
                                onLogoutClick = { viewModel.openLogoutDialog() }
                            )
                        }
                    }
                }

                // B. Écrans secondaires
                is AppScreen.Search -> {
                    SearchScreen(
                        games = uiState.games,
                        onGameClick = { game ->
                            viewModel.loadPackagesForGame(game.id)
                            viewModel.navigateTo(AppScreen.GameDetail(game.id))
                        },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.CategoryView -> {
                    CategoryScreen(
                        categoryName = screen.category,
                        games = uiState.games,
                        onGameClick = { game ->
                            viewModel.loadPackagesForGame(game.id)
                            viewModel.navigateTo(AppScreen.GameDetail(game.id))
                        },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.Notifications -> {
                    FullNotificationsScreen(
                        notifications = uiState.notifications,
                        onMarkAllRead = { viewModel.markAllNotificationsAsRead() },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }

                // C. Écrans de recharge de jeux
                is AppScreen.GameDetail -> {
                    val game = uiState.games.firstOrNull { it.id == screen.gameId }
                    if (game != null) {
                        GameDetailScreen(
                            game = game,
                            onStartRechargeClick = { viewModel.navigateTo(AppScreen.GameRecharge(game.id)) },
                            onBackClick = { viewModel.navigateBack() }
                        )
                    } else {
                        viewModel.navigateBack()
                    }
                }
                is AppScreen.GameRecharge -> {
                    val game = uiState.games.firstOrNull { it.id == screen.gameId }
                    if (game != null) {
                        DedicatedGameRechargeScreen(
                            game = game,
                            packages = packagesForSelectedGame,
                            user = uiState.user,
                            onConfirmOrder = { pkg, playerId, zoneId, method ->
                                viewModel.rechargeGame(game, pkg, playerId, zoneId, method)
                            },
                            onBackClick = { viewModel.navigateBack() }
                        )
                    } else {
                        viewModel.navigateBack()
                    }
                }
                is AppScreen.OrderTracking -> {
                    val order = uiState.orders.firstOrNull { it.id == screen.orderId } ?: uiState.orders.firstOrNull()
                    if (order != null) {
                        OrderTrackingScreen(
                            order = order,
                            onBackToHome = { viewModel.onTabSelected(0) },
                            onContactSupport = { orderNum ->
                                viewModel.navigateTo(AppScreen.ContactSupport)
                            }
                        )
                    } else {
                        viewModel.navigateBack()
                    }
                }
                is AppScreen.OrderSummary, is AppScreen.OrderDetail -> {
                    val order = uiState.orders.firstOrNull()
                    if (order != null) {
                        OrderTrackingScreen(
                            order = order,
                            onBackToHome = { viewModel.onTabSelected(0) },
                            onContactSupport = { viewModel.navigateTo(AppScreen.ContactSupport) }
                        )
                    } else {
                        viewModel.navigateBack()
                    }
                }

                // D. Écrans Wallet & Transactions
                is AppScreen.Deposit -> {
                    FullDepositScreen(
                        user = uiState.user,
                        onDepositSubmit = { amount, method, ref ->
                            viewModel.depositFunds(amount, method, ref)
                        },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.Transfer -> {
                    FullTransferScreen(
                        user = uiState.user,
                        onTransferSubmit = { recipient, amount, note ->
                            viewModel.sendFunds(recipient, amount, note)
                        },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.Withdraw -> {
                    FullWithdrawScreen(
                        user = uiState.user,
                        onWithdrawSubmit = { amount, method, account ->
                            viewModel.withdrawFunds(amount, method, account)
                        },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.TransactionHistory -> {
                    FullTransactionHistoryScreen(
                        transactions = uiState.transactions,
                        onTransactionClick = { tx -> viewModel.navigateTo(AppScreen.Receipt(tx.id)) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.Receipt, is AppScreen.TransactionDetail, is AppScreen.DepositConfirmation -> {
                    val txId = (screen as? AppScreen.Receipt)?.txId
                        ?: (screen as? AppScreen.TransactionDetail)?.txId
                        ?: (screen as? AppScreen.DepositConfirmation)?.txId
                    val tx = uiState.transactions.firstOrNull { it.id == txId } ?: uiState.transactions.firstOrNull()
                    if (tx != null) {
                        FullTransactionReceiptScreen(
                            transaction = tx,
                            onBackClick = { viewModel.navigateBack() }
                        )
                    } else {
                        viewModel.navigateBack()
                    }
                }
                is AppScreen.DepositDetail -> {
                    FullDepositScreen(
                        user = uiState.user,
                        onDepositSubmit = { amount, method, ref -> viewModel.depositFunds(amount, method, ref) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }

                // E. Écrans API & Développeurs
                is AppScreen.DeveloperPortal -> {
                    FullDeveloperPortalScreen(
                        apiServices = uiState.apiServices,
                        onOpenDocs = { viewModel.navigateTo(AppScreen.TopZynApiDoc) },
                        onOpenKeys = { viewModel.navigateTo(AppScreen.ApiKeysManagement) },
                        onOpenStatus = { viewModel.navigateTo(AppScreen.ApiStatus) },
                        onServiceClick = { service -> viewModel.navigateTo(AppScreen.TopZynApiDoc) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.TopZynApiDoc, is AppScreen.MonCashApi -> {
                    ApiDocumentationScreen(onBackClick = { viewModel.navigateBack() })
                }
                is AppScreen.ApiKeysManagement -> {
                    ApiKeysManagementScreen(onBackClick = { viewModel.navigateBack() })
                }
                is AppScreen.ApiStatus -> {
                    ApiStatusScreen(onBackClick = { viewModel.navigateBack() })
                }

                // F. Écrans Profil & Paramètres
                is AppScreen.DetailedProfile -> {
                    DetailedProfileScreen(
                        user = uiState.user,
                        onNavigateEdit = { viewModel.navigateTo(AppScreen.EditProfile) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.EditProfile -> {
                    EditProfileScreen(
                        user = uiState.user,
                        onSaveProfile = { name, email, phone -> viewModel.updateProfile(name, email, phone) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.LanguageSelection -> {
                    LanguageSelectionScreen(
                        currentLanguage = uiState.user?.language ?: "Français",
                        onLanguageSelected = { lang -> viewModel.updateLanguage(lang) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.SecuritySettings, is AppScreen.GeneralSettings -> {
                    SecuritySettingsScreen(
                        user = uiState.user,
                        onToggleBiometric = { viewModel.updateBiometric(it) },
                        onChangePin = {},
                        onNavigateChangePassword = { viewModel.navigateTo(AppScreen.ChangePassword) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.ChangePassword -> {
                    ResetPasswordScreen(
                        onResetSuccess = { viewModel.navigateBack() },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.SessionManagement -> {
                    SessionManagementScreen(onBackClick = { viewModel.navigateBack() })
                }
                is AppScreen.NotificationPreferences -> {
                    NotificationPreferencesScreen(
                        user = uiState.user,
                        onSavePrefs = { r, p, s -> viewModel.updateNotificationPrefs(r, p, s) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.HelpFaq -> {
                    HelpFaqScreen(
                        onNavigateContact = { viewModel.navigateTo(AppScreen.ContactSupport) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.ContactSupport -> {
                    ContactSupportScreen(onBackClick = { viewModel.navigateBack() })
                }
                is AppScreen.TermsOfService -> {
                    LegalTextScreen(
                        title = "Conditions d'Utilisation",
                        content = "Bienvenue sur TopZyn. En utilisant nos services de recharge et portefeuille en Gourdes (GDS), vous acceptez nos conditions générales. Tous les achats de diamants et devises numériques sont définitifs après livraison sur le serveur de jeu. TopZyn garantit la sécurité de vos fonds et applique les règles financières en vigueur en République d'Haïti.",
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.PrivacyPolicy -> {
                    LegalTextScreen(
                        title = "Politique de Confidentialité",
                        content = "TopZyn s'engage à protéger vos informations personnelles. Vos numéros MonCash et Natcash ne sont jamais partagés avec des tiers non autorisés. Les identifiants de jeu (UID) sont uniquement utilisés pour le routage de vos recharges numériques. Toutes les données sont chiffrées selon les standards de l'industrie.",
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
                is AppScreen.AdminPanel -> {
                    AdminPanelDialog(
                        apiServices = uiState.apiServices,
                        onToggleApiStatus = { id, currentStatus -> viewModel.adminToggleApiStatus(id, currentStatus) },
                        onAddGame = { name, cat, price, cur, draw -> viewModel.adminAddGame(name, cat, price, cur, draw) },
                        onDismiss = { viewModel.navigateBack() }
                    )
                }

                // G. Écrans d'état système
                is AppScreen.Maintenance -> {
                    FullMaintenanceScreen(onCheckStatus = { viewModel.onTabSelected(0) })
                }
                is AppScreen.SessionExpired -> {
                    FullSessionExpiredScreen(onReconnect = { viewModel.navigateTo(AppScreen.Login) })
                }
                is AppScreen.ErrorState -> {
                    FullConnectionErrorScreen(
                        onRetry = { viewModel.onTabSelected(0) },
                        onBackClick = { viewModel.navigateBack() }
                    )
                }
            }
        }
    }

    if (showLogoutDialog) {
        LogoutConfirmDialog(
            onConfirm = { viewModel.performLogout() },
            onDismiss = { viewModel.closeLogoutDialog() }
        )
    }
}
