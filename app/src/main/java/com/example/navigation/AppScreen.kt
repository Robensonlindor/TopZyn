package com.example.navigation

sealed interface AppScreen {
    // Main Tab Screens (Show bottom bar)
    data class Main(val tabIndex: Int = 0) : AppScreen // 0=Accueil, 1=Boutique, 2=Wallet, 3=Profil

    // Auth Screens
    data object Splash : AppScreen
    data object Welcome : AppScreen
    data object Login : AppScreen
    data object Register : AppScreen
    data class VerifyOtp(val contact: String) : AppScreen
    data object ForgotPassword : AppScreen
    data object ResetPassword : AppScreen

    // Main secondary screens
    data object Search : AppScreen
    data class CategoryView(val category: String) : AppScreen
    data object Notifications : AppScreen

    // Game recharge screens
    data class GameDetail(val gameId: String) : AppScreen
    data class GameRecharge(val gameId: String) : AppScreen
    data class OrderSummary(val orderId: String) : AppScreen
    data class OrderTracking(val orderId: String) : AppScreen
    data class OrderDetail(val orderId: String) : AppScreen

    // Wallet & Transactions screens
    data object Deposit : AppScreen
    data class DepositDetail(val amount: Double, val method: String) : AppScreen
    data class DepositConfirmation(val txId: String) : AppScreen
    data object Transfer : AppScreen
    data object Withdraw : AppScreen
    data object TransactionHistory : AppScreen
    data class TransactionDetail(val txId: String) : AppScreen
    data class Receipt(val txId: String) : AppScreen

    // API & Developers screens
    data object DeveloperPortal : AppScreen
    data object TopZynApiDoc : AppScreen
    data object MonCashApi : AppScreen
    data object ApiKeysManagement : AppScreen
    data object ApiStatus : AppScreen

    // Profile & Settings screens
    data object DetailedProfile : AppScreen
    data object EditProfile : AppScreen
    data object GeneralSettings : AppScreen
    data object LanguageSelection : AppScreen
    data object SecuritySettings : AppScreen
    data object ChangePassword : AppScreen
    data object SessionManagement : AppScreen
    data object NotificationPreferences : AppScreen
    data object HelpFaq : AppScreen
    data object ContactSupport : AppScreen
    data object TermsOfService : AppScreen
    data object PrivacyPolicy : AppScreen
    data object AdminPanel : AppScreen

    // System States
    data object Maintenance : AppScreen
    data object SessionExpired : AppScreen
    data class ErrorState(val message: String) : AppScreen
}
