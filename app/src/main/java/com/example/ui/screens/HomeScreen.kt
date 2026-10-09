package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.model.ApiServiceModel
import com.example.data.model.GameItem
import com.example.data.model.UserAccount
import com.example.ui.components.DeveloperApiSection
import com.example.ui.components.PopularGamesSection
import com.example.ui.components.TopZynHeader
import com.example.ui.components.TopZynWalletCard
import com.example.ui.theme.TopZynBackground

@Composable
fun HomeScreen(
    user: UserAccount?,
    unreadNotificationsCount: Int,
    games: List<GameItem>,
    apiServices: List<ApiServiceModel>,
    onToggleBalanceVisibility: () -> Unit,
    onDepositClick: () -> Unit,
    onSendClick: () -> Unit,
    onGameClick: (GameItem) -> Unit,
    onApiServiceClick: (ApiServiceModel) -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
            .verticalScroll(scrollState)
    ) {
        // 1. En-tête de la page (Header)
        TopZynHeader(
            user = user,
            unreadNotificationsCount = unreadNotificationsCount,
            onNotificationsClick = onNotificationsClick,
            onProfileClick = onProfileClick
        )

        // Main content padded area
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(14.dp))

            // 2. Carte Wallet (Solde disponible)
            TopZynWalletCard(
                user = user,
                onToggleVisibility = onToggleBalanceVisibility,
                onDepositClick = onDepositClick,
                onSendClick = onSendClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 3. Section API et développeurs
            DeveloperApiSection(
                apiServices = apiServices,
                onServiceClick = onApiServiceClick
            )

            // 4. Section des jeux populaires
            PopularGamesSection(
                games = games,
                onGameClick = onGameClick
            )

            // Bottom breathing room so bottom navigation doesn't mask anything
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
