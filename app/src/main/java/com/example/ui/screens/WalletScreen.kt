package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
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
import com.example.data.model.TransactionRecord
import com.example.data.model.UserAccount
import com.example.ui.components.TopZynWalletCard
import com.example.ui.theme.TopZynBackground
import com.example.ui.theme.TopZynCardBorder
import com.example.ui.theme.TopZynDarkBlack
import com.example.ui.theme.TopZynGreen
import com.example.ui.theme.TopZynOrange
import com.example.ui.theme.TopZynTextMuted
import com.example.ui.theme.TopZynTextPrimary
import com.example.ui.theme.TopZynTextSecondary
import com.example.ui.theme.TopZynWhite
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun WalletScreen(
    user: UserAccount?,
    transactions: List<TransactionRecord>,
    onToggleBalanceVisibility: () -> Unit,
    onDepositClick: () -> Unit,
    onSendClick: () -> Unit,
    onTransactionClick: (TransactionRecord) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("Tous") }
    val filters = listOf("Tous", "Dépôts", "Recharges", "Transferts")

    val filteredTransactions = transactions.filter { tx ->
        when (selectedFilter) {
            "Dépôts" -> tx.type == "DEPOT"
            "Recharges" -> tx.type == "RECHARGE"
            "Transferts" -> tx.type == "TRANSFERT"
            else -> true
        }
    }

    val dateFormat = SimpleDateFormat("dd MMM yyyy • HH:mm", Locale.FRANCE)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(TopZynBackground)
            .padding(horizontal = 20.dp)
            .testTag("wallet_screen")
    ) {
        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Wallet Title Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = TopZynOrange,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Mon Portefeuille TopZyn",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynDarkBlack
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Wallet Card
            TopZynWalletCard(
                user = user,
                onToggleVisibility = onToggleBalanceVisibility,
                onDepositClick = onDepositClick,
                onSendClick = onSendClick
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Filter Tabs for Transactions
            Text(
                text = "Historique des transactions",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TopZynDarkBlack
            )

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filters) { filter ->
                    val isSelected = selectedFilter == filter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                text = filter,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TopZynOrange,
                            selectedLabelColor = TopZynWhite,
                            containerColor = TopZynWhite,
                            labelColor = TopZynTextPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        if (filteredTransactions.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Aucune transaction enregistrée",
                        fontSize = 13.5.sp,
                        color = TopZynTextMuted
                    )
                }
            }
        } else {
            items(filteredTransactions) { tx ->
                val isPositive = tx.amountGds > 0
                val iconVector = when (tx.type) {
                    "DEPOT" -> Icons.Default.Add
                    "TRANSFERT" -> Icons.Default.NorthEast
                    else -> Icons.Default.SportsEsports
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onTransactionClick(tx) }
                        .testTag("tx_item_${tx.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = TopZynWhite),
                    border = androidx.compose.foundation.BorderStroke(1.dp, TopZynCardBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isPositive) TopZynGreen.copy(alpha = 0.12f)
                                        else TopZynOrange.copy(alpha = 0.12f)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = iconVector,
                                    contentDescription = null,
                                    tint = if (isPositive) TopZynGreen else TopZynOrange,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = tx.title,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TopZynTextPrimary
                                )
                                Text(
                                    text = tx.details,
                                    fontSize = 11.5.sp,
                                    color = TopZynTextSecondary
                                )
                                Text(
                                    text = dateFormat.format(Date(tx.timestamp)),
                                    fontSize = 10.5.sp,
                                    color = TopZynTextMuted
                                )
                            }
                        }

                        // Amount
                        Text(
                            text = if (isPositive) "+${"%.2f".format(tx.amountGds)} GDS"
                            else "${"%.2f".format(tx.amountGds)} GDS",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isPositive) TopZynGreen else TopZynDarkBlack
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
