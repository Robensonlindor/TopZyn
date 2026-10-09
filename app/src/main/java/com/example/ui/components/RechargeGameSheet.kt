package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.GameItem
import com.example.data.model.GamePackage
import com.example.data.model.UserAccount
import com.example.ui.theme.TopZynCardBorder
import com.example.ui.theme.TopZynDarkBlack
import com.example.ui.theme.TopZynGold
import com.example.ui.theme.TopZynGoldLight
import com.example.ui.theme.TopZynGreen
import com.example.ui.theme.TopZynGreenLight
import com.example.ui.theme.TopZynOrange
import com.example.ui.theme.TopZynOrangeContainer
import com.example.ui.theme.TopZynTextMuted
import com.example.ui.theme.TopZynTextPrimary
import com.example.ui.theme.TopZynTextSecondary
import com.example.ui.theme.TopZynWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RechargeGameSheet(
    game: GameItem,
    packages: List<GamePackage>,
    user: UserAccount?,
    onDismiss: () -> Unit,
    onConfirmRecharge: (pkg: GamePackage, playerId: String, paymentMethod: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var playerId by remember { mutableStateOf("") }
    var selectedPackageId by remember(packages) {
        mutableStateOf(packages.firstOrNull { it.isPopular }?.id ?: packages.firstOrNull()?.id ?: "")
    }
    var selectedPaymentMethod by remember { mutableStateOf("WALLET") } // "WALLET", "MONCASH", "NATCASH"
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val selectedPackage = packages.firstOrNull { it.id == selectedPackageId }

    // Resolve drawable
    val drawableRes = when (game.imageDrawableName) {
        "img_free_fire" -> R.drawable.img_free_fire
        "img_pubg_mobile" -> R.drawable.img_pubg_mobile
        "img_mobile_legends" -> R.drawable.img_mobile_legends
        "img_cod_mobile" -> R.drawable.img_cod_mobile
        else -> R.drawable.ic_topzyn_logo
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = TopZynWhite,
        dragHandle = null
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .testTag("recharge_game_sheet")
        ) {
            // Header: Title + Close Button
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(TopZynOrange.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.SportsEsports,
                                contentDescription = null,
                                tint = TopZynOrange,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Recharge ${game.name}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = TopZynTextPrimary
                            )
                            Text(
                                text = "Livraison directe et automatique",
                                fontSize = 12.sp,
                                color = TopZynTextSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFF4F4F5))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Fermer",
                            tint = TopZynDarkBlack,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Game Hero Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(TopZynDarkBlack)
                ) {
                    Image(
                        painter = painterResource(id = drawableRes),
                        contentDescription = game.name,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(TopZynDarkBlack.copy(alpha = 0.85f))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Unité : ${game.currencyName}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TopZynGold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Step 1: Input Player ID
            item {
                Text(
                    text = "1. Identifiant Joueur (Player ID)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynDarkBlack
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    value = playerId,
                    onValueChange = {
                        playerId = it
                        errorMessage = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("player_id_input"),
                    placeholder = { Text("Ex: 298410298", color = TopZynTextMuted) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TopZynOrange,
                        unfocusedBorderColor = TopZynCardBorder
                    ),
                    supportingText = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = TopZynTextSecondary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Trouvez votre ID dans le profil du jeu",
                                fontSize = 11.sp,
                                color = TopZynTextSecondary
                            )
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Step 2: Choose Package
            item {
                Text(
                    text = "2. Choisissez votre recharge (${game.currencyName})",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynDarkBlack
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            // Packages List
            items(packages) { pkg ->
                val isSelected = pkg.id == selectedPackageId
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { selectedPackageId = pkg.id }
                        .testTag("package_${pkg.id}"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) TopZynOrangeContainer.copy(alpha = 0.5f) else TopZynWhite
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) TopZynOrange else TopZynCardBorder
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) TopZynOrange else Color(0xFFF4F4F5)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Diamond,
                                    contentDescription = null,
                                    tint = if (isSelected) TopZynWhite else TopZynOrange,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column {
                                Text(
                                    text = pkg.title,
                                    fontSize = 14.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TopZynTextPrimary
                                )
                                if (pkg.bonus != null) {
                                    Text(
                                        text = pkg.bonus,
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TopZynGreen
                                    )
                                }
                            }
                        }

                        // Price
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "${"%.0f".format(pkg.priceGds)} GDS",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                color = if (isSelected) TopZynOrange else TopZynDarkBlack
                            )
                            if (pkg.isPopular) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(TopZynGoldLight)
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "POPULAIRE",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFB45309)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Step 3: Payment Method
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "3. Mode de paiement",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynDarkBlack
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Option: Solde Wallet TopZyn
                val walletBalance = user?.balanceGds ?: 0.0
                PaymentMethodRow(
                    title = "Solde TopZyn Wallet",
                    subtitle = "Solde disponible : ${"%.2f".format(walletBalance)} GDS",
                    isSelected = selectedPaymentMethod == "WALLET",
                    icon = Icons.Default.AccountBalanceWallet,
                    onClick = { selectedPaymentMethod = "WALLET" }
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Option: MonCash
                PaymentMethodRow(
                    title = "MonCash Haïti",
                    subtitle = "Paiement direct par téléphone",
                    isSelected = selectedPaymentMethod == "MONCASH",
                    icon = Icons.Default.PhoneAndroid,
                    onClick = { selectedPaymentMethod = "MONCASH" }
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Option: Natcash
                PaymentMethodRow(
                    title = "Natcash",
                    subtitle = "Paiement mobile Natcash",
                    isSelected = selectedPaymentMethod == "NATCASH",
                    icon = Icons.Default.PhoneAndroid,
                    onClick = { selectedPaymentMethod = "NATCASH" }
                )

                // Error message display
                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = errorMessage ?: "",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFDC2626)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Button: Recharger maintenant
                val requiredPrice = selectedPackage?.priceGds ?: 0.0
                Button(
                    onClick = {
                        if (playerId.isBlank()) {
                            errorMessage = "Veuillez entrer votre Identifiant Joueur (Player ID)"
                            return@Button
                        }
                        if (selectedPackage == null) {
                            errorMessage = "Veuillez sélectionner un pack"
                            return@Button
                        }
                        if (selectedPaymentMethod == "WALLET" && walletBalance < requiredPrice) {
                            errorMessage = "Solde Wallet insuffisant (${"%.2f".format(walletBalance)} GDS dispo). Déposez des fonds d'abord."
                            return@Button
                        }
                        onConfirmRecharge(selectedPackage, playerId.trim(), selectedPaymentMethod)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("confirm_recharge_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TopZynOrange,
                        contentColor = TopZynWhite
                    )
                ) {
                    Text(
                        text = "Recharger maintenant • ${"%.0f".format(requiredPrice)} GDS",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun PaymentMethodRow(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) TopZynOrangeContainer.copy(alpha = 0.3f) else TopZynWhite
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isSelected) TopZynOrange else TopZynCardBorder
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(
                    selectedColor = TopZynOrange
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) TopZynOrange else TopZynTextSecondary,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Text(
                    text = title,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = TopZynTextPrimary
                )
                Text(
                    text = subtitle,
                    fontSize = 11.5.sp,
                    color = TopZynTextSecondary
                )
            }
        }
    }
}
