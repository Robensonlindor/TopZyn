package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.NorthEast
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserAccount
import com.example.ui.theme.TopZynDarkBlack
import com.example.ui.theme.TopZynDarkCard
import com.example.ui.theme.TopZynGold
import com.example.ui.theme.TopZynOrange
import com.example.ui.theme.TopZynWhite
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TopZynWalletCard(
    user: UserAccount?,
    onToggleVisibility: () -> Unit,
    onDepositClick: () -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val balance = user?.balanceGds ?: 0.0
    val isVisible = user?.isBalanceVisible ?: true

    // Format balance in Haitian Gourdes (GDS)
    val numberFormatter = NumberFormat.getNumberInstance(Locale.FRANCE).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    val formattedBalance = if (balance == 0.0) "0 GDS" else "${numberFormatter.format(balance)} GDS"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("wallet_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = TopZynDarkBlack
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1B1B20),
                            TopZynDarkBlack
                        )
                    )
                )
                .border(
                    width = 1.dp,
                    color = Color(0xFF2C2C35),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header of Wallet Card: SOLDE DISPONIBLE + Eye Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(TopZynOrange.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.AccountBalanceWallet,
                                contentDescription = null,
                                tint = TopZynOrange,
                                modifier = Modifier.size(14.dp)
                            )
                        }

                        Text(
                            text = "SOLDE DISPONIBLE",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFA1A1AA),
                            letterSpacing = 1.2.sp
                        )
                    }

                    // Eye Icon to hide/show balance
                    IconButton(
                        onClick = onToggleVisibility,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF26262B))
                            .testTag("toggle_balance_button")
                    ) {
                        Icon(
                            imageVector = if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (isVisible) "Masquer le solde" else "Afficher le solde",
                            tint = if (isVisible) Color(0xFFD4D4D8) else TopZynGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Real-time Balance Display
                Row(
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isVisible) {
                        Text(
                            text = formattedBalance,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Black,
                            color = TopZynWhite,
                            letterSpacing = (-0.5).sp
                        )
                    } else {
                        Text(
                            text = "•••••• GDS",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black,
                            color = TopZynWhite.copy(alpha = 0.8f),
                            letterSpacing = 2.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Short explanation
                Text(
                    text = "Commencez par ajouter de l’argent à votre compte pour pouvoir effectuer des recharges.",
                    fontSize = 12.5.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFFA1A1AA)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Two large buttons on the same line: + Déposer (Orange) and ↗ Envoyer (Dark Gray)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // + Déposer Button
                    Button(
                        onClick = onDepositClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("deposit_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TopZynOrange,
                            contentColor = TopZynWhite
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = TopZynWhite
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Déposer",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // ↗ Envoyer Button
                    Button(
                        onClick = onSendClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("send_button"),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF27272A),
                            contentColor = TopZynWhite
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3F3F46))
                    ) {
                        Icon(
                            imageVector = Icons.Default.NorthEast,
                            contentDescription = null,
                            modifier = Modifier.size(17.dp),
                            tint = Color(0xFFE4E4E7)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Envoyer",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TopZynWhite
                        )
                    }
                }
            }
        }
    }
}
