package com.example.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.GameItem
import com.example.data.model.NotificationItem
import com.example.ui.components.PopularGameCard
import com.example.ui.components.TopZynSubPageHeader
import com.example.ui.theme.TopZynBackground
import com.example.ui.theme.TopZynCardBorder
import com.example.ui.theme.TopZynDarkBlack
import com.example.ui.theme.TopZynOrange
import com.example.ui.theme.TopZynTextMuted
import com.example.ui.theme.TopZynTextPrimary
import com.example.ui.theme.TopZynTextSecondary
import com.example.ui.theme.TopZynWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// 11. Écran de recherche
@Composable
fun SearchScreen(
    games: List<GameItem>,
    onGameClick: (GameItem) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var query by remember { mutableStateOf("") }
    val suggestedTags = listOf("Free Fire", "PUBG", "Mobile Legends", "COD", "Diamants", "UC")

    val results = if (query.isBlank()) emptyList() else games.filter {
        it.name.contains(query, ignoreCase = true) || it.category.contains(query, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Recherche",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_input_screen"),
                placeholder = { Text("Rechercher un jeu ou un service...", color = TopZynTextMuted) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TopZynOrange) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { query = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Effacer")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TopZynOrange,
                    unfocusedBorderColor = TopZynCardBorder,
                    focusedContainerColor = TopZynWhite,
                    unfocusedContainerColor = TopZynWhite
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text("Recherches populaires", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = TopZynDarkBlack)

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(suggestedTags) { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFF4F4F5))
                            .clickable { query = tag }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(text = tag, fontSize = 12.sp, color = TopZynTextPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (query.isNotBlank()) {
                Text(
                    text = "${results.size} résultat(s) pour « $query »",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynDarkBlack
                )

                Spacer(modifier = Modifier.height(10.dp))

                if (results.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Aucun jeu trouvé pour cette recherche", color = TopZynTextMuted, fontSize = 13.5.sp)
                    }
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        val pairs = results.chunked(2)
                        items(pairs) { pair ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                pair.forEach { game ->
                                    Box(modifier = Modifier.weight(1f)) {
                                        PopularGameCard(game = game, onClick = { onGameClick(game) })
                                    }
                                }
                                if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

// 12. Écran de catégorie
@Composable
fun CategoryScreen(
    categoryName: String,
    games: List<GameItem>,
    onGameClick: (GameItem) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryGames = games.filter { it.category.contains(categoryName, ignoreCase = true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = categoryName,
            subtitle = "${categoryGames.size} jeux disponibles",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            val pairs = categoryGames.chunked(2)
            items(pairs) { pair ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    pair.forEach { game ->
                        Box(modifier = Modifier.weight(1f)) {
                            PopularGameCard(game = game, onClick = { onGameClick(game) })
                        }
                    }
                    if (pair.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

// 13. Écran de notifications complet
@Composable
fun FullNotificationsScreen(
    notifications: List<NotificationItem>,
    onMarkAllRead: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("Toutes") }
    val filters = listOf("Toutes", "Non lues", "Recharges", "Wallet")
    val dateFormat = SimpleDateFormat("dd MMM, HH:mm", Locale.FRANCE)

    val filtered = notifications.filter {
        when (selectedFilter) {
            "Non lues" -> !it.isRead
            "Recharges" -> it.type == "RECHARGE"
            "Wallet" -> it.type == "WALLET"
            else -> true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
    ) {
        TopZynSubPageHeader(
            title = "Notifications",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(filters) { f ->
                        FilterChip(
                            selected = selectedFilter == f,
                            onClick = { selectedFilter = f },
                            label = { Text(f, fontSize = 11.5.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = TopZynOrange,
                                selectedLabelColor = TopZynWhite
                            )
                        )
                    }
                }

                Button(
                    onClick = onMarkAllRead,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF4F4F5), contentColor = TopZynDarkBlack),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Tout marquer lu", fontSize = 11.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (filtered.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aucune notification", color = TopZynTextMuted, fontSize = 13.5.sp)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(filtered) { notif ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (!notif.isRead) Color(0xFFFFF7ED) else TopZynWhite
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (!notif.isRead) TopZynOrange.copy(alpha = 0.4f) else TopZynCardBorder
                            )
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(notif.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = TopZynTextPrimary)
                                    Text(dateFormat.format(Date(notif.timestamp)), fontSize = 11.sp, color = TopZynTextMuted)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(notif.message, fontSize = 12.5.sp, color = TopZynTextSecondary, lineHeight = 17.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
